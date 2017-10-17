package toolBar;

import application.MainEventHandler;
import javafx.scene.image.Image;

public class OpenFileButton extends ToolBarItem{
	
	public OpenFileButton(MainEventHandler eventHandler) {
		setIcon(new Image("file:icons/openFileIcon.png"));
		setEventHandler(eventHandler.openFileDialog());
	}
	
	@Override
	public void onClick() {
	}

}
