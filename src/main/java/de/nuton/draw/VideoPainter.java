package de.nuton.draw;

import de.nuton.application.Point;
import de.nuton.math.MathUtils;
import de.nuton.math.Vector2;
import de.nuton.properties.PropertiesReader;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * This class is responsible for the communication between the canvas and the draw handler. Everything that should be drawn on the canvas must be called here.
 * Points are always converted from normalized to absolute form.
 */
public class VideoPainter {

    private static final Color CALIBRATION_DISTANCE_COLOR = Color.AQUAMARINE;
    private static final Color POINT_COLOR = PropertiesReader.getInstance().getPointColor();
    private static final Color CALIBRATION_POINT_COLOR = Color.rgb(255, 119, 0, 0.80);

    private static VideoPainter instance = null;
    private final Canvas canvas;
    private final DrawHandler drawHandler;

    private VideoPainter(Canvas canvas) {
        this.canvas = canvas;
        this.drawHandler = new DrawHandler(canvas.getGraphicsContext2D());
    }

    /**
     * Get the instance of DrawController. Before calling it for the first time, init() must be called.
     * @return The instance of DrawController
     */
    public static VideoPainter getInstance() {
        if (instance == null) {
            throw new AssertionError("You have to call init first!");
        }
        return instance;
    }

    /**
     * Initializes the draw controller. Needs to be called once before getInstance.
     * @param canvas The canvas
     * @return A VideoPainter
     */
    public static VideoPainter init(Canvas canvas) {
        if (instance != null) {
            throw new AssertionError("Init has already been called!");
        }
        instance = new VideoPainter(canvas);
        return instance;
    }

    /**
     * Clears a rectangular section of the canvas
     */
    public void clearScreen() {
        drawHandler.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Draws a straight line between two calibration points
     * @param a Point a
     * @param b Point b
     */
    public void drawCalibrationDistance(Point a, Point b) {
        this.drawCalibrationDistance(a, b, CALIBRATION_DISTANCE_COLOR, "");
    }

    /**
     * Draws a straight line between two calibration points
     * @param a Point a
     * @param b Point b
     * @param label Text above the line
     */
    public void drawCalibrationDistance(Point a, Point b, String label) {
       this.drawCalibrationDistance(a, b, CALIBRATION_DISTANCE_COLOR, label);
    }

    /**
     * Draws a straight line between two calibration points
     * @param a Point a
     * @param b Point b
     * @param color Color of the line
     */
    public void drawCalibrationDistance(Point a, Point b, Color color) {
        this.drawCalibrationDistance(a, b, color, "");
    }

    /**
     * Draws a straight line between two calibration points
     * @param a Point a
     * @param b Point b
     * @param color Color of the line
     * @param label Label of the line
     */
    public void drawCalibrationDistance(Point a, Point b, Color color, String label) {
        Vector2 normA = MathUtils.toAbsoluteCoordinates(a, canvas.getWidth(), canvas.getHeight());
        Vector2 normB = MathUtils.toAbsoluteCoordinates(b, canvas.getWidth(), canvas.getHeight());
        drawHandler.drawCalibrationDistance(normA.getX(), normA.getY(), normB.getX(), normB.getY(), color, label);
    }

    /**
     * Draws a straight line between two points
     * @param a Point a
     * @param b Point b
     */
    public void drawDistance(Point a, Point b) {
        this.drawDistance(a, b, POINT_COLOR);
    }

    /**
     * Draws a straight line between two points
     * @param a Point a
     * @param b Point b
     * @param color Color of the line
     */
    public void drawDistance(Point a, Point b, Color color) {
        Vector2 normA = MathUtils.toAbsoluteCoordinates(a, canvas.getWidth(), canvas.getHeight());
        Vector2 normB = MathUtils.toAbsoluteCoordinates(b, canvas.getWidth(), canvas.getHeight());
        drawHandler.drawDistance(normA.getX(), normA.getY(), normB.getX(), normB.getY(), color);
    }

    /**
     * Draws a straight line between two points
     * @param x1 X Coordinate of the first point
     * @param y1 Y Corrdinate of the first point
     * @param x2 X Coordinate of the second point
     * @param y2 Y Coordinate of the second point
     * @param color Color of the line
     */
    public void drawDistance(double x1, double y1, double x2, double y2, Color color) {
        drawHandler.drawDistance(x1, y1, x2, y2, color);
    }

    /**
     * Draws a straight line between two points
     * @param x1 X Coordinate of the first point
     * @param y1 Y Corrdinate of the first point
     * @param x2 X Coordinate of the second point
     * @param y2 Y Coordinate of the second point
     */
    public void drawDistance(double x1, double y1, double x2, double y2) {
        drawHandler.drawDistance(x1, y1, x2, y2, POINT_COLOR);
    }

    /**
     * Draws a point on the canvas above the video player
     * @param p Point that needs to be drawn
     * @param highlight Whether or not the point should be highlighted (inverse color)
     */
    public void drawPoint(Point p, boolean highlight) {
        Vector2 absP = MathUtils.toAbsoluteCoordinates(p, canvas.getWidth(), canvas.getHeight());
        drawHandler.drawPoint(absP.getX(), absP.getY(), POINT_COLOR, highlight);
    }

    /**
     * Draws a point on the canvas above the video player
     * @param p Point that needs to be drawn
     */
    public void drawPoint(Point p) {
       this.drawPoint(p, p.isHighlight());
    }

    /**
     * Draws a rectangle on the canvas above the video player, typically used for calibrations
     * @param color Color of the rectangle
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param width Width
     * @param height Height
     */
    public void drawCalibrationPoint(Color color, double x, double y, double width, double height) {
        drawHandler.drawCalibrationPoint(color, x, y);
    }

    /**
     * Draws a rectangle on the canvas above the video player, used for calibrations. Has standard color and size
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    public void drawCalibrationPoint(double x, double y) {
        drawHandler.drawCalibrationPoint(CALIBRATION_POINT_COLOR, x, y);
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
        drawHandler.drawSelectionRectangle(color, x1, y1, x2, y2);
    }

    /**
     * Draws a rectangle with a cross in the middle on the canvas above the video player
     * @param x1 X Coordinate of the top left corner
     * @param y1 Y Coordinate of the top left corner
     * @param x2 X Coordinate of the bottom right corner
     * @param y2 Y Coordinate of the bottom right corner
     */
    public void drawSelectionRectangle(double x1, double y1, double x2, double y2) {
        drawHandler.drawSelectionRectangle(POINT_COLOR, x1, y1, x2, y2);
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
        drawHandler.drawSelectionOval(color, x1, y1, x2, y2, width, height);
    }

    //TODO: Ordentliche Doku schreiben
    /**
     * Draws an oval with a cross in the middle on the canvas above the video player
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param width
     * @param height
     */
    public void drawSelectionOval(double x1, double y1, double x2, double y2, double width, double height) {
        drawHandler.drawSelectionOval(POINT_COLOR, x1, y1, x2, y2, width, height);
    }
}
