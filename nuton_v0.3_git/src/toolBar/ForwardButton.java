package toolBar;

import application.MainController;
import application.MainEventHandler;
import javafx.scene.image.Image;

public class ForwardButton extends ToolBarItem {

	private MainController controller;
	
	public ForwardButton(MainEventHandler eventHandler) {
		this.controller = eventHandler.getMainController();
		setIcon(new Image("file:icons/forwardIcon.png"));
	}
	
	@Override
	public void onClick() {
		double schrittweite = controller.getSCHRITTWEITE();
		controller.getSlider().setValue(controller.getSlider().getValue() + schrittweite);
	}

}
