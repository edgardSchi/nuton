/*******************************************************************************
 * Nuton
 * Copyright (C) 2018 Edgard Schiebelbein
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import application.settingsPane.SettingsController;
import camera.CameraController;
import javafx.application.HostServices;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import savingFile.LoadHandler;
import savingFile.SaveHandler;
import settings.Settings;
import states.StateManager;
import toolBar.ToolBarManager;
import tracking.TrackingSettingsController;
import userSettings.ProgramSettingsController;
import userSettings.ThemeLoader;

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
	
	@FXML private MenuItem startCameraMenu;
	@FXML private MenuItem stopCameraMenu;
	
	private GraphicsContext gc;
	private ToolBarManager tbm;
	private Stage mainStage;
	
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
		
		themeLoader = new ThemeLoader();
		scalingManager = new ScalingManager(this);
		settings = new Settings();
		eventHandler = new MainEventHandler(this);
		setSettingsController(new SettingsController(this, settings, themeLoader));
		trackingController = new TrackingSettingsController(this, themeLoader);
		
		pManager = new PixelManager(this);
		
		hostServices = (HostServices)Main.getStage().getProperties().get("hostServices");

		gc = canvas.getGraphicsContext2D();
		
		pSettings = new ProgramSettingsController(this, themeLoader);
		
		


		
		//camera.prepareCanvas(middleAnchorPane);
		
		startCameraMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				stateManager.setState(StateManager.STREAMING);
			}
			
		});
		
		stopCameraMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
//				canvasW.unbind();
//				canvasH.unbind();
				stateManager.setState(StateManager.DEFAULT);
			}
			
		});
		
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
				eventHandler.closeProgram();
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
				File file = chooser.showOpenDialog(Main.getStage());
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
				scalingManager.setCanvasDimension();
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
				UpdateChecker checker = new UpdateChecker("https://edgardschi.github.io/nuton-website/version.html", Main.getVersion());
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
	
	public void redraw() {
		stateManager.redraw();
	}
	
	public void reset() {
		settings.setSchrittweite(1000);
		settings.setEichung(100);
		slider.setValue(0);
		slider.setDisable(false);
		slider.setSnapToTicks(false);
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getWidth());
		startBtn.setDisable(false);
		pManager.reset();
		stateManager.setState(StateManager.DEFAULT);
	}
	
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
	
	public void updateLists() {
		tableView.getItems().clear();
		for(Point p : stateManager.getPoints()) {
			tableView.getItems().add(p);
		}
	}
	
	public void setMainStage(Stage stage) {
		
//		
//		stage.getScene().widthProperty().addListener(new ChangeListener<Number>() {
//			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//				System.out.println("Width: " + newSceneWidth);
//			}
//		});
	}
	
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
		return gc;
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
	
	public ScalingManager getScalingManager() {
		return scalingManager;
	}
	
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
