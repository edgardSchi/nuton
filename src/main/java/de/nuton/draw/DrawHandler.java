package de.nuton.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class is responsible for drawing objects with a given graphics context
 */
public class DrawHandler {

	private static final int LINE_WIDTH = 2;
	private static final int CALIBRATION_RECT_SIZE = 12;
	private static final int POINT_LENGTH = 5;

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
	 * @param x1 X Coordinate of the first point
	 * @param y1 Y Coordinate of the first point
	 * @param x2 X Coordinate of the second point
	 * @param y2 Y Coordinate of the second point
	 * @param color Color of the line
	 * @param label Label of the line
	 */
	public void drawCalibrationDistance(double x1, double y1, double x2, double y2, Color color, String label) {
		gc.setFill(color);
		gc.setStroke(color);

		drawCalibrationPoint(color, x1, y1);
		drawCalibrationPoint(color, x2, y2);
		gc.strokeLine(x1, y1, x2, y2);
		int textOffset = 15;
		gc.strokeText(label, x1 + textOffset, y1 + textOffset);
	}

	/**
	 * Draws a straight line between two points
	 * @param a Point a
	 * @param b Point b
	 * @param color Color of the line
	 */
	public void drawDistance(double x1, double y1, double x2, double y2, Color color) {
		gc.setFill(color);
		gc.setStroke(color);
		gc.strokeLine(x1, y1, x2, y2);
	}

	/**
	 * Draws a point on the canvas above the video player
	 * @param x X Coordinate of the point
	 * @param y Y Coordinate of the point
	 * @param color Color of the point
	 * @param highlight Whether or not the point should be highlighted (inverse color)
	 */
	public void drawPoint(double x, double y, Color color, boolean highlight) {
		if (highlight) {
			color = color.invert();
		}
		gc.setStroke(color);
		gc.strokeLine(x + POINT_LENGTH, y, x - POINT_LENGTH, y);
		gc.strokeLine(x, y + POINT_LENGTH, x, y - POINT_LENGTH);
	}

	/**
	 * Draws a rectangle on the canvas above the video player, typically used for calibrations
	 * @param color Color of the rectangle
	 * @param x X Coordinate of the middle of the rectangle
	 * @param y Y Coordinate of the middle of the rectangle
	 */
	public void drawCalibrationPoint(Color color, double x, double y) {
		gc.setFill(color);
		double rectCenter = CALIBRATION_RECT_SIZE / 2;
		gc.fillRect(x - rectCenter, y - rectCenter, CALIBRATION_RECT_SIZE, CALIBRATION_RECT_SIZE);
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
