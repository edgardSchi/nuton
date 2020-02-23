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

import de.nuton.savingFile.TempSaving;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShowPointsButton extends ToolBarItem {
	
	public ShowPointsButton(ToolBarManager tbm) {
		this.tbm = tbm;
		ToggleButton button = new ToggleButton();
		button.setPrefSize(ToolBarItem.SIZE, ToolBarItem.SIZE);
		button.setMaxSize(SIZE, SIZE);
		button.setMinSize(SIZE, SIZE);
		
		button.setSelected(true);
		
		button.selectedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(button.isSelected()) {
					TempSaving.setShowPoints(true);
				} else {
					TempSaving.setShowPoints(false);
				}
				tbm.getMainController().redraw();
			}
			
		});
		
		
		Image icon = new Image(getClass().getResourceAsStream("/showPointsIcon.png"));	
		ImageView view = new ImageView(icon);
		button.setGraphic(view);
			
		this.node = button;
	}

	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		
	}

}
