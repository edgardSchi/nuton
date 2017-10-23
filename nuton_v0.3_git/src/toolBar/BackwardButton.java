package toolBar;

import javafx.scene.image.Image;

public class BackwardButton extends ToolBarItem {

	
	public BackwardButton(ToolBarManager tbm) {
		this.tbm = tbm;
		setIcon(new Image("file:icons/backwardIcon.png"));
		//points = controller.getPoints();
	}
	
	@Override
	public void onClick() {
		tbm.getEventHandler().backwardButton();
//		controller.getCanvas().
	}


}
