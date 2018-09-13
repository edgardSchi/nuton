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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import settings.Settings;
import states.StateManager;

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
	
	private boolean invalidEichung = false;
	private boolean invalidSchrittweite = false;

	
	public SettingsTranslationPaneController(SettingsController settingsController, String path) {		
		super(settingsController, path);
		returnState = StateManager.TRANSLATION_CALIBRATION;
			
			xNullPunktBox.getItems().addAll("Links", "Rechts");
			xNullPunktBox.setValue("Links");
			
			yNullPunktBox.getItems().addAll("Oben", "Unten");
			yNullPunktBox.setValue("Oben");
			
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
	public void saveSettings() {
		if (schrittweiteField.getText() != "") {
			String s = schrittweiteField.getText();
			int t = Integer.parseInt(s);
			settingsObj.setSchrittweite(t);
			
			String e = eichungsField.getText();
			int i = Integer.parseInt(e);
			settingsObj.setEichung(i);
			
			if (xFixed()) {
				settingsObj.setxFixed(true);
			} else {
				settingsObj.setxFixed(false);
			}
			
			if (yFixed()) {
				settingsObj.setyFixed(true);
			} else {
				settingsObj.setyFixed(false);
			}
			
			if (yNullUnten()) {
				settingsObj.setyNull(Settings.NULL_Y_BOTTOM);
			} else {
				settingsObj.setyNull(Settings.NULL_Y_TOP);
			}
			
			if (xNullRechts()) {
				settingsObj.setxNull(Settings.NULL_X_RIGHT);
			} else {
				settingsObj.setxNull(Settings.NULL_X_LEFT);
			}
			
			settingsController.getMainController().getSlider().setMajorTickUnit(settingsObj.getSchrittweite());
			settingsController.getMainController().getSlider().setBlockIncrement(settingsObj.getSchrittweite());
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
	
	private boolean xNullRechts() {
		boolean xRechts = false;
		if (xNullPunktBox.getSelectionModel().getSelectedItem() == "Links") {
			xRechts = false;
		} else if (xNullPunktBox.getSelectionModel().getSelectedItem() == "Rechts") {
			xRechts = true;
		}
		return xRechts;
	}
	
	private boolean yNullUnten() {
		boolean yUnten = false;
		if (yNullPunktBox.getSelectionModel().getSelectedItem() == "Oben") {
			yUnten = false;
		} else if (yNullPunktBox.getSelectionModel().getSelectedItem() == "Unten") {
			yUnten = true;
		}
		return yUnten;
	}
	
	@Override
	public void reset() {
		eichungsField.setText("100");
		schrittweiteField.setText("1000");
	}
}
