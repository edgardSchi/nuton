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
package de.nuton.states.pendel;

import java.util.Optional;

import de.nuton.application.MainController;
import de.nuton.draw.VideoPainter;
import de.nuton.states.CalibrateState;
import de.nuton.application.ScalingManager;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CalibratePendelState extends CalibrateState{

	/*
	 * points(0) == Achsenmittelpunkt
	 * points(1) == erster Punkt für die Distanzkalibrierung
	 * points(2) == zweiter Punkt für die Distanzkalibierung
	 */
	
	public CalibratePendelState(MainController mainController) {
		super(mainController);
		setPointAmount(3);
	}

	@Override
	public void init() {
		resetSlider();
		mainController.setHelpLabel("Distanz kalibrieren");
	}

	@Override
	public void onClick(MouseEvent e) {
		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
			VideoPainter.getInstance().drawCalibrationPoint(e.getX(), e.getY());
			
			addPointByMouse(e);
			
			if(clickCounter == 2) {
				VideoPainter.getInstance().drawDistance(calibratePoints[0], calibratePoints[1], Color.RED);
			}
			
			if(clickCounter == 3) {
				TextInputDialog dialog = createDialog("Mittelpunkt und Distanz mit folgendem Wert speichern? (cm):", 1, 0);
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					pManager.setCalibratePoints(calibratePoints);
					mainController.getSettings().setEichung(Double.parseDouble(result.get()));
					pManager.setEichung(Double.parseDouble(result.get()));
					pManager.setOrigin(calibratePoints[2]);
					VideoPainter.getInstance().clearScreen();
					clickCounter = 0;
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
