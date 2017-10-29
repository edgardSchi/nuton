package toolBar;

import application.MainController;
import application.MainEventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class ForwardButton extends ToolBarButton {

	private MainController controller;
	
	public ForwardButton(MainEventHandler eventHandler) {
		this.controller = eventHandler.getMainController();
		setIcon(new Image("file:icons/forwardIcon.png"));
		button.setTooltip(new Tooltip("Vorwärts"));
	}
	
	@Override
	public void onClick() {
		double schrittweite = controller.getSCHRITTWEITE();
		controller.getSlider().setValue(controller.getSlider().getValue() + schrittweite);
	}

}
