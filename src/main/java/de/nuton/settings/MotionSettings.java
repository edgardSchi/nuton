package de.nuton.settings;

import de.nuton.math.UnitsHandler;

import java.io.Serializable;



public abstract class MotionSettings implements Serializable {

    public enum ZeroX {
        LEFT,
        RIGHT
    }

    public enum ZeroY {
        TOP,
        BOTTOM
    }

    private final double increment;
    private final double calibration;

    private final UnitsHandler.TimeUnit timeUnit;
    private final UnitsHandler.LengthUnit lengthUnit;

    private ZeroX zeroX;
    private ZeroY zeroY;

    MotionSettings(double increment, double calibration, UnitsHandler.TimeUnit timeUnit, UnitsHandler.LengthUnit lengthUnit, ZeroX zeroX, ZeroY zeroY) {
        this.increment = increment;
        this.calibration = calibration;
        this.timeUnit = timeUnit;
        this.lengthUnit = lengthUnit;
    }

/*    MotionSettings(double increment, double calibration) {
        this(increment, calibration, UnitsHandler.TimeUnit.MS, UnitsHandler.LengthUnit.CM);
    }*/

    public UnitsHandler.TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public UnitsHandler.LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public double getIncrement() {
        return increment;
    }

    public double getCalibration() {
        return calibration;
    }

    public ZeroX getZeroX() {
        return zeroX;
    }

    public ZeroY getZeroY() {
        return zeroY;
    }
}
