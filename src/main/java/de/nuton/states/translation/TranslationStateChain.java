package de.nuton.states.translation;

import de.nuton.application.MainController;
import de.nuton.states.calibration.CalibrateDistanceState;
import de.nuton.states.StateChain;

public class TranslationStateChain extends StateChain {

    private CalibrateDistanceState cds;
    private TranslationState ts;

    public TranslationStateChain(MainController mainController) {
        super(mainController);

        cds = new CalibrateDistanceState(mainController);
        ts = new TranslationState(mainController);

        this.addState(cds, "settings", "calibrationPoints");
        this.addState(ts);
    }

}
