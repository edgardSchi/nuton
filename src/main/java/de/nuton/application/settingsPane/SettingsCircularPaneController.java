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

import de.nuton.settings.CircularSettings;
import de.nuton.settings.MotionSettings;
import de.nuton.settings.Settings;
import de.nuton.states.Motion;
import de.nuton.states.StateManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

//TODO: Refactor class
public class SettingsCircularPaneController extends SettingsPaneController{

	@FXML
	private TextField eichungsField;
	@FXML
	private TextField schrittweiteField;
	@FXML
	private ComboBox<String> richtungsBox;
	@FXML
	private ComboBox<String> laengeBox;
	@FXML
	private ComboBox<String> zeitBox;
	
	private boolean invalidEichung = false;
	private boolean invalidSchrittweite = false;
	
	public SettingsCircularPaneController(SettingsController settingsController, String path) {
			super(settingsController, path, "Kreisbewegung");
			returnState = StateManager.CIRCULAR_CALIBRATION;
			richtungsBox.getItems().addAll("Gegen Uhrzeigersinn", "Im Uhrzeigersinn", "Automatisch");
			richtungsBox.setValue("Gegen Uhrzeigersinn");
			
			initUnitBoxes(zeitBox, laengeBox);
			
			schrittweiteField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					int i = 1;
					if (!newValue.matches("\\d*")) {
						schrittweiteField.setText(newValue.replaceAll("[^\\d]", "1"));
					} else {
						if (!newValue.isEmpty()) {
							i = Integer.parseInt(newValue);
						}
					}
					
					if (i > 10000) {
						schrittweiteField.setText(newValue.replaceAll(newValue, "10000"));
						invalidSchrittweite = false;
					}
					
					if (i == 0) {
						schrittweiteField.setText(newValue.replaceAll(newValue, "1"));
						invalidSchrittweite = false;
					}
					
					if (newValue.isEmpty()) {
						invalidSchrittweite = true;
						settingsController.getApplyBtn().setDisable(true);
					} else {
						invalidSchrittweite = false;
						if (!invalidSchrittweite && !invalidEichung) {
							settingsController.getApplyBtn().setDisable(false);
						}						
					}
				}
				
			});
			
			eichungsField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					int i = 1;
					if (!newValue.matches("\\d*")) {
						eichungsField.setText(newValue.replaceAll("[^\\d]", "1"));
					} else {
						if (!newValue.isEmpty()) {
							i = Integer.parseInt(newValue);
						}						
					}
									
					if (i > 10000) {
						eichungsField.setText(newValue.replaceAll(newValue, "10000"));
						invalidEichung = false;
					}
					
					if (i == 0) {
						eichungsField.setText(newValue.replaceAll(newValue, "1"));
						invalidEichung = false;
					}
					
					if (newValue.isEmpty()) {
						invalidEichung = true;
					} else {
						invalidEichung = false;
						if (!invalidSchrittweite && !invalidEichung) {
						}		
					}
				}
				
			});	
	}
	
	@Override
	public MotionSettings saveSettings() {
		if (!schrittweiteField.getText().equals("")) {
			
			String s = schrittweiteField.getText();
			int increment = Integer.parseInt(s);
			
			String e = eichungsField.getText();
			int calibration = Integer.parseInt(e);

			CircularSettings.Direction direction;

			if (againstClock()) {
				direction = CircularSettings.Direction.COUNTER_CLOCKWISE;
			} 
			else {
				direction = CircularSettings.Direction.CLOCKWISE;
			}

			//TODO: Wieso muss increment gesetzt werden?
			settingsController.getMainController().setIncrement(increment);

			//TODO: zeroX and zeroY variable
			return new CircularSettings(increment, calibration, getTimeUnit(zeitBox), getLengthUnit(laengeBox), MotionSettings.ZeroX.LEFT, MotionSettings.ZeroY.TOP, direction);
		}
		return null;
	}
	
	private boolean againstClock() {
		return (richtungsBox.getSelectionModel().getSelectedItem() == "Gegen Uhrzeigersinn");
	}
	
	@Override
	public void reset() {
		eichungsField.setText("100");
		schrittweiteField.setText("1000");
	}
}
