package states;

import java.util.ArrayList;

import application.MainController;
import javafx.scene.input.MouseEvent;

public class StateManager {

	private ArrayList<State> states;
	private int currentState;
	private MainController mainController;
	
	public static final int DEFAULT = 0;
	public static final int CALIBRATION = 1;
	public static final int TRANSLATION = 2;
	
	public StateManager(MainController mainController) {
		this.mainController = mainController;
		currentState = 0;
		states = new ArrayList<State>();
		initStates();
	}
	
	private void initStates() {
		states.add(new DefaultState(this));
		states.add(new CalibrateState(mainController, mainController.getPManager()));
		states.add(new TranslationState(mainController, mainController.getPManager()));
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
	
	public void setState(int state) {
		currentState = state;
	}
	
	public void fertigBtnClick() {
		states.get(currentState).fertigBtnClick();
	}
}
