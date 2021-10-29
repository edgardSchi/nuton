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

import de.nuton.settings.MotionSettings;
import de.nuton.settings.Settings;
import de.nuton.settings.TranslationSettings;
import de.nuton.states.StateManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SettingsTranslationPaneController extends SettingsPaneController{
	@FXML
	private TextField eichungsField;
	@FXML
	private ComboBox<String> xNullPunktBox;
	@FXML
	private ComboBox<String> yNullPunktBox;
	@FXML
	private TextField schrittweiteField;
	@FXML
	private ComboBox<String> achsenBox;
	@FXML
	private ComboBox<String> laengeBox;
	@FXML
	private ComboBox<String> zeitBox;
	
	private boolean invalidEichung = false;
	private boolean invalidSchrittweite = false;

	
	public SettingsTranslationPaneController(SettingsController settingsController, String path) {		
		super(settingsController, path, "Translation");
		returnState = StateManager.TRANSLATION_CALIBRATION;
			
			xNullPunktBox.getItems().addAll("Links", "Rechts");
			xNullPunktBox.setValue("Links");
			
			yNullPunktBox.getItems().addAll("Oben", "Unten");
			yNullPunktBox.setValue("Oben");
			
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

		
		achsenBox.getItems().addAll("Keine", "X-Achse", "Y-Achse");
		achsenBox.setValue("Keine");
			
	}
	
	@Override
	public MotionSettings saveSettings() {
		if (!schrittweiteField.getText().equals("")) {
			
			String s = schrittweiteField.getText();
			int increment = Integer.parseInt(s);
			
			String e = eichungsField.getText();
			int calibration = Integer.parseInt(e);

			MotionSettings.ZeroX zeroX;
			if (zeroXRight()) {
				zeroX = MotionSettings.ZeroX.RIGHT;
			} else {
				zeroX = MotionSettings.ZeroX.LEFT;
			}

			MotionSettings.ZeroY zeroY;
			if (zeroYBottom()) {
				zeroY = MotionSettings.ZeroY.BOTTOM;
			} else {
				zeroY = MotionSettings.ZeroY.TOP;
			}

			//TODO: Wieso muss increment gesetzt werden?
			settingsController.getMainController().setIncrement(increment);

			return new TranslationSettings(increment, calibration, getTimeUnit(zeitBox), getLengthUnit(laengeBox), zeroX, zeroY, fixedAxis());
		}
		return null;
	}


	private TranslationSettings.FixedAxis fixedAxis() {
		if (xFixed()) {
			return TranslationSettings.FixedAxis.X;
		} else if (yFixed()) {
			return TranslationSettings.FixedAxis.Y;
		} else {
			return TranslationSettings.FixedAxis.NONE;
		}
	}

	private boolean xFixed() {
		boolean choice = false;
			if (achsenBox.getSelectionModel().getSelectedItem() == "X-Achse") {
				choice = true;
			}
		return choice;
	}
	
	private boolean yFixed() {
		boolean choice = false;
		if (achsenBox.getSelectionModel().getSelectedItem() == "Y-Achse") {
				choice = true;
		}
		return choice;
	}
	
	private boolean zeroXRight() {
		boolean xRight = false;
		if (xNullPunktBox.getSelectionModel().getSelectedItem() == "Links") {
			xRight = false;
		} else if (xNullPunktBox.getSelectionModel().getSelectedItem() == "Rechts") {
			xRight = true;
		}
		return xRight;
	}
	
	private boolean zeroYBottom() {
		boolean yBottom = false;
		if (yNullPunktBox.getSelectionModel().getSelectedItem() == "Oben") {
			yBottom = false;
		} else if (yNullPunktBox.getSelectionModel().getSelectedItem() == "Unten") {
			yBottom = true;
		}
		return yBottom;
	}
	
	@Override
	public void reset() {
		eichungsField.setText("100");
		schrittweiteField.setText("1000");
	}
}
