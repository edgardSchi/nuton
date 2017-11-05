package toolBar;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import states.State;

public class PointButton extends ToolBarToggleButton{
	
	public PointButton(ToolBarManager tbm) {
		this.tbm = tbm;
		setIcon(new Image(getClass().getResourceAsStream("/pointIcon.png")));
		button.setContentDisplay(ContentDisplay.CENTER);
		button.setTooltip(new Tooltip("Punkt"));
	}
	
	
	@Override
	public void addPoint(State state, MouseEvent e) {
		toolBarEvents.AddPointEvents.point(state, e);
	}
	
}
