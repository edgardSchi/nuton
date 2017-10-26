package toolBar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ToolBarButton extends ToolBarItem{

	protected Button button;
	
	ToolBarButton() {
		button = createButton();
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				onClick();
			}
			
		});
	}
	
	public void setEventHandler(EventHandler<ActionEvent> event) {
		button.setOnAction(event);
	}
	
	public abstract void onClick();
	
	public void setIcon(Image icon) {
		ImageView view = new ImageView(icon);
		button.setGraphic(view);
	}

	
	public Button getButton() {
		return button;
	}
	
}
