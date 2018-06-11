package toolBar;

import application.MainController;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class SaveButton extends ToolBarButton{

	private MainController mainController;
	
	public SaveButton(MainController mainController) {
		this.mainController = mainController;
		setIcon(new Image(getClass().getResourceAsStream("/saveIcon.png")));
		button.setTooltip(new Tooltip("Projekt speichern"));
	}
	
	@Override
	public void onClick() {
		mainController.getSaveFileMenu().fire();		
	}

}
