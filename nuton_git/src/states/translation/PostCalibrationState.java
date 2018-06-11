package states.translation;

import java.util.Optional;

import application.MainController;
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
import states.State;
import states.StateManager;

public class PostCalibrationState extends CalibrateState{
	
	private double sliderPos = 0;
	
	private GraphicsContext gc;
	
	public PostCalibrationState(MainController mainController) {
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
					pManager.setCalibratePoints(calibratePoints);
					mainController.getSettings().setEichung(Double.parseDouble(result.get()));
					pManager.setEichung(Double.parseDouble(result.get()));
					resetCalibrate();
					mainController.getStateManager().setState(StateManager.TRANSLATION);
					mainController.getStateManager().getCurrentState().setPoints(points);
					mainController.getScalingManager().normalizePoint(calibratePoints[0]);
					mainController.getScalingManager().normalizePoint(calibratePoints[1]);
					mainController.getStateManager().getCurrentState().setCalibratePoints(calibratePoints);
					mainController.getSlider().setSnapToTicks(true);
					mainController.getSlider().setValue(sliderPos);
					mainController.setSettings(settings);
					mainController.redraw();
					mainController.getSlider().setSnapToTicks(true);
				} else {
					gc.clearRect(0, 0,mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
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
		
	}


}
