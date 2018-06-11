package toolBarEvents;

import application.MainController;
import application.Point;
import javafx.scene.input.MouseEvent;

public class MovePointEvents {
	
	private static double x = 0;
	private static double y = 0;
	private static double dragX = 0;
	private static double dragY = 0;
	public static void dragPoint(MainController mainController, MouseEvent e, Point p) {
		
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.isPrimaryButtonDown() && !e.isSecondaryButtonDown()) {
			dragX = e.getX();
			dragY = e.getY();	
		}		
		
		if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.isPrimaryButtonDown()) {		
			
			x = p.getDrawX() - dragX + e.getX();
			y = p.getDrawY() - dragY + e.getY();

			if(x >= 0 && x <= mainController.getCanvas().getWidth() && y >= 0 && y <= mainController.getCanvas().getHeight()) {
				p.setX((int)x);
				p.setY((int)y);
				
				
				dragX = e.getX();
				dragY = e.getY();	
				
				mainController.getScalingManager().normalizePoint(p);
				mainController.getScalingManager().updatePointPos(p);
				mainController.redraw();
				mainController.updateLists();
			}
			

		}
	}
	
}
