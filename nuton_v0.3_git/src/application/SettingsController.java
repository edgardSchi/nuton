package application;

import java.io.IOException;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import settings.Settings;
import states.StateManager;
import userSettings.ThemeLoader;

public class SettingsController{ 
	@FXML
	private ToggleGroup settings;	
	@FXML private TextField schrittweiteField;
	@FXML private ComboBox achsenBox;
	@FXML private TextField eichungsField;
	private static double SCHRITTWEITE = 1000; //Schrittweite in ms
	private static double LAENGENEINHEIT = 100; //Länge in cm
	private boolean settingsComplete = false;
	private MainController mainController;
	@FXML private ComboBox richtungsBox;
	@FXML private ComboBox xNullPunktBox;
	@FXML private ComboBox yNullPunktBox;
	private Settings settingsObj;
	private Dialog<ButtonType> dialog;
	private boolean invalidEichung = false;
	private boolean invalidSchrittweite = false;
	
	@SuppressWarnings("unchecked")
	SettingsController(MainController mainController, Settings settings) {		
		try {
			dialog = new Dialog<ButtonType>();
			dialog.setTitle("Einstellungen der Bewegung");
			this.settingsObj = settings;
			this.mainController = mainController;
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
			loader.setController(this);
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			ThemeLoader themeLoader = new ThemeLoader();
			scene.getStylesheets().add(themeLoader.getTheme());
			Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/nutonLogo.png")));
//			stage = new Stage();
//			stage.setResizable(false);
//			stage.setTitle("Einstellungen");
//			stage.setScene(scene);
//			stage.getIcons().add(new Image(SettingsController.class.getResourceAsStream("Nuton_logo.png")));
			
			richtungsBox.getItems().addAll("X-Achse", "Y-Achse");
			richtungsBox.setValue("X-Achse");
			
			xNullPunktBox.getItems().addAll("Links", "Rechts");
			xNullPunktBox.setValue("Links");
			
			yNullPunktBox.getItems().addAll("Oben", "Unten");
			yNullPunktBox.setValue("Oben");
			dialog.getDialogPane().setContent(root);
			dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
			
//			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//
//				@Override
//				public void handle(WindowEvent arg0) {
//					mainController.startBtn.setDisable(false);
//				}
//				
//			});
			
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
						dialog.getDialogPane().lookupButton(ButtonType.APPLY).setDisable(true);
						invalidSchrittweite = true;
					} else {
						invalidSchrittweite = false;
						if (!invalidSchrittweite && !invalidEichung) {
							dialog.getDialogPane().lookupButton(ButtonType.APPLY).setDisable(false);
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
									
					if (i > 1000) {
						eichungsField.setText(newValue.replaceAll(newValue, "1000"));
						invalidEichung = false;
					}
					
					if (i == 0) {
						eichungsField.setText(newValue.replaceAll(newValue, "1"));
						invalidEichung = false;
					}
					
					if (newValue.isEmpty()) {
						dialog.getDialogPane().lookupButton(ButtonType.APPLY).setDisable(true);
						invalidEichung = true;
					} else {
						invalidEichung = false;
						if (!invalidSchrittweite && !invalidEichung) {
							dialog.getDialogPane().lookupButton(ButtonType.APPLY).setDisable(false);
						}		
					}
				}
				
			});

				
		} catch (IOException e) {
			e.printStackTrace();
		}		

		
		achsenBox.getItems().addAll("Keine", "Y-Achse");
		achsenBox.setValue("Keine");

		richtungsBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (richtungsBox.getSelectionModel().getSelectedItem() == "X-Achse") {
					achsenBox.getItems().clear();
					achsenBox.getItems().addAll("Keine", "Y-Achse");
				}
				
				if (richtungsBox.getSelectionModel().getSelectedItem() == "Y-Achse") {
					achsenBox.getItems().clear();
					achsenBox.getItems().addAll("Keine", "X-Achse");
				}
			}
			
		});
		
		
		//buttonClicked();
	}
	
	
//	@FXML
//	private void saveSettingsAction(ActionEvent event) {
//		saveSettings();
//		settingsComplete = true;
//		stage.close();
//		System.out.println("Settings ready! (From Settings)");
//	}
	

	private void saveSettings() {
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
			
			if (yRichtung()) {
				settingsObj.setDirection(Settings.DIRECTION_Y);
			} else {
				settingsObj.setDirection(Settings.DIRECTION_X);
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
			
			mainController.slider.setMajorTickUnit(SCHRITTWEITE);
			mainController.slider.setBlockIncrement(SCHRITTWEITE);
		}
	}
		
	private boolean xFixed() {
		boolean choice = false;
		if (richtungsBox.getSelectionModel().getSelectedItem() == "Y-Achse") {
			if (achsenBox.getSelectionModel().getSelectedItem() == "X-Achse") {
				choice = true;
			}
		}
		return choice;
	}
	
	private boolean yFixed() {
		boolean choice = false;
		if (richtungsBox.getSelectionModel().getSelectedItem() == "X-Achse") {
			if (achsenBox.getSelectionModel().getSelectedItem() == "Y-Achse") {
				choice = true;
			}
		}
		return choice;
	}
	
	private boolean yRichtung() {
		if (richtungsBox.getSelectionModel().getSelectedItem() == "Y-Achse") {
			return true;
		} else {
			return false;
		}
		
	}
	
	public void showDialog() {
		Optional<ButtonType> result = dialog.showAndWait();
	    if (result.isPresent() && result != null && result.get() == ButtonType.APPLY) {
			saveSettings();
			mainController.getStateManager().setState(StateManager.CALIBRATION);
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
	    System.out.println("RESULT: " + result.toString());
	}

	private double calcMaxSlider(double schrittweite, double duration) {
		double max = 0;
		for (int i = 0; i <= duration; i += schrittweite) {
			max = i;
		}
		return max;
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
	
}
