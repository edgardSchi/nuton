package toolBar;

import application.MainController;
import application.MainEventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class ForwardButton extends ToolBarButton {

	private MainController controller;
	
	public ForwardButton(MainEventHandler eventHandler) {
		this.controller = eventHandler.getMainController();
		setIcon(new Image(getClass().getResourceAsStream("/forwardIcon.png")));
		button.setTooltip(new Tooltip("Eine Schrittweite vorwärts"));
	}
	
	@Override
	public void onClick() {
		double schrittweite = controller.getSettings().getSchrittweite();
		controller.getSlider().setValue(controller.getSlider().getValue() + schrittweite);
	}

}
