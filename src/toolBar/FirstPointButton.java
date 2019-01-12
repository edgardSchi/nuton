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
package toolBar;

import application.MainController;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class FirstPointButton extends ToolBarButton {

	private MainController mainController;
	
	public FirstPointButton(ToolBarManager manager) {
		mainController = manager.getMainController();
		setIcon(new Image(getClass().getResourceAsStream("/firstPointIcon.png")));
		button.setTooltip(new Tooltip("Springe zum ersten Punkt"));
	}
	
	@Override
	public void onClick() {
		if (mainController.getStateManager().getPoints() != null && mainController.getStateManager().getPoints().size() != 0) {
			double time = mainController.getStateManager().getPoints().get(0).getTime();
			mainController.getSlider().setValue(time);
		}		
	}

}
