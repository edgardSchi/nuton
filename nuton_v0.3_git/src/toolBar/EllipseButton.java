package toolBar;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import states.State;

public class EllipseButton extends ToolBarToggleButton{

	public EllipseButton() {
		setIcon(new Image(getClass().getResourceAsStream("/ellipseIcon.png")));
		button.setContentDisplay(ContentDisplay.CENTER);
		button.setTooltip(new Tooltip("Ellipsenauswahl"));
	}
	
	@Override
	public void addPoint(State state, MouseEvent e) {
		toolBarEvents.AddPointEvents.addEllipse(state, e);
	}

}
