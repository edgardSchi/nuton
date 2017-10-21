package states;

import java.util.ArrayList;

import javafx.scene.input.MouseEvent;

public class StateManager {

	private ArrayList<State> states;
	private int currentState;
	
	public static final int DEFAULT = 0;
	
	public StateManager() {
		currentState = 0;
		states = new ArrayList<State>();
		initStates();
	}
	
	private void initStates() {
		states.add(new DefaultState(this));
	}
	
	public void init() {
		states.get(currentState).init();
	}
	
	public void onClick(MouseEvent e) {
		states.get(currentState).onClick(e);
	}
	
	public void keyPressed(int k) {
		states.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k) {
		states.get(currentState).keyReleased(k);
	}
	
	public State getCurrentState() {
		return states.get(currentState);
	}
	
}
