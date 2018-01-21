package states;

import java.util.ArrayList;
import java.util.Optional;

import application.MainController;
import application.PixelManager;
import application.Point;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import settings.Settings;

public class PostCalibrationState extends State{
	
	private MainController mainController;
	private ArrayList<Point> points;
	private Settings settings;
	private double sliderPos = 0;
	
	private PixelManager pManager;
	private GraphicsContext gc;
	private int clickCounter = 0;
	private double x1;
	private double x2;
	private double y1;
	private double y2;
	
	public PostCalibrationState(MainController mainController, PixelManager pManager) {
		this.mainController = mainController;
		points = new ArrayList<Point>();
		this.pManager = pManager;
		this.gc = mainController.getGc();
		settings = mainController.getSettings();
	}
	
	@Override
	public void init() {
		mainController.getFertigBtn().setDisable(true);
		points = mainController.getStateManager().getPoints();
		sliderPos = mainController.getSlider().getValue();
	}

	@Override
	public void onClick(MouseEvent e) {
		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
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
				
				TextInputDialog dialog = new TextInputDialog("" + (int)mainController.getSettings().getEichung());
				Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(getClass().getResourceAsStream("/nutonLogo.png")));
				dialog.setTitle("Bestätigen");
				dialog.setHeaderText(null);
				dialog.setContentText("Distanz für folgenen Wert speichern? (cm):");
				
				dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						int i = 1;
						if (!newValue.matches("\\d*")) {
							dialog.getEditor().setText(newValue.replaceAll("[^\\d]", "1"));
						} else {
							if (!newValue.isEmpty()) {
								i = Integer.parseInt(newValue);
							}						
						}
										
						if (i > 1000) {
							dialog.getEditor().setText(newValue.replaceAll(newValue, "1000"));
						}
						
//						if (i < 10) {
//							eichungsField.setText(newValue.replaceAll(newValue, "10"));
//						}
						
						if (newValue.isEmpty()) {
							dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
						} else {
							dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
						}
					}
					
				});
				
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					pManager.setLaengePixel(pManager.getDistance(x1, y1, x2, y2));		
				
					mainController.getSettings().setEichung(Double.parseDouble(result.get()));
					pManager.setEichung(Double.parseDouble(result.get()));
					gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
					clickCounter = 0;
					mainController.getStateManager().setState(StateManager.TRANSLATION);
					mainController.getStateManager().getCurrentState().setPoints(points);
					mainController.getSlider().setValue(sliderPos);
					mainController.setSettings(settings);
					mainController.redraw();
					mainController.getSlider().setSnapToTicks(true);
				} else {
					gc.clearRect(0, 0,mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
					x1 = 0;
					y1 = 0;
					x2 = 0;
					y2 = 0;
					clickCounter = 0;
				}
			}
		}
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fertigBtnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Point> getPoints() {
		return points;
	}

	@Override
	public MainController getMainController() {
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Point> setPoints(ArrayList<Point> points) {
		// TODO Auto-generated method stub
		return null;
	}

}
