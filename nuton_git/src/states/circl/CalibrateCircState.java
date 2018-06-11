package states.circl;

import java.util.Optional;

import application.MainController;
import application.Point;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import states.CalibrateState;
import states.StateManager;

public class CalibrateCircState extends CalibrateState{

	private Point origin;
	
	public CalibrateCircState(MainController mainController) {
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
				
				TextInputDialog dialog = createDialog("Mittelpunkt und Radius mit folgendem Wert speichern?:", 1, 999999);
				
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					origin = calibratePoints[0];
					mainController.getScalingManager().normalizePoint(origin);
					mainController.getScalingManager().normalizePoint(calibratePoints[1]);
					pManager.setCalibratePoints(calibratePoints);
					mainController.getSettings().setEichung(Double.parseDouble(result.get()));
					pManager.setEichung(Double.parseDouble(result.get()));
					pManager.setOrigin(origin);
					gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
					clickCounter = 0;
					mainController.getStateManager().setState(StateManager.CIRCULAR);
					mainController.getSlider().setSnapToTicks(true);
					mainController.getStateManager().getCurrentState().setCalibratePoints(calibratePoints);
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
