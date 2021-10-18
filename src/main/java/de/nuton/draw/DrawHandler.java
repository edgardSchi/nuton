package de.nuton.draw;


import de.nuton.application.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class is responsible for drawing objects with a given graphics context
 */
public class DrawHandler {

	private static final int LINE_WIDTH = 2;
	private static final int CALIBRATION_RECT_SIZE = 12;

	private GraphicsContext gc;

	public DrawHandler(GraphicsContext gc) {
		this.gc = gc;
		gc.setLineWidth(LINE_WIDTH);
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
		int rectCenter = CALIBRATION_RECT_SIZE / 2;
		drawCalibratePoint(color,a.getDrawX() - rectCenter, a.getDrawY() - rectCenter);
		drawCalibratePoint(color,b.getDrawX() - rectCenter, b.getDrawY() - rectCenter);
		gc.strokeLine(a.getDrawX(), a.getDrawY(), b.getDrawX(), b.getDrawY());
		int textOffset = 15;
		gc.strokeText(label, a.getDrawX() + textOffset, a.getDrawY() + textOffset);
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
	 */
	public void drawCalibratePoint(Color color, double x, double y) {
		gc.setFill(color);
		gc.fillRect(x, y, CALIBRATION_RECT_SIZE, CALIBRATION_RECT_SIZE);
	}

	/**
	 * Draws a rectangle with a cross in the middle on the canvas above the video player
	 * @param color Color of the rectangle
	 * @param x1 X Coordinate of the top left corner
	 * @param y1 Y Coordinate of the top left corner
	 * @param x2 X Coordinate of the bottom right corner
	 * @param y2 Y Coordinate of the bottom right corner
	 */
	public void drawSelectionRectangle(Color color, double x1, double y1, double x2, double y2) {
		gc.setStroke(color);

		//Rectangle
		gc.strokeLine(x1, y1, x2, y1);
		gc.strokeLine(x2, y1, x2, y2);
		gc.strokeLine(x1, y1, x1, y2);
		gc.strokeLine(x1, y2, x2, y2);

		//Cross
		gc.strokeLine(((x2 - x1) / 2 + x1) - 5, ((y2 - y1) / 2 + y1), ((x2 - x1) / 2 + x1) + 5, ((y2 - y1) / 2 + y1));
		gc.strokeLine(((x2 - x1) / 2 + x1), ((y2 - y1) / 2 + y1) - 5, ((x2 - x1) / 2 + x1), ((y2 - y1) / 2 + y1) + 5);
	}

	//TODO: Ordentliche Doku schreiben
	/**
	 * Draws an oval with a cross in the middle on the canvas above the video player
	 * @param color Color of the oval
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param width
	 * @param height
	 */
	public void drawSelectionOval(Color color, double x1, double y1, double x2, double y2, double width, double height) {
		gc.setStroke(color);

		//Oval
		gc.strokeOval(x1, y1, width, height);

		//Cross
		double xAbs = Math.abs(x2 - x1) / 2 + x1;
		double yAbs = Math.abs(y2 - y1) / 2 + y1;
		gc.strokeLine(xAbs - 5, yAbs, xAbs + 5, yAbs);
		gc.strokeLine(xAbs, yAbs - 5, xAbs, yAbs + 5);
	}
}
