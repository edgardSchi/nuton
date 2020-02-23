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

import de.nuton.application.Point;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class RemovePointButton extends ToolBarButton {
	
	private Point p;
	
	public RemovePointButton(ToolBarManager tbm) {
		this.tbm = tbm;
		setIcon(new Image(getClass().getResourceAsStream("/removePointIcon.png")));
		button.setTooltip(new Tooltip("Ausgewählten Punkt löschen"));
	}
	
	@Override
	public void onClick() {
		if (tbm.getMainController().getStateManager().getCurrentState() instanceof de.nuton.states.PointState) {
			p = tbm.getSelectedPoint();
			if (p != null) {
				tbm.getMainController().getStateManager().getCurrentState().getPoints().remove(p);
				tbm.getMainController().redraw();
				tbm.getMainController().updateLists();
				tbm.setSelectedPoint(null);
				double value = tbm.getMainController().getSlider().getValue();
				tbm.getMainController().getSlider().setValue(0);
				tbm.getMainController().getSlider().setValue(value);
			}
		}
	}

}
