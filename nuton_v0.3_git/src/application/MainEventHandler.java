package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ffmpeg.FfmpegHandler;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import properties.PropertiesReader;

public class MainEventHandler {

	private MainController mainController;
	private PropertiesReader propReader;
	
	public MainEventHandler(MainController mainController) {
		this.mainController = mainController;
	}
	
	private void openMedia(File media) {	
		mainController.setMedia(new Media(media.toURI().toString()));
		mainController.setPlayer(new MediaPlayer(mainController.getMedia()));
		mainController.getMv().setMediaPlayer(mainController.getPlayer());
		mainController.getPlayer().setMute(true);
		mainController.getSettingsController().reset();
		mainController.reset();
		mainController.getStartBtn().setDisable(false);
		System.out.println("Öffne das Video...");
		//mainController.getMv().setPreserveRatio(true);
	}
	
	public EventHandler<ActionEvent> openFileDialog() {
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				
				FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Video Dateien (*.fxm), (*.flv), (*.mp4), (*.m4v)", "*.fxm", "*.flv", "*.mp4", "*.m4v");
				fileChooser.getExtensionFilters().add(filter);
				File mediaFile = fileChooser.showOpenDialog(null);
				
				if (mediaFile != null) {
					
				
					openMedia(mediaFile);
				
					//Prüft, ob der MediaPlayer bereit ist
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
				String videoPath;
				videoPath = chooser.showOpenDialog(null).getAbsolutePath();
				
				String outputPath = null;
				if (propReader.isFfmpegOutputSame()) {
					outputPath = propReader.getFfmpegSameOutputPath();
				}
				
				String name = "output";
				FfmpegHandler handler = new FfmpegHandler(videoPath, outputPath, name);
				openMedia(handler.getVideo());
				
				
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
			}
			
		};
		
		return event;
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
					stage.getIcons().add(new Image(SettingsController.class.getResourceAsStream("Nuton_logo.png")));
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
					mainController.getSettingsController().show();
				}			
			}
			
		};
		return event;
	}
	
	public EventHandler<ActionEvent> reset() {
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainController.reset();			
			}
			
		};
		return event;
	}
	
	public void backwardButton() {
		ArrayList<Point> points = mainController.getStateManager().getPoints();
		if (points != null) {
			double time = points.get(points.size()-1).getTime();
			if (time == mainController.getSlider().getValue() - mainController.getSCHRITTWEITE() && mainController.getSlider().getValue() - mainController.getSCHRITTWEITE() >= 0) {
				points.get(points.size()-1).removePoint(mainController.getGc());
				points.remove(points.size()-1);
				mainController.getSlider().setValue(mainController.getSlider().getValue() - mainController.getSCHRITTWEITE());
			}
		} else if (mainController.getSlider().getValue() - mainController.getSCHRITTWEITE() >= 0){
			mainController.getSlider().setValue(mainController.getSlider().getValue() - mainController.getSCHRITTWEITE());
		}
	}
	
	public MainController getMainController() {
		return mainController;
	}

	
	//Toolbarevents
	public void setPoint(MouseEvent e, ArrayList<Point> points, GraphicsContext gc) {
		
	}
	
}
