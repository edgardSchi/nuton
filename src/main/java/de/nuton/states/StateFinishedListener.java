package de.nuton.states;

import de.nuton.settings.MotionSettings;

import java.util.EventListener;

public interface StateFinishedListener extends EventListener {

    void onFinish(State state);
}
