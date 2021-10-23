/*******************************************************************************
 * Nuton
 *   Copyright (C) 2018-2019 Edgard Schiebelbein
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.nuton.application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import de.nuton.application.settingsPane.SettingsController;
import de.nuton.draw.VideoPainter;
import de.nuton.ffmpeg.FfmpegHandler;
import de.nuton.math.UnitsHandler.LengthUnit;
import de.nuton.math.UnitsHandler.TimeUnit;
import de.nuton.properties.PropertiesReader;
import de.nuton.properties.PropertiesWriter;
import de.nuton.savingFile.LoadHandler;
import de.nuton.savingFile.SaveHandler;
import de.nuton.settings.Settings;
import de.nuton.states.StateManager;
import de.nuton.toolBar.ToolBarManager;
import de.nuton.tracking.TrackingSettingsController;
import de.nuton.ui.UIUtils;
import de.nuton.userSettings.ProgramSettingsController;
import de.nuton.userSettings.ThemeLoader;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MainController implements Initializable{
	
	@FXML private MediaView mv;
	private MediaPlayer player;
	private Media media;
	private SettingsController settingsController;
	@FXML private MenuItem openFileMenu;
	@FXML private MenuItem einstellungenItem;
	@FXML private MenuItem ffmpegMenu;
	@FXML private MenuItem closeMenu;
	@FXML private MenuItem menuAnleitung;
	@FXML private MenuItem loadProjectMenu;
	@FXML private MenuItem saveFileMenu;
	@FXML private MenuItem saveFileAsMenu;
	@FXML private MenuItem startTrackingMenu;
	@FXML private MenuItem  menuOpenWiki;
	@FXML private MenuItem menuAbout;
	@FXML private MenuItem menuUpdates;
	@FXML private Canvas canvas; 
	@FXML private Canvas streamCanvas;
	@FXML public Slider slider;
	@FXML public Button startBtn;
	@FXML public Button restartBtn;
	@FXML public Button fertigBtn;
	@FXML private Button saveSettingsBtn;
	@FXML private TableView<Point> tableView;
	@FXML private ToolBar toolBar;
	@FXML private StackPane stackPane;
	@FXML private Label helpLabel;
	
	//@FXML private MenuItem startCameraMenu;
	//@FXML private MenuItem stopCameraMenu;

	private ToolBarManager tbm;
	
	private double mediaLength = 0;
	
	private ThemeLoader themeLoader;
	private ProgramSettingsController pSettings;
	private PixelManager pManager;
	private StateManager stateManager;
	private Settings settings;
	private LoadHandler loadHandler;
	private ScalingManager scalingManager;
	private HostServices hostServices;
	private TrackingSettingsController trackingController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initControllers();
		initGuiEvents();
	}
	
	/**
	 * Initialisiert die verschiedenen Controller.
	 */
	private void initControllers() {
		themeLoader = new ThemeLoader();
		scalingManager = ScalingManager.getInstance();
		settings = new Settings();
		VideoPainter.init(canvas);
		
		settingsController = new SettingsController(this, settings, themeLoader);
		
		trackingController = new TrackingSettingsController(this, themeLoader);
		
		pManager = new PixelManager(this);
		
		hostServices = (HostServices)MainFX.getStage().getProperties().get("hostServices");
		
		pSettings = new ProgramSettingsController(this, themeLoader);

	}
	
	/**
	 * Initialisiert die Eventhandler der verschiedenen GUI-Items.
	 */
	private void initGuiEvents() {
		//Für Livecamera, wurde rausgenommen
//		startCameraMenu.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent arg0) {
//				//stateManager.setState(StateManager.STREAMING);
//			}
//			
//		});
//		
//		stopCameraMenu.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent arg0) {
////				canvasW.unbind();
////				canvasH.unbind();
//			}
//			
//		});
		
		openFileMenu.setOnAction(event -> openVideoDialog());
		
		ffmpegMenu.setOnAction(event -> openVideoWithFfmpegDialog());
		
		closeMenu.setOnAction(event -> closeProgram());
		
		
		tbm = new ToolBarManager(toolBar, this);
		initMenuItems();

		stateManager = new StateManager(this);
		SaveHandler saveHandler = new SaveHandler(this);
		loadHandler = new LoadHandler(this, settings);
		
		einstellungenItem.setOnAction(event -> pSettings.showDialog());
		
		loadProjectMenu.setOnAction(event -> {
			FileChooser chooser = new FileChooser();
			FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Nuton Datein (*.ntn)", "*.ntn");
			chooser.getExtensionFilters().add(filter);
			File file = chooser.showOpenDialog(MainFX.getStage());
			//TODO: Nach Point refactor ist defect
			//loadHandler.load(file);
		});
		
		saveFileMenu.setOnAction(event -> {
			if (player != null && player.getMedia() != null) {
				saveHandler.save();
			} else {
				UIUtils.showErrorAlert("Es ist nichts zum speichern vorhanden.");
			}
		});
		
		saveFileAsMenu.setOnAction(event -> {
			if (player != null && player.getMedia() != null) {
				saveHandler.setSaveAs(true);
				saveHandler.save();
			} else {
				UIUtils.showErrorAlert("Es ist nichts zum speichern vorhanden.");
			}
		});

		
		if (getPlayer() != null) {
			getPlayer().currentTimeProperty().addListener((arg0, arg1, arg2) -> slider.setValue(getPlayer().getCurrentTime().toSeconds()));
		}


		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (getPlayer() != null) {
				getPlayer().seek(Duration.millis((double)newValue));
			}

		});
		
		//Verbessern
		startBtn.setOnAction(event -> {
			if (mv.getMediaPlayer() == null || mv.getMediaPlayer().getMedia() == null) {
				UIUtils.showErrorAlert("Wählen Sie zuerst eine geeignete Videodatei aus.");
			} else {
				getStartBtn().setDisable(true);
				getSettingsController().showDialog();
			}
		});
		
		
		
		canvas.addEventFilter(MouseEvent.ANY, e -> stateManager.onClick(e));
		
		restartBtn.setOnAction(event -> restart());
		
		
		fertigBtn.setOnAction(event -> stateManager.fertigBtnClick());
		
		canvas.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
			scalingManager.setCanvasDimension(getCanvas().getWidth(), getCanvas().getHeight());
			if (stateManager.getCurrentState().getPoints() != null) {
/*				for (Point p : stateManager.getCurrentState().getPoints()) {
					System.out.println("X: " + p.getDrawX() + "Y: " + p.getDrawY());
					scalingManager.updatePointPos(p);
				}
				for(Point p : stateManager.getCurrentState().getCalibratePoints()) {
					if(p != null) {
						scalingManager.updatePointPos(p);
					}
				}
				if(pManager.getOrigin() != null) {
					scalingManager.updatePointPos(pManager.getOrigin());
				}*/
				redraw();
			}
		});
		
		initTableView();
		
		
		startTrackingMenu.setOnAction(event -> trackingController.showDialog());
		
		menuOpenWiki.setOnAction(event -> getHostServices().showDocument("https://github.com/edgardSchi/nuton/wiki"));
		
		menuUpdates.setOnAction(event -> {
			UpdateChecker checker = new UpdateChecker("https://edgardschi.github.io/nuton-website/version.html", MainFX.VERSION);
			if(checker.readData()) {
				checker.checkVersion();
			} else {
				checker.errorDialog();
			}
		});
		
		menuAbout.setOnAction(event -> openAboutDialog());
		
		

		
		setHelpLabel("Video öffnen");
			
		
	}
	
	/**
	 * Der aktuelle Zustand auf dem Canvas neu gezeichnet.
	 */
	public void redraw() {
		stateManager.redraw();
	}
	
	/**
	 * Setzt den MainController auf den Anfangszustand zurück.
	 */
	public void reset() {
		settings.setLengthUnit(LengthUnit.CM);
		settings.setTimeUnit(TimeUnit.MS);
		settings.setSchrittweite(1000);
		settings.setEichung(100);
		slider.setValue(0);
		slider.setDisable(false);
		slider.setSnapToTicks(false);
		VideoPainter.getInstance().clearScreen();
		startBtn.setDisable(false);
		pManager.reset();
		stateManager.setState(StateManager.DEFAULT);
	}
	
	/**
	 * Initialisiert die Tastenkürzel für die MenuItems in der Menüleiste, sowie die Icons.
	 */
	private void initMenuItems() {
		openFileMenu.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		Image openVideoIcon = new Image(getClass().getResourceAsStream("/openFileIcon.png"));
		ImageView openVideoIconView = new ImageView(openVideoIcon);
		openVideoIconView.setFitWidth(15);
		openVideoIconView.setFitHeight(15);
		openFileMenu.setGraphic(openVideoIconView);
		ffmpegMenu.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		einstellungenItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		//saveFileMenu.setDisable(true);
		saveFileMenu.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		Image saveIcon = new Image(getClass().getResourceAsStream("/saveIcon.png"));
		ImageView saveIconView = new ImageView(saveIcon);
		saveIconView.setFitWidth(15);
		saveIconView.setFitHeight(15);
		saveFileMenu.setGraphic(saveIconView);
	}
	
	/**
	 * Initialisiert die Spalten der TableView, sowie den Listener.
	 */
	private void initTableView() {
		tableView.setPlaceholder(new Label("Keine Punkte ausgewählt"));
		tableView.getColumns().clear();
		TableColumn<Point, Integer> timeCol = new TableColumn<>("Zeit[ms]");
		timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
		timeCol.setResizable(false);
		timeCol.setPrefWidth(53);
		timeCol.setSortable(false);
		TableColumn<Point, Integer> xCol = new TableColumn<>("X[px]");
		xCol.setCellValueFactory(new PropertyValueFactory<>("x"));
		xCol.setResizable(false);
		xCol.setPrefWidth(54);
		xCol.setSortable(false);
		TableColumn<Point, Integer> yCol = new TableColumn<>("Y[px]");
		yCol.setCellValueFactory(new PropertyValueFactory<>("y"));
		yCol.setResizable(false);
		yCol.setPrefWidth(54);
		yCol.setSortable(false);
		
		tableView.getColumns().addAll(timeCol, xCol, yCol);
		
		tableView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.intValue() != -1) {
				double t = stateManager.getPoints().get(newValue.intValue()).getTime();
				slider.setValue(t);
			}
		});
	}
	/**
	 * Aktualisiert die TableView der ausgewählten Punkte.
	 */
	public void updateLists() {
		tableView.getItems().clear();
		for(Point p : stateManager.getPoints()) {
			tableView.getItems().add(p);
		}
	}

	public void openMedia(File media) {
		reset();
		if (media != null) {
			player = new MediaPlayer(new Media(media.toURI().toString()));
			player.setOnReady(() -> {
				// slider.setMin(0);
				mv.setPreserveRatio(true);
				DoubleProperty mvw = mv.fitWidthProperty();
				DoubleProperty mvh = mv.fitHeightProperty();
				DoubleProperty canvasX = canvas.layoutXProperty();
				DoubleProperty canvasW = canvas.widthProperty();
				DoubleProperty canvasH = canvas.heightProperty();

				mvw.bind(Bindings.selectDouble(mv.parentProperty(), "width"));
				mvh.bind(Bindings.selectDouble(mv.parentProperty(), "height"));
				canvasW.bind(Bindings.selectDouble(mv.boundsInParentProperty(), "width"));
				canvasH.bind(Bindings.selectDouble(mv.boundsInParentProperty(), "height"));

				slider.setMinorTickCount(0);
				slider.setMajorTickUnit(1000);
				slider.setMax(player.getTotalDuration().toMillis());
				mediaLength = player.getTotalDuration().toMillis();

				//Fix for changing the dimensions in the scaling manager when a video is opened
				scalingManager.setMediaDimension(player.getMedia().getWidth(), player.getMedia().getHeight());
				scalingManager.setCanvasDimension(canvas.getWidth(), canvas.getHeight());
			});
			mv.setMediaPlayer(player);
			player.setMute(true);

			settingsController.reset();
			//TODO: Wieso steht hier nochmal reset?
			reset();
			startBtn.setDisable(false);
		} else {
			UIUtils.showErrorAlert("Fehler beim Öffnen der Datei!");
		}

	}

	public void openVideoDialog() {
		PropertiesReader propReader = PropertiesReader.getInstance();
		PropertiesWriter propWriter = new PropertiesWriter();
		FileChooser fileChooser = new FileChooser();

		if (!propReader.getLastPath().isEmpty()) {
			if (new File(propReader.getLastPath()).isDirectory()) {
				fileChooser.setInitialDirectory(new File(propReader.getLastPath()));
			} else {
				propWriter.setLastPath("");
			}
		}

		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
				"Video Dateien (*.fxm), (*.flv), (*.mp4), (*.m4v)", "*.fxm", "*.flv", "*.mp4", "*.m4v");
		fileChooser.getExtensionFilters().add(filter);
		File mediaFile = fileChooser.showOpenDialog(MainFX.getStage());

		if (mediaFile != null) {
			//-----------------------------------------------------------------------------
			openMedia(mediaFile);
			//-----------------------------------------------------------------------------
			System.out.println(mediaFile.getAbsolutePath());
			propWriter.setLastPath(mediaFile.getParent());
			propWriter.confirm();
			de.nuton.savingFile.TempSaving.setURL(mediaFile.getAbsolutePath());
			de.nuton.savingFile.TempSaving.withFfmpeg(false);
		}
	}

	public boolean loadVideoWithFfmpeg(File video) {
		PropertiesReader propReader = PropertiesReader.getInstance();
		PropertiesWriter propWriter = new PropertiesWriter();
		Alert waitAlert = new Alert(Alert.AlertType.NONE);
		waitAlert.setHeaderText("Ffmpeg verabeitet das Video.");
		waitAlert.setContentText("Das Video wird importiert.");
		waitAlert.initStyle(StageStyle.TRANSPARENT);
		String videoPath = "";
		if (video != null) {
			videoPath = video.getAbsolutePath();
			String outputPath = null;
			if (propReader.isFfmpegOutputSame()) {
				outputPath = propReader.getFfmpegSameOutputPath();
			}
			String name = "output";

			FfmpegHandler handler = new FfmpegHandler(videoPath, outputPath, name);

			if (!handler.getffmpegStarted()) {
				System.out.println("ffmpeg nicht gefunden");
				return false;
			}

			if (!handler.getVideo().exists()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Video nicht gefunden");
				alert.setHeaderText("Ein Fehler ist aufgetreten!");
				alert.setContentText(
						"Das Video konnte nicht gefunden werden. Überprüfen Sie die Log-Datei für weitere Informationen.");

				alert.showAndWait();
				return false;
			} else {
				propWriter.setLastPath(video.getParent());
				propWriter.confirm();
				openMedia(handler.getVideo());
				de.nuton.savingFile.TempSaving.setURL(video.getAbsolutePath());
				de.nuton.savingFile.TempSaving.withFfmpeg(true);

			}
		}
		waitAlert.close();
		waitAlert.hide();
		return true;
	}

	public void openVideoWithFfmpegDialog() {
		PropertiesReader propReader = PropertiesReader.getInstance();
		PropertiesWriter propWriter = new PropertiesWriter();
		FileChooser chooser = new FileChooser();

		if (!propReader.getLastPath().isEmpty()) {
			if (new File(propReader.getLastPath()).exists()) {
				chooser.setInitialDirectory(new File(propReader.getLastPath()));
			} else {
				propWriter.setLastPath("");
			}
		}

		File video;
		video = chooser.showOpenDialog(MainFX.getStage());

		loadVideoWithFfmpeg(video);
	}

	public void openAboutDialog() {
		try {
			Parent loader = FXMLLoader.load(getClass().getResource("/fxml/About.fxml"));
			Scene scene = new Scene(loader);
			Stage stage = new Stage();
			stage.setTitle("Über Nuton");
			stage.setScene(scene);
			stage.getIcons().add(new Image(MainController.class.getResourceAsStream("/nutonLogo.png")));
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public void closeProgram() {
		Platform.exit();
		System.exit(0);
	}

	//TODO: besserer Name
	public void restart() {
		startBtn.setDisable(false);
		stateManager.getCurrentState().reset();
		stateManager.setState(StateManager.DEFAULT);
		de.nuton.toolBarEvents.AddPointEvents.reset();
		updateLists();
	}

	public void backwardButton() {
		ArrayList<Point> points = stateManager.getPoints();
		double schrittweite = settings.getSchrittweite();
		if (points != null) {
			double time = 0;
			if (points.size() - 1 > 0) {
				time = points.get(points.size() - 1).getTime();
			}

			if (time == slider.getValue() - schrittweite
					&& slider.getValue() - schrittweite >= 0) {
				slider.setValue(slider.getValue() - schrittweite);
			} else if (slider.getValue() - schrittweite >= 0) {
				slider.setValue(slider.getValue() - schrittweite);
			}
		} else if (slider.getValue() - schrittweite >= 0) {
			slider.setValue(slider.getValue() - schrittweite);
		}
	}

	
/*	public void clearCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}*/
	
	public Media getMedia() {
		return media;
	}


	public void setMedia(Media media) {
		this.media = media;
	}


	public MediaPlayer getPlayer() {
		return player;
	}


	public void setPlayer(MediaPlayer player) {
		this.player = player;
	}


	public MediaView getMv() {
		return mv;
	}


	public void setMv(MediaView mv) {
		this.mv = mv;
	}


	public SettingsController getSettingsController() {
		return settingsController;
	}


	public void setSettingsController(SettingsController settingsController) {
		this.settingsController = settingsController;
	}
	
	public Slider getSlider() {
		return slider;
	}


	public void setSlider(Slider slider) {
		this.slider = slider;
	}


	public Button getStartBtn() {
		return startBtn;
	}


	public void setStartBtn(Button testBtn) {
		this.startBtn = testBtn;
	}


	public Button getRestartBtn() {
		return restartBtn;
	}


	public void setRestartBtn(Button restartBtn) {
		this.restartBtn = restartBtn;
	}


	public Button getFertigBtn() {
		return fertigBtn;
	}


	public void setFertigBtn(Button fertigBtn) {
		this.fertigBtn = fertigBtn;
	}
	

	public double getMediaLength() {
		return mediaLength;
	}


	public void setMediaLength(double mediaLength) {
		this.mediaLength = mediaLength;
	}


	public Canvas getCanvas() {
		return canvas;
	}
	
	public Canvas getStreamCanvas() {
		return streamCanvas;
	}


	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	public StateManager getStateManager() {
		return stateManager;
	}


	public PixelManager getPManager() {
		return pManager;
	}

	@Deprecated
	public GraphicsContext getGc() {
		return this.canvas.getGraphicsContext2D();
	}

	public ToolBarManager getToolBarManager() {
		return tbm;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	public MenuItem getSaveFileMenu() {
		return saveFileMenu;
	}
	
	public StackPane getStackPane() {
		return stackPane;
	}
	
	// public ScalingManager getScalingManager() {
	// 	return scalingManager;
	// }
	
	public void setThemeLoader(ThemeLoader themeLoader) {
		this.themeLoader = themeLoader;
	}
	
	public ThemeLoader getThemeLoader() {
		return themeLoader;
	}
	
	public HostServices getHostServices() {
		return hostServices;
	}
	
	public void setHelpLabel(String text) {
		helpLabel.setText(text);
	}

	public TrackingSettingsController getTrackingController() {
		return trackingController;
	}

}
