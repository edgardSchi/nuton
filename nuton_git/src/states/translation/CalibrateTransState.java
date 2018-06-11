package states.translation;

import java.util.Optional;

import application.MainController;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import states.CalibrateState;
import states.StateManager;

public class CalibrateTransState extends CalibrateState { 
	
	public CalibrateTransState(MainController mainController) {
		super(mainController);
	}
	
	@Override
	public void init() {
		resetSlider();
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
				
				TextInputDialog dialog = createDialog("Distanz für folgenen Wert speichern? (cm):", 1, 999999);
				
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){	
					pManager.setCalibratePoints(calibratePoints);
					mainController.getSettings().setEichung(Double.parseDouble(result.get()));
					pManager.setEichung(Double.parseDouble(result.get()));
					resetCalibrate();
					mainController.getStateManager().setState(StateManager.TRANSLATION);
					mainController.getScalingManager().normalizePoint(calibratePoints[0]);
					mainController.getScalingManager().normalizePoint(calibratePoints[1]);
					mainController.getStateManager().getCurrentState().setCalibratePoints(calibratePoints);
					mainController.getSlider().setSnapToTicks(true);
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
		
	}


	
}
