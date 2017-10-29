package toolBar;

import application.MainEventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class OpenFileButton extends ToolBarButton{
	
	
	public OpenFileButton(MainEventHandler eventHandler) {
		setIcon(new Image("file:icons/openFileIcon.png"));
		setEventHandler(eventHandler.openFileDialog());
		button.setTooltip(new Tooltip("Video öffnen"));
	}
	
	@Override
	public void onClick() {
	}



}
