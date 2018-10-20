/*******************************************************************************
 * Nuton
 * Copyright (C) 2018 Edgard Schiebelbein
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package states;

import java.util.ArrayList;

import application.MainController;
import application.Point;
import javafx.scene.input.MouseEvent;
import states.circl.CalibrateCircState;
import states.circl.CircState;
import states.circl.PostCalibrationCircState;
import states.stream.StreamState;
import states.tracking.TrackingState;
import states.translation.CalibrateTransState;
import states.translation.PostCalibrationState;
import states.translation.TranslationState;

public class StateManager {

	private ArrayList<State> states;
	private int currentState;
	private MainController mainController;
	
	public static final int DEFAULT = 0;
	public static final int TRANSLATION_CALIBRATION = 1;
	public static final int TRANSLATION = 2;
	public static final int TRANSLATION_POSTCALIBRATION = 3;
	public static final int CIRCULAR_CALIBRATION = 4;
	public static final int CIRCULAR = 5;
	public static final int CIRCULAR_POSTCALIBRATION = 6;
	public static final int TRACKING = 7;
	public static final int STREAMING = 8;
	
	public StateManager(MainController mainController) {
		this.mainController = mainController;
		currentState = 0;
		states = new ArrayList<State>();
		initStates();
		states.get(0).init();
	}
	
	private void initStates() {
		states.add(new DefaultState(mainController));
		states.add(new CalibrateTransState(mainController));
		states.add(new TranslationState(mainController));
		states.add(new PostCalibrationState(mainController));
		states.add(new CalibrateCircState(mainController));
		states.add(new CircState(mainController));
		states.add(new PostCalibrationCircState(mainController));
		states.add(new TrackingState(mainController));
		states.add(new StreamState(mainController));
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
	
	public void redraw() {
		states.get(currentState).redraw();
	}
	
	public State getCurrentState() {
		return states.get(currentState);
	}
	
	public int getCurrentStateID() {
		return currentState;
	}
	
	public void setState(int state) {
		states.get(currentState).onKill();
		states.get(state).init();
		currentState = state;
		System.out.println("Neuer State: " + currentState);
	}
	
	public void fertigBtnClick() {
		states.get(currentState).fertigBtnClick();
	}
	
	public ArrayList<Point> getPoints() {
		return states.get(currentState).getPoints();
	}
}
