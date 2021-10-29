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
import de.nuton.states.State;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public abstract class ToolBarToggleButton extends ToolBarItem {

	protected ToggleButton button;
	
	public ToolBarToggleButton() {
		button = createToggleButton();
		button.selectedProperty().addListener((p, ov, nv) -> {
	       if (button.isSelected()) {
	       }
	    });
	}
	
	@Override
	public void onClick() {

		
	}
	
	public void setIcon(Image icon) {
		ImageView view = new ImageView(icon);
		button.setGraphic(view);
	}
	
	public ToggleButton getToggleButton() {
		return button;
	}
	
	public boolean isSelected() {
		return button.isSelected();
	}
	
	public abstract void getPoint(State state, MouseEvent e);

}
