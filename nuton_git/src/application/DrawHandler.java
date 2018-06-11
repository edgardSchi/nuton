package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawHandler {

	public static void drawDistance(GraphicsContext gc, Point a, Point b, String label) {
		gc.setFill(Color.AQUAMARINE);
		gc.setStroke(Color.AQUAMARINE);
		gc.fillRect(a.getDrawX() - 4, a.getDrawY() - 4, 8, 8);
		gc.fillRect(b.getDrawX() - 4, b.getDrawY() - 4, 8, 8);
		gc.strokeLine(a.getDrawX(), a.getDrawY(), b.getDrawX(), b.getDrawY());
		int d = 15;
		gc.strokeText(label, a.getDrawX() + d, a.getDrawY() + d);
	}
	
}
