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
import java.util.ArrayList;
import java.util.Optional;

import application.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import settings.Settings;
import userSettings.ThemeLoader;

public class SettingsController{ 

	@FXML Pane rootPane;
	@FXML Pane contentPane;
	@FXML ComboBox<String> motionBox;
	
	private static double SCHRITTWEITE = 1000; //Schrittweite in ms
	private static double LAENGENEINHEIT = 100; //Länge in cm
	private boolean settingsComplete = false;
	private MainController mainController;
	private Settings settingsObj;
	private Dialog<ButtonType> dialog;
	
	private ArrayList<SettingsPaneController> motionController;
	
	private int currentMotion;
	
	public SettingsController(MainController mainController, Settings settings, ThemeLoader themeLoader) {		
		try {
			dialog = new Dialog<ButtonType>();
			dialog.setTitle("Einstellungen der Bewegung");
			this.settingsObj = settings;
			this.mainController = mainController;
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
			loader.setController(this);
			rootPane = (Pane) loader.load();
			Scene scene = new Scene(rootPane);
			scene.getStylesheets().add(themeLoader.getTheme());
			Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/nutonLogo.png")));
			dialog.getDialogPane().setContent(rootPane);
			dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
			
			initMotions();
			initMotionBox();
		} catch (IOException e) {
			e.printStackTrace();
		}		

	}
	
	private void initMotions() {
		motionController = new ArrayList<SettingsPaneController>();
		motionController.add(new SettingsTranslationPaneController(this, "SettingsTranslationPane.fxml"));
		motionController.add(new SettingsCircPaneController(this, "SettingsCircPane.fxml"));
	}

	private void saveSettings() {
		motionController.get(currentMotion).saveSettings();
	}
		
	
	public void showDialog() {
		Optional<ButtonType> result = dialog.showAndWait();
	    if (result.isPresent() && result != null && result.get() == ButtonType.APPLY) {
			saveSettings();
			mainController.getStateManager().setState(motionController.get(currentMotion).getReturnState());
			mainController.restartBtn.setDisable(false);
			mainController.fertigBtn.setDisable(false);
			mainController.getSlider().setMajorTickUnit(settingsObj.getSchrittweite());
			mainController.getSlider().setMax(calcMaxSlider(settingsObj.getSchrittweite(), mainController.getPlayer().getTotalDuration().toMillis()));
			System.out.println(settingsObj.getSchrittweite());
	    } else {
	    	//FIXEN//
	    	reset();
	    	mainController.reset();
	    }
	    dialog.setResult(null);
	}

	public static double calcMaxSlider(double schrittweite, double duration) {
		double max = 0;
		for (int i = 0; i <= duration; i += schrittweite) {
			max = i;
		}
		return max;
	}
	
	private void initMotionBox() {
		motionBox.getItems().addAll("Translation", "Kreisbewegung");
		
		motionBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(motionBox.getSelectionModel().getSelectedItem() == "Translation") {
					contentPane.getChildren().clear();
					motionController.get(0).reset();
					contentPane.getChildren().addAll(motionController.get(0).getRootPane());
					currentMotion = 0;
				}
				if (motionBox.getSelectionModel().getSelectedItem() == "Kreisbewegung") {
					contentPane.getChildren().clear();
					motionController.get(1).reset();
					contentPane.getChildren().addAll(motionController.get(1).getRootPane());
					currentMotion = 1;
				}
				getApplyBtn().setDisable(false);
			}
			
		});
		motionBox.getSelectionModel().select(0);
		contentPane.getChildren().addAll(motionController.get(0).getRootPane());
		currentMotion = 0;
	}
	
	public double getSCHRITTWEITE() {
		return SCHRITTWEITE;
	}

	public double getLAENGENEINHEIT() {
		return LAENGENEINHEIT;
	}
	
	public boolean getSettingsComplete() {
		return settingsComplete;
	}
	
	public void reset() {
		LAENGENEINHEIT = 100;
		SCHRITTWEITE = 1000;
	}




	public MainController getMainController() {
		return mainController;
	}


	public Settings getSettingsObj() {
		return settingsObj;
	}


	public static void setSCHRITTWEITE(double sCHRITTWEITE) {
		SCHRITTWEITE = sCHRITTWEITE;
	}


	public static void setLAENGENEINHEIT(double lAENGENEINHEIT) {
		LAENGENEINHEIT = lAENGENEINHEIT;
	}


	public void setSettingsComplete(boolean settingsComplete) {
		this.settingsComplete = settingsComplete;
	}
	
	public Button getApplyBtn() {
		return (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
	}
	
	
	
}
