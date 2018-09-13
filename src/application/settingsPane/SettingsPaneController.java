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
package application.settingsPane;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import settings.Settings;

public abstract class SettingsPaneController {

	protected SettingsController settingsController;
	protected Settings settingsObj;
	
	@FXML protected Pane rootPane;
	
	protected int returnState;
	
	public SettingsPaneController(SettingsController settingsController, String path) {
		this.settingsController = settingsController;
		this.settingsObj = settingsController.getSettingsObj();
		try {
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource(path));
			loader.setController(this);
			rootPane = (Pane) loader.load();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		returnState = -1;
	}
	
	public abstract void saveSettings();
	public abstract void reset();
	
	public Pane getRootPane() {
		return rootPane;
	}
	
	public int getReturnState() {
		return returnState;
	}
	
	
	
}
