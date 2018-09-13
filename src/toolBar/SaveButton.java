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

import application.MainController;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class SaveButton extends ToolBarButton{

	private MainController mainController;
	
	public SaveButton(MainController mainController) {
		this.mainController = mainController;
		setIcon(new Image(getClass().getResourceAsStream("/saveIcon.png")));
		button.setTooltip(new Tooltip("Projekt speichern"));
	}
	
	@Override
	public void onClick() {
		mainController.getSaveFileMenu().fire();		
	}

}
