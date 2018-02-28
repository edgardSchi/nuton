package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ffmpeg.FfmpegHandler;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import properties.PropertiesReader;
import properties.PropertiesWriter;
import savingFile.SaveHandler;
import states.StateManager;

public class MainEventHandler {

	private MainController mainController;
	private PropertiesReader propReader;
	private PropertiesWriter propWriter;
	
	public MainEventHandler(MainController mainController) {
		this.mainController = mainController;
		propWriter = new PropertiesWriter();
	}
	
	public void openMedia(File media) {
		if (media != null) {
			mainController.setPlayer(new MediaPlayer(new Media(media.toURI().toString())));
			mainController.getPlayer().setOnReady(new Runnable() {

				@Override
				public void run() {
					//slider.setMin(0);
					mainController.getMv().setPreserveRatio(true);
					DoubleProperty mvw = mainController.getMv().fitWidthProperty();
					DoubleProperty mvh = mainController.getMv().fitHeightProperty();
					DoubleProperty canvasX = mainController.getCanvas().layoutXProperty();
					DoubleProperty canvasW = mainController.getCanvas().widthProperty();
					DoubleProperty canvasH = mainController.getCanvas().heightProperty();
					
					mvw.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "width"));
					mvh.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "height"));
					canvasW.bind(Bindings.selectDouble(mainController.getMv().boundsInParentProperty(), "width"));
					canvasH.bind(Bindings.selectDouble(mainController.getMv().boundsInParentProperty(), "height"));
					
					
					//Synchronisiert die Scrolls der ListViews. Kann erst gemacht werden, wenn die ListViews gerendert sind
					Node n = mainController.getListX().lookup(".scroll-bar");
			        if (n instanceof ScrollBar) {
			            final ScrollBar bar = (ScrollBar) n;
			            if (bar.getOrientation().equals(Orientation.VERTICAL)) {
			                Node ny = mainController.getListY().lookup(".scroll-bar");
			                final ScrollBar bary = (ScrollBar) ny;
			                bar.valueProperty().bindBidirectional(bary.valueProperty());
			            }
			        }
					
					System.out.println("MEDIA READY");
					
					mainController.getSlider().setMinorTickCount(0);
					mainController.getSlider().setMajorTickUnit(1000);
					mainController.getSlider().setMax(mainController.getPlayer().getTotalDuration().toMillis());
					mainController.setMediaLength(mainController.getPlayer().getTotalDuration().toMillis());
					
				
				
					System.out.println("X:" + mainController.getMv().getLayoutX());
				}
				
			});
			mainController.getMv().setMediaPlayer(mainController.getPlayer());
			mainController.getPlayer().setMute(true);
			
			mainController.getSettingsController().reset();
			mainController.reset();		
			mainController.getStartBtn().setDisable(false);
			//mainController.getSettingsController().showDialog();
		} else {
			System.out.println("MEDIA NULL");
		}

	}
	
	public EventHandler<ActionEvent> openFileDialog() {
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				propReader = new PropertiesReader();
				FileChooser fileChooser = new FileChooser();
				
				if (!propReader.getLastPath().isEmpty()) {
					if (new File(propReader.getLastPath()).isDirectory()) {
						fileChooser.setInitialDirectory(new File(propReader.getLastPath()));
					} else {
						propWriter.setLastPath("");
					}
				}
				
				FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Video Dateien (*.fxm), (*.flv), (*.mp4), (*.m4v)", "*.fxm", "*.flv", "*.mp4", "*.m4v");
				fileChooser.getExtensionFilters().add(filter);
				File mediaFile = fileChooser.showOpenDialog(Main.getStage());
				
				if (mediaFile != null) {
					openMedia(mediaFile);
					System.out.println(mediaFile.getAbsolutePath());
					propWriter.setLastPath(mediaFile.getParent());
					propWriter.confirm();
					savingFile.TempSaving.setURL(mediaFile.getAbsolutePath());
					savingFile.TempSaving.withFfmpeg(false);
				}
			}		
		};
		
		return event;		
	}
	
	public EventHandler<ActionEvent> ffmpegOpenFileDialog() {
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				propReader = new PropertiesReader();
				FileChooser chooser = new FileChooser();
				
				if (!propReader.getLastPath().isEmpty()) {
					if (new File(propReader.getLastPath()).exists()) {
						chooser.setInitialDirectory(new File(propReader.getLastPath()));
					} else {
						propWriter.setLastPath("");
					}
				}
				
				String videoPath;
				File video;
				video = chooser.showOpenDialog(Main.getStage());
				
				if (video != null) {
					videoPath = video.getAbsolutePath();
					String outputPath = null;
					if (propReader.isFfmpegOutputSame()) {
						outputPath = propReader.getFfmpegSameOutputPath();
					}
					
					String name = "output";
					FfmpegHandler handler = new FfmpegHandler(videoPath, outputPath, name);
					
					if (!handler.getVideo().exists()) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Ein Fehler ist aufgetreten!");
						alert.setContentText("Das Video konnte nicht gefunden werden. Überprüfen Sie, ob der Pfad zu ffmpeg korrekt ist.");

						alert.showAndWait();
					} else {
						openMedia(handler.getVideo());
						
						propWriter.setLastPath(video.getParent());
						propWriter.confirm();
						savingFile.TempSaving.setURL(video.getAbsolutePath());
						savingFile.TempSaving.withFfmpeg(true);
		
					}				

				}			

			}
			
		};
		
		return event;
	}
	
	public void openMediaDialogWithoutReset() {
		propReader = new PropertiesReader();
		FileChooser fileChooser = new FileChooser();
		
		if (!propReader.getLastPath().isEmpty()) {
			if (new File(propReader.getLastPath()).isDirectory()) {
				fileChooser.setInitialDirectory(new File(propReader.getLastPath()));
			} else {
				propWriter.setLastPath("");
			}
		}
		
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Video Dateien (*.fxm), (*.flv), (*.mp4), (*.m4v)", "*.fxm", "*.flv", "*.mp4", "*.m4v");
		fileChooser.getExtensionFilters().add(filter);
		File mediaFile = fileChooser.showOpenDialog(Main.getStage());
		
		if (mediaFile != null) {
			mainController.setPlayer(new MediaPlayer(new Media(mediaFile.toURI().toString())));
			mainController.getPlayer().setOnReady(new Runnable() {

				@Override
				public void run() {
					//slider.setMin(0);
					DoubleProperty mvw = mainController.getMv().fitWidthProperty();
					DoubleProperty mvh = mainController.getMv().fitHeightProperty();
					DoubleProperty canvasW = mainController.getCanvas().widthProperty();
					DoubleProperty canvasH = mainController.getCanvas().heightProperty();
					
					mvw.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "width"));
					mvh.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "height"));
					canvasW.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "width"));
					canvasH.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "height"));
					
					mainController.getSlider().setMinorTickCount(0);
					mainController.getSlider().setMajorTickUnit(1000);
					mainController.getSlider().setMax(mainController.getPlayer().getTotalDuration().toMillis());
					mainController.setMediaLength(mainController.getPlayer().getTotalDuration().toMillis());
				
					System.out.println("Running");
				}
				
			});
			mainController.getMv().setMediaPlayer(mainController.getPlayer());
			mainController.getPlayer().setMute(true);
			
			mainController.getSettingsController().reset();
			mainController.reset();		
			mainController.getStartBtn().setDisable(false);
			//mainController.getSettingsController().showDialog();
		}
			
		System.out.println(mediaFile.getAbsolutePath());
		propWriter.setLastPath(mediaFile.getParent());
		propWriter.confirm();
		savingFile.TempSaving.setURL(mediaFile.getAbsolutePath());
		savingFile.TempSaving.withFfmpeg(false);
	}

	
	public EventHandler<ActionEvent> openAnleitungDialog() {		
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					Parent loader = FXMLLoader.load(getClass().getResource("Anleitung.fxml"));
					
					Scene scene = new Scene(loader);
					Stage stage = new Stage();
					stage.setTitle("Anleitung");
					stage.setScene(scene);
					stage.getIcons().add(new Image(MainEventHandler.class.getResourceAsStream("Nuton_logo.png")));
					stage.show();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		};
		
		return event;
	}
	
	public EventHandler<ActionEvent> closeProgram() {
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
                System.exit(0);
			}
		};
		return event;
	}
	
	
	public EventHandler<ActionEvent> openSettings() {
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (mainController.getMedia() == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Videodatei auswählen!");
					alert.setContentText("Wählen Sie zuerst eine geeignete Videodatei aus.");
					alert.setHeaderText(null);
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							alert.close();
						}
					});
				} else {
					mainController.getStartBtn().setDisable(true);
					mainController.getSettingsController().showDialog();
				}			
			}
			
		};
		return event;
	}
	
	public EventHandler<ActionEvent> reset() {
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainController.getStartBtn().setDisable(false);
				mainController.getStateManager().getCurrentState().reset();
				mainController.getStateManager().setState(StateManager.DEFAULT);
				toolBarEvents.AddPointEvents.reset();
			}
			
		};
		return event;
	}
	
	public void backwardButton() {
		ArrayList<Point> points = mainController.getStateManager().getPoints();
		double schrittweite = mainController.getSettings().getSchrittweite();
		if (points != null) {
			double time = 0;
			if (points.size()-1 > 0) {
				time = points.get(points.size()-1).getTime();
			}
			
			if (time == mainController.getSlider().getValue() - schrittweite && mainController.getSlider().getValue() - schrittweite >= 0) {
//				points.get(points.size()-1).removePoint(mainController.getGc());
//				points.remove(points.size()-1);
				mainController.getSlider().setValue(mainController.getSlider().getValue() - schrittweite);
//				mainController.getListX().getItems().remove(points.size());
//				mainController.getListY().getItems().remove(points.size());
			} else if (mainController.getSlider().getValue() - schrittweite >= 0){
				mainController.getSlider().setValue(mainController.getSlider().getValue() - schrittweite);
			}
		} else if (mainController.getSlider().getValue() - schrittweite >= 0){
			mainController.getSlider().setValue(mainController.getSlider().getValue() - schrittweite);
		}
	}
	
	public MainController getMainController() {
		return mainController;
	}
	
}
