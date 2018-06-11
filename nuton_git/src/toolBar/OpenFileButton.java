package toolBar;

import application.MainEventHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class OpenFileButton extends ToolBarButton{
	
	
	public OpenFileButton(MainEventHandler eventHandler) {
		setIcon(new Image(getClass().getResourceAsStream("/openFileIcon.png")));
		setEventHandler(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				eventHandler.openFileDialog();
			}
		});
		button.setTooltip(new Tooltip("Video öffnen"));
	}
	
	@Override
	public void onClick() {
	}



}
