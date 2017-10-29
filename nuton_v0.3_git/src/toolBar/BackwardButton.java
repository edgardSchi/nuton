package toolBar;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class BackwardButton extends ToolBarButton {
	
	public BackwardButton(ToolBarManager tbm) {
		this.tbm = tbm;
		setIcon(new Image("file:icons/backwardIcon.png"));
		//points = controller.getPoints();
		button.setTooltip(new Tooltip("Zurück"));
	}
	
	@Override
	public void onClick() {
		tbm.getEventHandler().backwardButton();
//		controller.getCanvas().
	}



}
