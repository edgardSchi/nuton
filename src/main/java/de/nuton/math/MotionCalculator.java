package de.nuton.math;

import de.nuton.application.Point;
import de.nuton.settings.MotionSettings;
import java.util.ArrayList;
import java.util.List;

public class MotionCalculator {

    private List<Point> points;
    private MotionSettings settings;
    private Point[] calibrationPoints;
    private double increment;

    public MotionCalculator(List<Point> points, Point[] calibrationPoints, MotionSettings settings) {
        this.points = points;
        this.settings = settings;
        this.calibrationPoints = calibrationPoints;
        increment = settings.getIncrement();
    }

    /**
     * Converts a list of points from relative coordinates to the distances in meters
     * @param points
     * @param calibrationInMeters
     * @param calibrationLength
     * @return A vector where x is the distance on the X-Coordinate, y the distance on the Y-Coordinate and z the delta time
     */
    public static List<Vector3> convertToMeter(List<Point> points, double calibrationInMeters, double calibrationLength) {
        ArrayList<Vector3> meters = new ArrayList<>();
        double calibMeter = calibrationInMeters / calibrationLength;

        double xNull = points.get(0).getX();
        double yNull = points.get(0).getY();
        double tNull = points.get(0).getTime();

        for (Point p : points) {
            double x = calibMeter * (p.getX() - xNull);
            double y = calibMeter * (p.getY() - yNull);
            double t = p.getTime() - tNull;
            meters.add(new Vector3(x, y, t));
        }
        return meters;
    }

    public List<Double> getXSpeeds() {
        List<Double> speeds = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            speeds.add(points.get(i+1).getX() - points.get(i).getX());
        }
        return speeds;
    }
}
