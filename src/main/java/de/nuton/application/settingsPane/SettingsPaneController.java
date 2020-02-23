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
package de.nuton.application.settingsPane;

import java.io.IOException;

import de.nuton.math.UnitsHandler;
import de.nuton.math.UnitsHandler.LengthUnit;
import de.nuton.math.UnitsHandler.TimeUnit;
import de.nuton.settings.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

public abstract class SettingsPaneController {

	protected SettingsController settingsController;
	protected Settings settingsObj;
	private String name;
	
	@FXML protected Pane rootPane;
	
	protected int returnState;
	
	public SettingsPaneController(SettingsController settingsController, String path, String name) {
		this.settingsController = settingsController;
		this.settingsObj = settingsController.getSettingsObj();
		try {
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource(path));
			loader.setController(this);
			rootPane = (Pane) loader.load();
			this.name = name;
				
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
	
	public String getName() {
		return name;
	}
	
	protected void addToController() {
		settingsController.addMotion(this);
	}
	
	protected void initUnitBoxes(ComboBox<String> time, ComboBox<String> length) {
		length.getItems().addAll("km", "m", "dm", "cm", "mm", "µm");
		length.setValue("cm");
		
		time.getItems().addAll("h", "m", "s", "ms");
		time.setValue("ms");
	}
	
	/**
	 * Speichert die verwendete Einheit und den entsprechenden Wert in einem Settings-Objekt.
	 * @param time ComboBox, die für die Schrittweite zuständig ist
	 * @param length ComboBox, die für die Eichung zuständig ist
	 */
	protected void saveUnits(ComboBox<String> time, ComboBox<String> length) {
		UnitsHandler.TimeUnit tUnit = TimeUnit.S;
		switch (time.getValue()) {
		case "h":
			tUnit = TimeUnit.H;
			break;
		case "m":
			tUnit = TimeUnit.M;
			break;
		case "s":
			tUnit = TimeUnit.S;
			break;
		case "ms":
			tUnit = TimeUnit.MS;
			break;
		}
		settingsObj.setTimeUnit(tUnit);
		
		LengthUnit lUnit = LengthUnit.M;
		switch (length.getValue()) {
		case "km":
			lUnit = LengthUnit.KM;
			break;
		case "m":
			lUnit = LengthUnit.M;
			break;
		case "dm":
			lUnit = LengthUnit.DM;
			break;
		case "cm":
			lUnit = LengthUnit.CM;
			break;
		case "mm":
			lUnit = LengthUnit.MM;
			break;
		case "µm":
			lUnit = LengthUnit.MIM;
			break;
		}
		settingsObj.setLengthUnit(lUnit);
		
		System.out.println("Units: " + tUnit + " -- " + lUnit);
	}
	
	
}
