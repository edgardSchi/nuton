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

import de.nuton.application.MainController;
import javafx.scene.input.MouseEvent;

public class DefaultState extends State{
	
	
	public DefaultState(MainController mainController) {
		super(mainController);
		mainController.getFertigBtn().setDisable(true);
	}

	@Override
	public void init() {
		mainController.setHelpLabel("Start-Button drücken");
		mainController.getSlider().setSnapToTicks(false);
		mainController.getSlider().setValue(0);
		mainController.getFertigBtn().setDisable(true);
		mainController.getRestartBtn().setDisable(true);
	}

	@Override
	public void onClick(MouseEvent e) {
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
