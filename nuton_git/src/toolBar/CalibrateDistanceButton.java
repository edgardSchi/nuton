package toolBar;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import states.StateManager;

public class CalibrateDistanceButton extends ToolBarButton{
	
	public CalibrateDistanceButton(ToolBarManager tbm) {
		this.tbm = tbm;
		setIcon(new Image(getClass().getResourceAsStream("/calibrateIcon.png")));
		button.setTooltip(new Tooltip("Distanz neu kalibrieren"));
	}
	
	@Override
	public void onClick() {
		if (tbm.getMainController().getStateManager().getCurrentStateID() == StateManager.TRANSLATION) {
			tbm.getMainController().getStateManager().setState(StateManager.TRANSLATION_POSTCALIBRATION);
		}
		if (tbm.getMainController().getStateManager().getCurrentStateID() == StateManager.CIRCULAR) {
			tbm.getMainController().getStateManager().setState(StateManager.CIRCULAR_POSTCALIBRATION);
		}
	}

}
