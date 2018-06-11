package toolBar;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class BackwardButton extends ToolBarButton {
	
	public BackwardButton(ToolBarManager tbm) {
		this.tbm = tbm;
		setIcon(new Image(getClass().getResourceAsStream("/backwardIcon.png")));
		button.setTooltip(new Tooltip("Eine Schrittweite zurück"));
	}
	
	@Override
	public void onClick() {
		tbm.getEventHandler().backwardButton();
	}



}
