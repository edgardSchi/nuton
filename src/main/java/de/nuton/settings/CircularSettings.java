package de.nuton.settings;

import de.nuton.math.UnitsHandler;

public class CircularSettings extends MotionSettings {

    public enum Direction {
        CLOCKWISE,
        COUNTER_CLOCKWISE
    }

    private final Direction direction;

    public CircularSettings(double increment, double calibration, UnitsHandler.TimeUnit timeUnit, UnitsHandler.LengthUnit lengthUnit, ZeroX zeroX, ZeroY zeroY, Direction direction) {
        super(increment, calibration, timeUnit, lengthUnit, zeroX, zeroY);
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }
}
