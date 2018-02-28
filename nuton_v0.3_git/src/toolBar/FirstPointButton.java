package toolBar;

import application.MainController;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class FirstPointButton extends ToolBarButton {

	private MainController mainController;
	
	public FirstPointButton(ToolBarManager manager) {
		mainController = manager.getMainController();
		setIcon(new Image(getClass().getResourceAsStream("/firstPointIcon.png")));
		button.setTooltip(new Tooltip("Springe zum ersten Punkt"));
	}
	
	@Override
	public void onClick() {
		if (mainController.getStateManager().getPoints() != null) {
			double time = mainController.getStateManager().getPoints().get(0).getTime();
			mainController.getSlider().setValue(time);
		}		
	}

}
