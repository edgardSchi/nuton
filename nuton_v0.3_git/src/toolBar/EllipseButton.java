package toolBar;

import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import states.State;

public class EllipseButton extends ToolBarToggleButton{

	public EllipseButton() {
		setIcon(new Image("file:icons/ellipseIcon.png"));
		button.setContentDisplay(ContentDisplay.CENTER);
	}
	
	@Override
	public void addPoint(State state, MouseEvent e) {
		toolBarEvents.AddPointEvents.addEllipse(state, e);
	}

}
