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
package de.nuton.toolBar;

import de.nuton.states.StateManager;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class CalibrateDistanceButton extends ToolBarButton{
	
	public CalibrateDistanceButton(ToolBarManager tbm) {
		this.tbm = tbm;
		setIcon(new Image(getClass().getResourceAsStream("/calibrateIcon.png")));
		button.setTooltip(new Tooltip("Distanz neu kalibrieren"));
	}
	
	@Override
	public void onClick() {
		//TODO: After new state system
/*		if (tbm.getMainController().getStateManager().getCurrentStateID() == StateManager.TRANSLATION) {
			tbm.getMainController().getStateManager().pauseCurrentState(StateManager.TRANSLATION_CALIBRATION);
		}
		if (tbm.getMainController().getStateManager().getCurrentStateID() == StateManager.CIRCULAR) {
			tbm.getMainController().getStateManager().pauseCurrentState(StateManager.CIRCULAR_CALIBRATION);
		}*/
	}

}
