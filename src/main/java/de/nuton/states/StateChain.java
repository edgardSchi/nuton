package de.nuton.states;

import de.nuton.application.MainController;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public abstract class StateChain extends State {

    private final ArrayList<State> states;
    private final ArrayList<String[]> dataKeys;
    private State currentState;

    public StateChain(MainController mainController) {
        super(mainController);
        states = new ArrayList<>();
        dataKeys = new ArrayList<>();
    }

    public void addState(State state, String ... transitionData) {
        states.add(state);
        this.dataKeys.add(transitionData);
        if (states.size() == 1) currentState = state;
    }

    private void stateTransition(State nextState, String ... keys) throws Exception {
        System.out.println("#####################################################");
        System.out.println("Transition from state " + currentState.toString() + " to state " + nextState.toString() + " with parameters:");
        for (String key : keys) {
            System.out.println(key);
            Object stateData = currentState.getStateData(key);
            nextState.setStateData(key, () -> stateData);
        }

        System.out.println("#####################################################");
        currentState = nextState;
        nextState.init();
    }

    private void applyChain() {
        for (int i = 0; i < states.size() - 1; i++) {
            int finalI = i;
            states.get(i).addStateFinishedListener(state -> {
                try {
                    stateTransition(states.get(finalI +1), dataKeys.get(finalI));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public Object getStateData(String key) throws Exception {
        return currentState.getStateData(key);
    }

    @Override
    public void setStateData(String key, Callable<Object> callable) {
        currentState.setStateData(key, callable);
    }

    @Override
    public void init() {
        applyChain();
        currentState.init();
    }

    @Override
    public void onClick(MouseEvent e) {
        currentState.onClick(e);
    }

    @Override
    public void keyPressed(int k) {
        currentState.keyPressed(k);
    }

    @Override
    public void keyReleased(int k) {
        currentState.keyReleased(k);
    }

    @Override
    public void fertigBtnClick() {
        currentState.fertigBtnClick();
    }

    @Override
    public void reset() {
        currentState.reset();
    }

    @Override
    public void redraw() {
        currentState.redraw();
    }

    @Override
    public void onKill() {
        currentState = states.get(0);
        currentState.onKill();
    }

    @Override
    public void onUnpause() {
        currentState.onUnpause();
    }
}
