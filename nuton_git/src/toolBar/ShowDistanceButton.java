/*******************************************************************************
 * Nuton
 * Copyright (C) 2018 Edgard Schiebelbein
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package toolBar;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import savingFile.TempSaving;

public class ShowDistanceButton extends ToolBarItem {
	
	public ShowDistanceButton(ToolBarManager tbm) {
		this.tbm = tbm;
		ToggleButton button = new ToggleButton();
		button.setPrefSize(ToolBarItem.SIZE, ToolBarItem.SIZE);
		button.setMaxSize(SIZE, SIZE);
		button.setMinSize(SIZE, SIZE);
		
		button.selectedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(button.isSelected()) {
					TempSaving.setShowDistance(true);
				} else {
					TempSaving.setShowDistance(false);
				}
				tbm.getMainController().redraw();
			}
			
		});
		
		button.setSelected(false);
		
		Image icon = new Image(getClass().getResourceAsStream("/showDistanceIcon.png"));	
		ImageView view = new ImageView(icon);
		button.setGraphic(view);
			
		this.node = button;
	}

	@Override
	public void onClick() {

	}

}
