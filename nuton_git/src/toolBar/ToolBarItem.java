package toolBar;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public abstract class ToolBarItem {
	
	public static final int SIZE = 25;
	protected ToolBarManager tbm;
	protected Node node;
	
	public Node getNode() {
		return node;
	};
	
	public abstract void onClick();
	
	protected Button createButton() {
		Button button = new Button();
		node = button;
		button.setPrefWidth(SIZE);
		button.setPrefHeight(SIZE);
		button.setMaxSize(SIZE, SIZE);
		button.setMinSize(SIZE, SIZE);
		return button;
	}
	
	protected ToggleButton createToggleButton() {
		ToggleButton button = new ToggleButton();
		node = button;
		button.setPrefWidth(SIZE);
		button.setPrefHeight(SIZE);
		button.setMaxSize(SIZE, SIZE);
		button.setMinSize(SIZE, SIZE);
		return button;
	}
	
	
}
