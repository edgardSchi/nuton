package toolBar;

import application.MainController;
import application.MainEventHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ToolBarItem {
	private Button button;
	private int size = 25;
	protected MainController mainController;
	protected static MainEventHandler eventHandler;
	
	public ToolBarItem(MainEventHandler eventHandler) {
		button = new Button();
		button.setPrefWidth(size);
		button.setPrefHeight(size);
		button.setMaxSize(size, size);
		button.setMinSize(size, size);
		this.mainController = mainController;
		eventHandler = new MainEventHandler(mainController);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				onClick();
			}
			
		});
	}
	
	public ToolBarItem() {
		button = new Button();
		button.setPrefWidth(size);
		button.setPrefHeight(size);
		button.setMaxSize(size, size);
		button.setMinSize(size, size);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				onClick();
			}
			
		});
	}
	
	public void setIcon(Image icon) {
		ImageView view = new ImageView(icon);
		button.setGraphic(view);
	}
	
	public Button getButton() {
		return button;
	}
	
	public abstract void onClick();
	
	
	public void setEventHandler(EventHandler<ActionEvent> event) {
		button.setOnAction(event);
	}
	
}
