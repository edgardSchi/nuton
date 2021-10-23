package de.nuton.math;

import de.nuton.application.Point;

public class MathUtils {

    public static Vector2 toAbsoluteCoordinates(Point p, double width, double height) {
        return new Vector2(p.getX() * width, p.getY() * height);
    }

    public static Vector2 toAbsoluteCoordinates(double x, double y, double width, double height) {
        return new Vector2(x * width, y * height);
    }

    public static Vector2 toNormalizedCoordinates(Point p, double width, double height) {
        return new Vector2(p.getX() / width, p.getY() / height);
    }

    public static Vector2 toNormalizedCoordinates(double x, double y, double width, double height) {
        return new Vector2(x / width, y / height);
    }
}
