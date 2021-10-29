/*******************************************************************************
 * Nuton
 *   Copyright (C) 2018-2019 Edgard Schiebelbein
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.nuton.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.states.circl.CircularStateChain;
import de.nuton.states.translation.TranslationStateChain;
import javafx.scene.input.MouseEvent;

public class StateManager {

	private ArrayList<State> states;
	private int currentStateID;
	private MainController mainController;
	
	public static final int DEFAULT = 0;
	public static final int TRANSLATION_CALIBRATION = 1;
	//public static final int TRANSLATION = 2;
	public static final int CIRCULAR_CALIBRATION = 2;
	//public static final int CIRCULAR = 4;
	public static final int TRACKING = 5;
	public static final int STREAMING = 6;
	
	private State pausedState;
	private int pausedStateID;
	private State currentState;
	
	public StateManager(MainController mainController) {
		this.mainController = mainController;
		currentStateID = 0;
		pausedStateID = -1;
		states = new ArrayList<State>();
		initStates();
		states.get(0).init();
		currentState = states.get(currentStateID);
	}
	
	private void initStates() {
		states.add(new DefaultState(mainController));
		//states.add(new CalibrateTransState(mainController));
		states.add(new TranslationStateChain(mainController));
		//states.add(new TranslationState(mainController));
		states.add(new CircularStateChain(mainController));
		//states.add(new CircState(mainController));
		//states.add(new TrackingState(mainController));
	}
	
	public void init() {
		currentState.init();
	}
	
	public void onClick(MouseEvent e) {
		currentState.onClick(e);
	}
	
	public void keyPressed(int k) {
		currentState.keyPressed(k);
	}
	
	public void keyReleased(int k) {
		currentState.keyReleased(k);
	}
	
	public void redraw() {
		currentState.redraw();
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public int getCurrentStateID() {
		return currentStateID;
	}
	
	public void pauseCurrentState(int state) {
		pausedState = currentState;
		pausedStateID = currentStateID;
		setState(state);
	}
	
	public void unpauseState() {
		if(pausedState != null) {
			currentState = pausedState;
			currentStateID = pausedStateID;
			currentState.onUnpause();
			pausedState = null;
			pausedStateID = -1;
		}
	}

	public void loadState(State state) {
		currentState = state;
	}
	
	public boolean statePaused() {
		return pausedState != null;
	}
	
	public void setState(int state) {
		currentState.onKill();
		currentState = states.get(state);
		currentState.init();
		currentStateID = state;
		System.out.println("Neuer State: " + currentStateID);
	}
	
	public void fertigBtnClick() {
		currentState.fertigBtnClick();
	}

	//Hacky way
	public Optional<List<Point>> getPoints() {
		try {
			return  Optional.of((List<Point>) getCurrentState().getStateData("points"));
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
