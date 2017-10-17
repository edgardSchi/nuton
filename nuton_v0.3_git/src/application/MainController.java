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
import javafx.scene.paint.Color;
import javafx.util.Duration;
import toolBar.ToolBarManager;
import userSettings.ProgramSettingsController;

public class MainController implements Initializable{
	
	@FXML private MediaView mv;
	private static MediaPlayer player;
	private Media media;
	private static SettingsController settingsController;
	@FXML private MenuItem openFileMenu;
	@FXML private MenuItem einstellungenItem;
	@FXML private MenuItem ffmpegMenu;
	@FXML private MenuItem closeMenu;
	@FXML private MenuItem menuAnleitung;
	@FXML private Canvas canvas; 
	@FXML public Slider slider;
	@FXML public Button startBtn;
	@FXML public Button restartBtn;
	@FXML public Button fertigBtn;
	private boolean readyToEich;
	private boolean readyToSetPoints;
	private boolean fertig;
	@FXML private Button saveSettingsBtn;
	@FXML private ListView listX;
	@FXML private ListView listY;
	@FXML private ToolBar toolBar;
	private GraphicsContext gc;
	private ArrayList<Point> points;
	private DiagramsController diaController;
	
	public static double SCHRITTWEITE = 1000;
	private static double EICHUNG = 100;
	private double LAENGEPIXEL = 0;
	private static double mediaLength = 0;



	private int initPoints = 0;
	private double yFix = 0;
	private double xFix = 0;
	
	private Scene rootScene;
	private static PixelManager pManager;
	private MainEventHandler eventHandler;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
//		String path = new File("src/media/Cs(03).mp4").getAbsolutePath();
//		media = new Media(new File(path).toURI().toString());		
//		player = new MediaPlayer(media);
//		player.setAutoPlay(true);
//		player.setMute(true);
//		mv.setMediaPlayer(player);
		
		eventHandler = new MainEventHandler(this);
		setSettingsController(new SettingsController(this));
		
		points = new ArrayList<Point>();
		pManager = new PixelManager(getSettingsController());
		
		diaController = new DiagramsController(pManager);

		gc = canvas.getGraphicsContext2D();
		
		readyToEich = false;
		readyToSetPoints = false;
		fertig = false;
		
		openFileMenu.setOnAction(eventHandler.openFileDialog());
		
		menuAnleitung.setOnAction(eventHandler.openAnleitungDialog());
		
		ffmpegMenu.setOnAction(eventHandler.ffmpegOpenFileDialog());
		
		closeMenu.setOnAction(eventHandler.closeProgram());
		
		ToolBarManager toolBarM = new ToolBarManager(toolBar, this);
//		ForwardButton fButton = new ForwardButton();
//		TestToolBarItem item = new TestToolBarItem();
//		toolBarM.addItem(item);
//		toolBarM.addItem(fButton);
		
		einstellungenItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ProgramSettingsController pSettings = new ProgramSettingsController();
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
		
		
		
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			int clickCounter = 0;
			double x1 = 0;
			double y1 = 0;
			double x2 = 0;
			double y2 = 0;		
			
			
			@Override
			public void handle(MouseEvent e) {
				if (readyToEich == true && clickCounter < 2 && fertig == false) {
					gc.setFill(Color.rgb(255, 119, 0, 0.80));
					gc.fillRect(e.getX() - 10, e.getY() - 10, 20, 20);
					
					if (clickCounter == 0) {
						x1 = e.getX();
						y1 = e.getY();
					}
					
					if (clickCounter == 1) {
						x2 = e.getX();
						y2 = e.getY();
					}
					
					System.out.println("X1: " + x1);
					System.out.println("Y1: " + y1);
					System.out.println("X2: " + x2);
					System.out.println("Y2: " + y2);
					
					clickCounter++;
					
					if (clickCounter == 2) {
						gc.setStroke(Color.RED);
						gc.strokeLine(x1, y1, x2, y2);
						
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Bestätigen");
						alert.setHeaderText("Diese Distanz speichern?");
						alert.showAndWait().ifPresent(rs -> {
							if (rs == ButtonType.OK) {
								LAENGEPIXEL = pManager.getDistance(x1, y1, x2, y2);
								pManager.setLaengePixel(LAENGEPIXEL);
								System.out.println(LAENGEPIXEL);			
								pManager.setEichung(EICHUNG);
								readyToEich = false;
								gc.clearRect(0, 0, canvas.getWidth(), canvas.getWidth());
								readyToSetPoints = true;
								clickCounter = 0;
							} 
							if (rs == ButtonType.CANCEL) {
								gc.clearRect(0, 0, canvas.getWidth(), canvas.getWidth());
								x1 = 0;
								y1 = 0;
								x2 = 0;
								y2 = 0;
								clickCounter = 0;
							}
						});
					}
				}
				
				if (readyToSetPoints == true && fertig == false) {
					if (initPoints == 1) {
						gc.setFill(Color.rgb(255, 119, 0, 0.80));						
						if (getSettingsController().yFixed() == true && yFix == 0) {
							//gc.fillRect(e.getX() - 10, e.getY() - 10, 20, 20);
							yFix = e.getY();
							Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue(), points.size());
							points.add(p);
							p.drawPoint(gc);
							listX.getItems().add(p.getX());
							listY.getItems().add(p.getY());
							System.out.println(p.getId());
						} else if (getSettingsController().yFixed() == true) {
							Point p = new Point((int)e.getX(), (int)yFix, slider.getValue(), points.size());
							points.add(p);
							p.drawPoint(gc);
							listX.getItems().add(p.getX());
							listY.getItems().add(p.getY());
						} else if (getSettingsController().xFixed() == true && xFix == 0) {
							xFix = e.getX();
							Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue(), points.size());
							points.add(p);
							p.drawPoint(gc);
							listX.getItems().add(p.getX());
							listY.getItems().add(p.getY());
						} else if (getSettingsController().xFixed() == true) {
							Point p = new Point((int)xFix, (int)e.getY(), slider.getValue(), points.size());
							points.add(p);
							p.drawPoint(gc);
							listX.getItems().add(p.getX());
							listY.getItems().add(p.getY());
						} else {
							Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue(), points.size());
							points.add(p);
							p.drawPoint(gc);
							listX.getItems().add(p.getX());
							listY.getItems().add(p.getY());
						}
						slider.setValue(slider.getValue() + SCHRITTWEITE);
						
						
					} else if (initPoints == 0) {
						slider.setValue(0);
						slider.setDisable(true);
						initPoints = 1;
					}
					
				}
				
			}
		});
		
		restartBtn.setOnAction(eventHandler.reset());
		
		
		
		fertigBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (points.size() < 3) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Bitte wählen Sie mindestens zwei Punke aus.");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							alert.close();
						}
					});
				} else {
					fertig = true;
					pManager.setPoints(points);
					pManager.calcDistances();
					pManager.calcMeter(points);
					diaController.setPoints(points);
					diaController.makeDiagram();
					diaController.show();
				}
			}
			
		});
		
	}
	
	
	public void eichungsReady(boolean s) {
		readyToEich = s;
	}
	
	void reset() {
		points.clear();
		SCHRITTWEITE = 1000;
		EICHUNG = 100;
		LAENGEPIXEL = 0;
		readyToEich = false;
		readyToSetPoints = false;
		listX.getItems().clear();
		listY.getItems().clear();
		slider.setValue(0);
		slider.setDisable(false);
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getWidth());
		startBtn.setDisable(false);
		initPoints = 0;
		yFix = 0;
		xFix = 0;
		pManager.reset();
		diaController.reset();
		fertig = false;
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
		MainController.player = player;
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


	public static void setSettingsController(SettingsController settingsController) {
		MainController.settingsController = settingsController;
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


	public static void setSCHRITTWEITE(double sCHRITTWEITE) {
		SCHRITTWEITE = sCHRITTWEITE;
	}


	public static double getEICHUNG() {
		return EICHUNG;
	}


	public static void setEICHUNG(double eICHUNG) {
		EICHUNG = eICHUNG;
	}


	public static double getMediaLength() {
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


	public ArrayList<Point> getPoints() {
		return points;
	}
	
	
	
	

//	public void resizeMedia() {
//		centerPane.widthProperty().addListener(new ChangeListener<Number>() {
//		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//		        System.out.println("Width: " + centerPane.getWidth());
//		        double oldWidth = mv.getFitWidth();
//		        if (newSceneWidth.doubleValue() > oldSceneWidth.doubleValue()) {
//		        	//mv.fitWidthProperty().set(oldWidth + (newSceneWidth.doubleValue() - oldSceneWidth.doubleValue()));
//		        }		        
//		    }
//		});
////		centerPane.heightProperty().addListener(new ChangeListener<Number>() {
////		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
////		        System.out.println("Height: " + newSceneHeight);
////		    }
////		});
//	}
	
}
