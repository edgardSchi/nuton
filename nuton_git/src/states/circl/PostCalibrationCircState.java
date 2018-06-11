package states.circl;

import java.util.Optional;

import application.MainController;
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
import states.CalibrateState;
import states.StateManager;

public class PostCalibrationCircState extends CalibrateState{

	private double sliderPos = 0;
	
	private GraphicsContext gc;
	
	private Point origin;
	
	public PostCalibrationCircState(MainController mainController) {
		super(mainController);
		this.gc = mainController.getGc();
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
			gc.fillRect(e.getX() - 5, e.getY() - 5, 10, 10);
				
			addPointByMouse(e);
			
			if (clickCounter == 2) {
				gc.setStroke(Color.RED);
				gc.strokeLine(calibratePoints[0].getX(), calibratePoints[0].getY(), calibratePoints[1].getX(), calibratePoints[1].getY());	
				
				TextInputDialog dialog = new TextInputDialog("" + (int)mainController.getSettings().getEichung());
				Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(getClass().getResourceAsStream("/nutonLogo.png")));
				dialog.setTitle("Best�tigen");
				dialog.setHeaderText(null);
				dialog.setContentText("Mittelpunkt und Radius mit folgendem Wert speichern?:");
				
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
							dialog.getEditor().setText(newValue.replaceAll(newValue, "10000"));
						}
						
						if (newValue.isEmpty()) {
							dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
						} else {
							dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
						}
					}
					
				});
				
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){	
					origin = calibratePoints[0];
					mainController.getScalingManager().normalizePoint(origin);
					pManager.setCalibratePoints(calibratePoints);
					mainController.getSettings().setEichung(Double.parseDouble(result.get()));
					pManager.setEichung(Double.parseDouble(result.get()));
					pManager.setOrigin(origin);
					gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
					clickCounter = 0;
					mainController.getStateManager().setState(StateManager.CIRCULAR);
					mainController.getStateManager().getCurrentState().setPoints(points);
					mainController.getSlider().setValue(sliderPos);
					mainController.getSlider().setSnapToTicks(true);
					mainController.getStateManager().getCurrentState().setCalibratePoints(calibratePoints);
					mainController.setSettings(settings);
					mainController.redraw();
				} else {
					resetCalibrate();
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
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redraw() {
		// TODO Auto-generated method stub
		
	}}
