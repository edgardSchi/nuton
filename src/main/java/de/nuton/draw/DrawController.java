package de.nuton.draw;

import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.properties.PropertiesReader;
import javafx.scene.paint.Color;

/**
 * This class is responsible for the communication between the main controller and the draw handler. Everything that should be drawn on the canvas must be called here.
 */
public class DrawController {

    private static final Color CALIBRATION_DISTANCE_COLOR = Color.AQUAMARINE;
    private static final Color POINT_COLOR = PropertiesReader.getInstance().getPointColor();
    private static final Color CALIBRATION_POINT_COLOR = Color.rgb(255, 119, 0, 0.80);

    private static DrawController instance = null;
    private final MainController mainController;
    private final DrawHandler drawHandler;

    private DrawController(MainController mainController) {
        this.mainController = mainController;
        this.drawHandler = new DrawHandler(mainController.getGc());
    }

    /**
     * Get the instance of DrawController. Before calling it for the first time, init() must be called.
     * @return The instance of DrawController
     */
    public static DrawController getInstance() {
        if (instance == null) {
            throw new AssertionError("You have to call init first!");
        }
        return instance;
    }

    /**
     * Initializes the draw controller. Needs to be called once before getInstance.
     * @param mainController The MainController
     * @return A DrawController
     */
    public static DrawController init(MainController mainController) {
        if (instance != null) {
            throw new AssertionError("Init has already been called!");
        }
        instance = new DrawController(mainController);
        return instance;
    }

    public void clearScreen() {
        drawHandler.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
    }

    /**
     * Draws a straight line between two calibration points
     * @param a Point a
     * @param b Point b
     */
    public void drawCalibrationDistance(Point a, Point b) {
        drawHandler.drawCalibrationDistance(a, b, CALIBRATION_DISTANCE_COLOR, "");
    }

    /**
     * Draws a straight line between two calibration points
     * @param a Point a
     * @param b Point b
     * @param label Text above the line
     */
    public void drawCalibrationDistance(Point a, Point b, String label) {
        drawHandler.drawCalibrationDistance(a, b, CALIBRATION_DISTANCE_COLOR, label);
    }

    /**
     * Draws a straight line between two calibration points
     * @param a Point a
     * @param b Point b
     * @param color Color of the line
     */
    public void drawCalibrationDistance(Point a, Point b, Color color) {
        drawHandler.drawCalibrationDistance(a, b, color, "");
    }

    /**
     * Draws a straight line between two calibration points
     * @param a Point a
     * @param b Point b
     * @param color Color of the line
     * @param label Label of the line
     */
    public void drawCalibrationDistance(Point a, Point b, Color color, String label) {
        drawHandler.drawCalibrationDistance(a, b, color, label);
    }

    /**
     * Draws a straight line between two points
     * @param a Point a
     * @param b Point b
     */
    public void drawDistance(Point a, Point b) {
        drawHandler.drawDistance(a, b, POINT_COLOR);
    }

    /**
     * Draws a straight line between two points
     * @param a Point a
     * @param b Point b
     * @param color Color of the line
     */
    public void drawDistance(Point a, Point b, Color color) {
        drawHandler.drawDistance(a, b, color);
    }

    /**
     * Draws a point on the canvas above the video player
     * @param p Point that needs to be drawn
     * @param highlight Whether or not the point should be highlighted (inverse color)
     */
    public void drawPoint(Point p, boolean highlight) {
        drawHandler.drawPoint(p, POINT_COLOR, highlight);
    }

    /**
     * Draws a point on the canvas above the video player
     * @param p Point that needs to be drawn
     */
    public void drawPoint(Point p) {
        drawHandler.drawPoint(p, POINT_COLOR, p.isHighlight());
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
        drawHandler.drawCalibratePoint(color, x, y, width, height);
    }

    /**
     * Draws a rectangle on the canvas above the video player, used for calibrations. Has standard color and size
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    public void drawCalibratePoint(double x, double y) {
        drawHandler.drawCalibratePoint(CALIBRATION_POINT_COLOR, x, y, 10, 10);
    }
}