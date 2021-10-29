package de.nuton.states.circl;

import de.nuton.application.MainController;
import de.nuton.states.StateChain;
import de.nuton.states.calibration.CalibrateDistanceState;
import de.nuton.states.calibration.CalibratePointState;

public class CircularStateChain extends StateChain {

    public CircularStateChain(MainController mainController) {
        super(mainController);

        CalibrateDistanceState cds = new CalibrateDistanceState(mainController);
        CalibratePointState cps = new CalibratePointState(mainController);
        CircularState cs = new CircularState(mainController);

        this.addState(cds, "calibrationPoints");
        this.addState(cps, "calibrationPoint", "calibrationPoints");
        this.addState(cs);
    }
}
