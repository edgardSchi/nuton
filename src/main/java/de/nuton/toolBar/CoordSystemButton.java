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

public class CoordSystemButton extends ToolBarButton {
	
	public CoordSystemButton(ToolBarManager tbm) {
		this.tbm = tbm;
	}

	@Override
	public void onClick() {
		tbm.getMainController().getStateManager().pauseCurrentState(StateManager.TRANSLATION_CALIBRATION);
	}

}
