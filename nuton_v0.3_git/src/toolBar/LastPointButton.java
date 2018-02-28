package toolBar;

import application.MainController;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class LastPointButton extends ToolBarButton {

	private MainController mainController;
	
	public LastPointButton(ToolBarManager manager) {
		mainController = manager.getMainController();
		setIcon(new Image(getClass().getResourceAsStream("/lastPointIcon.png")));
		button.setTooltip(new Tooltip("Springe zum letzten Punkt"));
	}
	
	@Override
	public void onClick() {
		if (mainController.getStateManager().getPoints() != null) {
			int index = mainController.getStateManager().getPoints().size();
			double time = mainController.getStateManager().getPoints().get(index-1).getTime();
			mainController.getSlider().setValue(time);
		}
	}

}
