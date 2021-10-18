package de.nuton.draw;


import de.nuton.application.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class is responsible for drawing objects with a given graphics context
 */
public class DrawHandler {

	private GraphicsContext gc;

	public DrawHandler(GraphicsContext gc) {
		this.gc = gc;
	}

	/**
	 * Sets the GraphicsContext for drawing
	 * @param gc GraphicsContext used for drawing
	 */
	public void setGraphicsContext(GraphicsContext gc) {
		this.gc = gc;
	}

	/**
	 * Clears a rectangular section of the canvas
	 * @param x X Coordinate
	 * @param y	Y Coordinate
	 * @param width Width of the rectangle
	 * @param height Height of the rectangle
	 */
	public void clearRect(double x, double y, double width, double height) {
		gc.clearRect(x, y, width, height);
	}


	/**
	 * Draws a straight line between two calibration points
	 * @param a Point a
	 * @param b Point b
	 * @param color Color of the line
	 * @param label Label of the line
	 */
	public void drawCalibrationDistance(Point a, Point b, Color color, String label) {
		gc.setFill(color);
		gc.setStroke(color);
		gc.fillRect(a.getDrawX() - 4, a.getDrawY() - 4, 8, 8);
		gc.fillRect(b.getDrawX() - 4, b.getDrawY() - 4, 8, 8);
		gc.strokeLine(a.getDrawX(), a.getDrawY(), b.getDrawX(), b.getDrawY());
		int d = 15;
		gc.strokeText(label, a.getDrawX() + d, a.getDrawY() + d);
	}

	/**
	 * Draws a straight line between two points
	 * @param a Point a
	 * @param b Point b
	 * @param color Color of the line
	 */
	public void drawDistance(Point a, Point b, Color color) {
		gc.setFill(color);
		gc.setStroke(color);
		gc.strokeLine(a.getDrawX(), a.getDrawY(), b.getDrawX(), b.getDrawY());
	}

	/**
	 * Draws a point on the canvas above the video player
	 * @param p Point that needs to be drawn
	 * @param color Color of the point
	 * @param highlight Whether or not the point should be highlighted (inverse color)
	 */
	public void drawPoint(Point p, Color color, boolean highlight) {
		int drawX = p.getDrawX();
		int drawY = p.getDrawY();
		if (highlight) {
			color = color.invert();
		}
		gc.setStroke(color);
		gc.strokeLine(drawX + 5, drawY, drawX - 5, drawY);
		gc.strokeLine(drawX, drawY + 5, drawX, drawY - 5);
	}

	/**
	 * Draws a rectangle on the canvas above the video player, typically used for calibrations
	 * @param color Color of the rectangle
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @param width Width
	 * @param height Height
	 */
	public void drawCalibratePoint(Color color, double x, double y, double width, double height) {
		gc.setFill(color);
		gc.fillRect(x, y, height, width);
	}
}
