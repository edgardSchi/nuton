package de.nuton.settings;

import de.nuton.math.UnitsHandler;

public class TranslationSettings extends MotionSettings {

    public enum FixedAxis {
        NONE,
        X,
        Y
    }

    private final FixedAxis fixedAxis;

    public TranslationSettings(double increment, double calibration, UnitsHandler.TimeUnit timeUnit, UnitsHandler.LengthUnit lengthUnit, ZeroX zeroX, ZeroY zeroY, FixedAxis fixedAxis) {
        super(increment, calibration, timeUnit, lengthUnit, zeroX, zeroY);
        this.fixedAxis = fixedAxis;
    }

    public FixedAxis getFixedAxis() {
        return fixedAxis;
    }

}
