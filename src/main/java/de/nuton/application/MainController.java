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
import java.net.URL;
import java.util.ResourceBundle;

import de.nuton.application.settingsPane.SettingsController;
import de.nuton.draw.DrawController;
import de.nuton.draw.DrawHandler;
import de.nuton.math.UnitsHandler.LengthUnit;
import de.nuton.math.UnitsHandler.TimeUnit;
import de.nuton.savingFile.LoadHandler;
import de.nuton.savingFile.SaveHandler;
import de.nuton.settings.Settings;
import de.nuton.states.StateManager;
import de.nuton.toolBar.ToolBarManager;
import de.nuton.tracking.TrackingSettingsController;
import de.nuton.userSettings.ProgramSettingsController;
import de.nuton.userSettings.ThemeLoader;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
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
	
	//private GraphicsContext gc;
	private ToolBarManager tbm;
	
	private double mediaLength = 0;
	
	private ThemeLoader themeLoader;
	private ProgramSettingsController pSettings;
	private PixelManager pManager;
	private MainEventHandler eventHandler;
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
		eventHandler = new MainEventHandler(this);
		DrawController.init(this);
		
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
		
		openFileMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				eventHandler.openFileDialog();
			}
			
		});
		
		ffmpegMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				eventHandler.ffmpegOpenFileDialog();
			}
			
		});
		
		closeMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				MainEventHandler.closeProgram();
			}
			
		});
		
		
		tbm = new ToolBarManager(toolBar, this);
		initMenuItems();

		stateManager = new StateManager(this);
		SaveHandler saveHandler = new SaveHandler(this);
		loadHandler = new LoadHandler(this, settings);
		
		einstellungenItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				pSettings.showDialog();
			}
			
		});
		
		loadProjectMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				FileChooser chooser = new FileChooser();
				FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Nuton Datein (*.ntn)", "*.ntn");
				chooser.getExtensionFilters().add(filter);
				File file = chooser.showOpenDialog(MainFX.getStage());
				loadHandler.load(file);
			}
			
		});
		
		saveFileMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (player != null && player.getMedia() != null) {
					saveHandler.save();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error!");
					alert.setContentText("Es ist nichts zum speichern vorhanden.");
					alert.setHeaderText(null);
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							alert.close();
						}
					});
				}		
			}
			
		});
		
		saveFileAsMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (player.getMedia() != null) {
					saveHandler.setSaveAs(true);
					saveHandler.save();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error!");
					alert.setContentText("Es ist nichts zum speichern vorhanden.");
					alert.setHeaderText(null);
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							alert.close();
						}
					});
				}
			}
			
		});

		
		if (getPlayer() != null) {
			getPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {

				@Override
				public void changed(ObservableValue<? extends Duration> arg0, Duration arg1, Duration arg2) {
					slider.setValue(getPlayer().getCurrentTime().toSeconds());				
				}
				
			});
		}


		slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (getPlayer() != null) {
					getPlayer().seek(Duration.millis((double)newValue));
				}			
				
			}
			
		});
		
		//Verbessern
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (mv.getMediaPlayer() == null || mv.getMediaPlayer().getMedia() == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Videodatei auswählen!");
					alert.setContentText("Wählen Sie zuerst eine geeignete Videodatei aus.");
					alert.setHeaderText(null);
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							alert.close();
						}
					});
//				} else if (camera.isRunning()) {
//					getStartBtn().setDisable(true);
//					getSettingsController().showDialog();
				} else {
					getStartBtn().setDisable(true);
					getSettingsController().showDialog();
				}		
			}
			
		});
		
		
		
		canvas.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {	
			
			
			@Override
			public void handle(MouseEvent e) {
				
				stateManager.onClick(e);
				
			}
		});
		
		restartBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				eventHandler.reset();
			}
			
		});
		
		
		fertigBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				stateManager.fertigBtnClick();
			}

		});
		
		canvas.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				scalingManager.setCanvasDimension(getCanvas().getWidth(), getCanvas().getHeight());
				if (stateManager.getCurrentState().getPoints() != null) {
					for (Point p : stateManager.getCurrentState().getPoints()) {
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
					}
					redraw();
				}
			}
		});
		
		initTableView();
		
		
		startTrackingMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				trackingController.showDialog();
			}
			
		});
		
		menuOpenWiki.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				getHostServices().showDocument("https://github.com/edgardSchi/nuton/wiki");
			}
			
		});
		
		menuUpdates.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				UpdateChecker checker = new UpdateChecker("https://edgardschi.github.io/nuton-website/version.html", MainFX.getVersion());
				if(checker.readData()) {
					checker.checkVersion();
				} else {
					checker.errorDialog();
				}
			}
			
		});
		
		menuAbout.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				eventHandler.openAboutDialog();
			}
			
		});
		
		

		
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
		DrawController.getInstance().clearScreen();
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
		timeCol.setCellValueFactory(new PropertyValueFactory<Point, Integer>("time"));
		timeCol.setResizable(false);
		timeCol.setPrefWidth(53);
		timeCol.setSortable(false);
		TableColumn<Point, Integer> xCol = new TableColumn<>("X[px]");
		xCol.setCellValueFactory(new PropertyValueFactory<Point, Integer>("x"));
		xCol.setResizable(false);
		xCol.setPrefWidth(54);
		xCol.setSortable(false);
		TableColumn<Point, Integer> yCol = new TableColumn<>("Y[px]");
		yCol.setCellValueFactory(new PropertyValueFactory<Point, Integer>("y"));
		yCol.setResizable(false);
		yCol.setPrefWidth(54);
		yCol.setSortable(false);
		
		tableView.getColumns().addAll(timeCol, xCol, yCol);
		
		tableView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() != -1) {
					double t = stateManager.getPoints().get(newValue.intValue()).getTime();
					slider.setValue(t);
				}
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
	
	public MainEventHandler getMainEventHandler() {
		return eventHandler;
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
