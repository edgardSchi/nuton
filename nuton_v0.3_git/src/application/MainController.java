package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import states.StateManager;
import toolBar.ToolBarManager;
import userSettings.ProgramSettingsController;
import waveGenerator.WaveGeneratorController;

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
	@FXML private MenuItem simWaveMenu;
	@FXML private Canvas canvas; 
	@FXML public Slider slider;
	@FXML public Button startBtn;
	@FXML public Button restartBtn;
	@FXML public Button fertigBtn;
	@FXML private Button saveSettingsBtn;
	@FXML private ListView<Integer> listX;
	@FXML private ListView<Integer> listY;
	@FXML private ToolBar toolBar;
	private GraphicsContext gc;
	private ToolBarManager tbm;
	//private ArrayList<Point> points;
	
	private double SCHRITTWEITE = 1000;
	private double EICHUNG = 100;
	private double LAENGEPIXEL = 0;
	private double mediaLength = 0;
	
	private PixelManager pManager;
	private MainEventHandler eventHandler;
	private StateManager stateManager;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		eventHandler = new MainEventHandler(this);
		setSettingsController(new SettingsController(this));
		
		//points = new ArrayList<Point>();
		pManager = new PixelManager(getSettingsController());
		

		gc = canvas.getGraphicsContext2D();
		
		
		openFileMenu.setOnAction(eventHandler.openFileDialog());
		
		menuAnleitung.setOnAction(eventHandler.openAnleitungDialog());
		
		ffmpegMenu.setOnAction(eventHandler.ffmpegOpenFileDialog());
		
		closeMenu.setOnAction(eventHandler.closeProgram());
		
		tbm = new ToolBarManager(toolBar, this);
//		ForwardButton fButton = new ForwardButton();
//		TestToolBarItem item = new TestToolBarItem();
//		toolBarM.addItem(item);
//		toolBarM.addItem(fButton);
		stateManager = new StateManager(this);
		
		einstellungenItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				@SuppressWarnings("unused")
				ProgramSettingsController pSettings = new ProgramSettingsController();
			}
			
		});
		
		simWaveMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				WaveGeneratorController wave = new WaveGeneratorController();
			}
			
		});
		
		mv.setPreserveRatio(false);

		
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
		
		
		startBtn.setOnAction(eventHandler.openSettings());
		
		
		
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
		
	}
	
	
	void reset() {
		//points.clear();
		SCHRITTWEITE = 1000;
		EICHUNG = 100;
		LAENGEPIXEL = 0;
		listX.getItems().clear();
		listY.getItems().clear();
		slider.setValue(0);
		slider.setDisable(false);
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getWidth());
		startBtn.setDisable(false);
		pManager.reset();
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


	public double getSCHRITTWEITE() {
		return SCHRITTWEITE;
	}


	public void setSCHRITTWEITE(double sCHRITTWEITE) {
		this.SCHRITTWEITE = sCHRITTWEITE;
	}


	public double getEICHUNG() {
		return EICHUNG;
	}


	public void setEICHUNG(double eICHUNG) {
		EICHUNG = eICHUNG;
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
}
