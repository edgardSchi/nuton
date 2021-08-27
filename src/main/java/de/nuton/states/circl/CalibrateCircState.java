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
package de.nuton.states.circl;

import java.util.Optional;

import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.application.ScalingManager;
import de.nuton.states.CalibrateState;
import de.nuton.states.StateManager;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CalibrateCircState extends CalibrateState{

	private Point origin;
	
	public CalibrateCircState(MainController mainController) {
		super(mainController);
		setPointAmount(3);
	}

	@Override
	public void init() {
		//resetSlider();
		mainController.clearCanvas();
		origin = pManager.getOrigin();
		if(origin != null && mainController.getStateManager().statePaused()) {
			origin.drawPoint(gc);
		}
		mainController.setHelpLabel("Distanz kalibieren");
	}

	@Override
	public void onClick(MouseEvent e) {
		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
			gc.setFill(Color.rgb(255, 119, 0, 0.80));
			gc.fillRect(e.getX() - 5, e.getY() - 5, 10, 10);
			
			addPointByMouse(e);
			
			if (clickCounter == 2) {
				gc.setStroke(Color.RED);
				gc.strokeLine(calibratePoints[0].getX(), calibratePoints[0].getY(), calibratePoints[1].getX(), calibratePoints[1].getY());
				mainController.setHelpLabel("Mittelpunkt auswählen");
			}
			
			if (clickCounter == 3) {						
				TextInputDialog dialog = createDialog("Mittelpunkt und Distanz mit folgendem Wert speichern? (cm):", 1, 0);
				
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					origin = calibratePoints[2];
					ScalingManager.getInstance().normalizePoint(origin);
					ScalingManager.getInstance().normalizePoint(calibratePoints[1]);
					pManager.setCalibratePoints(calibratePoints);
					mainController.getSettings().setEichung(Double.parseDouble(result.get()));
					pManager.setEichung(Double.parseDouble(result.get()));
					pManager.setOrigin(origin);
					mainController.clearCanvas();
					clickCounter = 0;
					
					if(mainController.getStateManager().statePaused()) {
						mainController.getStateManager().unpauseState();
					} else {
						mainController.getStateManager().setState(StateManager.CIRCULAR);
					}
					
					mainController.getSlider().setSnapToTicks(true);
					mainController.getStateManager().getCurrentState().setCalibratePoints(calibratePoints);
				} else {
					resetCalibrate();
				}
			}
		}
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fertigBtnClick() {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redraw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnpause() {
		// TODO Auto-generated method stub
		
	}

}
