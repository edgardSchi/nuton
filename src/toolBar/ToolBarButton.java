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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ToolBarButton extends ToolBarItem{

	protected Button button;
	
	ToolBarButton() {
		button = createButton();
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				onClick();
			}
			
		});
	}
	
	public void setEventHandler(EventHandler<ActionEvent> event) {
		button.setOnAction(event);
	}
	
	public abstract void onClick();
	
	public void setIcon(Image icon) {
		ImageView view = new ImageView(icon);
		button.setGraphic(view);
	}

	
	public Button getButton() {
		return button;
	}
	
}
