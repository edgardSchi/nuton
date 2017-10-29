package toolBar;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import states.State;

public class RectangleButton extends ToolBarToggleButton {

	
	public RectangleButton(ToolBarManager tbm) {
		this.tbm = tbm;
		setIcon(new Image("file:icons/rectangleIcon.png"));
		button.setContentDisplay(ContentDisplay.CENTER);
		button.setTooltip(new Tooltip("Rechteckauswahl"));
	}
	

	@Override
	public void addPoint(State state, MouseEvent e) {
		toolBarEvents.AddPointEvents.addRectangle(state, e);
	}
	



}
