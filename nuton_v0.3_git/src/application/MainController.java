package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.scene.web.Debugger;

import application.settingsPane.SettingsController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
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
import javafx.util.Duration;
import savingFile.LoadHandler;
import savingFile.SaveHandler;
import settings.Settings;
import states.StateManager;
import toolBar.ToolBarManager;
import userSettings.ProgramSettingsController;

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
	@FXML private Canvas canvas; 
	@FXML public Slider slider;
	@FXML public Button startBtn;
	@FXML public Button restartBtn;
	@FXML public Button fertigBtn;
	@FXML private Button saveSettingsBtn;
	@FXML private ListView<Integer> listX;
	@FXML private ListView<Integer> listY;
	@FXML private ToolBar toolBar;
	@FXML private StackPane stackPane;
	private GraphicsContext gc;
	private ToolBarManager tbm;
	private Stage mainStage;
	//private ArrayList<Point> points;
	
	private double mediaLength = 0;
	
	private ProgramSettingsController pSettings;
	private PixelManager pManager;
	private MainEventHandler eventHandler;
	private StateManager stateManager;
	private Settings settings;
	private LoadHandler loadHandler;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		settings = new Settings();
		eventHandler = new MainEventHandler(this);
		setSettingsController(new SettingsController(this, settings));
		
		//points = new ArrayList<Point>();
		pManager = new PixelManager(settings);
		

		gc = canvas.getGraphicsContext2D();
		
		pSettings = new ProgramSettingsController(this);
		
		openFileMenu.setOnAction(eventHandler.openFileDialog());
		
		menuAnleitung.setOnAction(eventHandler.openAnleitungDialog());
		
		ffmpegMenu.setOnAction(eventHandler.ffmpegOpenFileDialog());
		
		closeMenu.setOnAction(eventHandler.closeProgram());
		
		tbm = new ToolBarManager(toolBar, this);
		initMenuItems();
//		ForwardButton fButton = new ForwardButton();
//		TestToolBarItem item = new TestToolBarItem();
//		toolBarM.addItem(item);
//		toolBarM.addItem(fButton);
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
				File file = chooser.showOpenDialog(Main.getStage());
				loadHandler.loadFile(file);
			}
			
		});
		
		saveFileMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (player != null && player.getMedia() != null) {
					saveHandler.write();
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
					saveHandler.write();
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
		
		restartBtn.setOnAction(eventHandler.reset());
		
		
		fertigBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				stateManager.fertigBtnClick();
			}

		});
		
		listX.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() != -1) {
					double t = stateManager.getPoints().get(newValue.intValue()).getTime();
					slider.setValue(t);
				}
			}
		});
		
		listY.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() != -1) {
					double t = stateManager.getPoints().get(newValue.intValue()).getTime();
					slider.setValue(t);
				}
			}
		});
		
		canvas.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if (arg2.intValue()/arg1.intValue() > 1) {
					System.out.println("Ratio > 1");
				}
				double newX = 0;
				if (stateManager.getCurrentState().getPoints() != null) {
					for (Point p : stateManager.getCurrentState().getPoints()) {
						newX = (p.getX() / arg1.doubleValue()) * arg2.doubleValue();
						p.setX((int)newX);
//						if (p.getX() != (int)newX) {
//							System.out.println("X hat sich geändert!");
//						}
						
					}
					redraw();
				}
				
			}
		});
		
	}
	
	public void redraw() {
//		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//		if (stateManager.getCurrentState().getPoints() != null) {
//			for (Point p : stateManager.getCurrentState().getPoints()) {
//				if (p != null) {
//					p.updateColor();
//					p.drawPoint(gc);
//				}
//			}
//		}
		stateManager.redraw();
	}
	
	public void updateLists() {
		listX.getItems().clear();
		listY.getItems().clear();
		for (Point p : stateManager.getCurrentState().getPoints()) {
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
		}
	}
	
	public void reset() {
		//points.clear();
		settings.setSchrittweite(1000);
		settings.setEichung(100);
		listX.getItems().clear();
		listY.getItems().clear();
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


	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}


//	public ArrayList<Point> getPoints() {
//		return points;
//	}
	
	public StateManager getStateManager() {
		return stateManager;
	}


	public PixelManager getPManager() {
		return pManager;
	}


	public GraphicsContext getGc() {
		return gc;
	}


	public ListView<Integer> getListX() {
		return listX;
	}


	public ListView<Integer> getListY() {
		return listY;
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
}
