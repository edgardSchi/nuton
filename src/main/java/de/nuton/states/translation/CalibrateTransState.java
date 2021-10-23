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
package de.nuton.states.translation;

import java.util.Optional;

import de.nuton.draw.VideoPainter;
import de.nuton.application.MainController;
import de.nuton.application.ScalingManager;
import de.nuton.states.CalibrateState;
import de.nuton.states.StateManager;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CalibrateTransState extends CalibrateState { 
	
	public CalibrateTransState(MainController mainController) {
		super(mainController);
	}
	
	@Override
	public void init() {
		//resetSlider();
		super.init();
		mainController.setHelpLabel("Distanz kalibieren");
	}

	@Override
	public void onClick(MouseEvent e) {
		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
			VideoPainter.getInstance().drawCalibrationPoint(e.getX(), e.getY());
			
			addPointByMouse(e);
			
			if (clickCounter == 2) {
				VideoPainter.getInstance().drawDistance(calibratePoints[0].getX(), calibratePoints[0].getY(), calibratePoints[1].getX(), calibratePoints[1].getY(), Color.RED);

				TextInputDialog dialog = createDialog("Distanz für folgenen Wert speichern? ("+ settings.getLengthUnit().toString().toLowerCase() + "):", 1, 0);
				
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){	
					pManager.setCalibratePoints(calibratePoints);
					mainController.getSettings().setEichung(Double.parseDouble(result.get()));
					pManager.setEichung(Double.parseDouble(result.get()));
					resetCalibrate();
					
					//Überprüfen, ob ein State pausiert ist 
					if (mainController.getStateManager().statePaused()) {
						mainController.getStateManager().unpauseState();
					} else {
						mainController.getStateManager().setState(StateManager.TRANSLATION);
					}

					mainController.getStateManager().getCurrentState().setCalibratePoints(calibratePoints);
				} else {
					resetCalibrate();
				}
			}
		}
		
	}

	@Override
	public void keyPressed(int k) {

		
	}

	@Override
	public void keyReleased(int k) {

		
	}

	@Override
	public void fertigBtnClick() {

		
	}

	@Override
	public void reset() {

		
	}

	@Override
	public void redraw() {

		
	}

	@Override
	public void onKill() {

		
	}

	@Override
	public void onUnpause() {

		
	}


	
}
