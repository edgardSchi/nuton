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
			x = p.getX() - dragX + e.getX();
			y = p.getY() - dragY + e.getY();

			p.setX((int)x);
			p.setY((int)y);
			
			dragX = e.getX();
			dragY = e.getY();	
			mainController.redraw();
			mainController.updateLists();
		}
//		
//		if (e.getEventType() == MouseEvent.MOUSE_RELEASED && !e.isPrimaryButtonDown() && leftClicked) {
//			addPoint(state, e, (x2 - x) / 2 + x, (y2 - y) / 2 + y);
//			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
//			for(Point p : points) {
//				p.drawPoint(gc);
//			}
//			leftClicked = false;
//		}
	}
	
}
