package toolBar;

import application.Point;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class RemovePointButton extends ToolBarButton {
	
	private Point p;
	
	public RemovePointButton(ToolBarManager tbm) {
		this.tbm = tbm;
		setIcon(new Image(getClass().getResourceAsStream("/removePointIcon.png")));
		button.setTooltip(new Tooltip("Ausgewählten Punkt löschen"));
	}
	
	@Override
	public void onClick() {
		if (tbm.getMainController().getStateManager().getCurrentState() instanceof states.PointState) {
			p = tbm.getSelectedPoint();
			if (p != null) {
				tbm.getMainController().getStateManager().getCurrentState().getPoints().remove(p);
				tbm.getMainController().redraw();
				tbm.getMainController().updateLists();
				tbm.setSelectedPoint(null);
			}
		}
	}

}
