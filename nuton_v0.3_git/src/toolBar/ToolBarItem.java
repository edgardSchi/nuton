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
	protected MainEventHandler eventHandler;
	protected ToolBarManager tbm;
	
	public ToolBarItem(ToolBarManager tbm) {
		button = new Button();
		button.setPrefWidth(size);
		button.setPrefHeight(size);
		button.setMaxSize(size, size);
		button.setMinSize(size, size);
		eventHandler = new MainEventHandler(mainController);
		this.tbm = tbm;
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
