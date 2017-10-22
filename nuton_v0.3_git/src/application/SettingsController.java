package application;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import states.StateManager;
import userSettings.ThemeLoader;

public class SettingsController {
	@FXML
	private ToggleGroup settings;	
	@FXML public Button saveSettingsBtn;
	private Stage stage;
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
	
	@SuppressWarnings("unchecked")
	SettingsController(MainController mainController) {		
		try {
			this.mainController = mainController;
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
			loader.setController(this);
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			ThemeLoader themeLoader = new ThemeLoader();
			scene.getStylesheets().add(themeLoader.getTheme());
			stage = new Stage();
			stage.setResizable(false);
			stage.setTitle("Einstellungen");
			stage.setScene(scene);
			stage.getIcons().add(new Image(SettingsController.class.getResourceAsStream("Nuton_logo.png")));
			
			richtungsBox.getItems().addAll("X-Achse", "Y-Achse");
			richtungsBox.setValue("X-Achse");
			
			xNullPunktBox.getItems().addAll("Links", "Rechts");
			xNullPunktBox.setValue("Links");
			
			yNullPunktBox.getItems().addAll("Oben", "Unten");
			yNullPunktBox.setValue("Oben");
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
					mainController.startBtn.setDisable(false);
				}
				
			});
			
			schrittweiteField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*")) {
						schrittweiteField.setText(newValue.replaceAll("[^\\d]", "1"));
					}
					
					int i = Integer.parseInt(newValue);
					if (i > 10000) {
						schrittweiteField.setText(newValue.replaceAll(newValue, "10000"));
					}
					
					if (i == 0) {
						schrittweiteField.setText(newValue.replaceAll(newValue, "1"));
					}
					
					if (newValue == "") {
						schrittweiteField.setText(newValue.replaceAll(newValue, "1"));
					}
				}
				
			});
			
			eichungsField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*")) {
						eichungsField.setText(newValue.replaceAll("[^\\d]", "1"));
					}
					
					int i = Integer.parseInt(newValue);
					if (i > 500) {
						eichungsField.setText(newValue.replaceAll(newValue, "500"));
					}
					
//					if (i < 10) {
//						eichungsField.setText(newValue.replaceAll(newValue, "10"));
//					}
					
					if (newValue == "") {
						eichungsField.setText(newValue.replaceAll(newValue, "10"));
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
		
		
		buttonClicked();
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
			System.out.println(t);
			SCHRITTWEITE = t;
			
			String e = eichungsField.getText();
			int i = Integer.parseInt(e);
			LAENGENEINHEIT = i;
			
			mainController.slider.setMajorTickUnit(SCHRITTWEITE);
			mainController.slider.setBlockIncrement(SCHRITTWEITE);
		}
	}
	
	public void buttonClicked() {
		
		saveSettingsBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println(achsenBox.getSelectionModel().getSelectedItem());		
				saveSettings();
				stage.close();
				mainController.getStateManager().setState(StateManager.CALIBRATION);
				mainController.setSCHRITTWEITE(SCHRITTWEITE);
				mainController.setEICHUNG(LAENGENEINHEIT);
				mainController.restartBtn.setDisable(false);
				mainController.fertigBtn.setDisable(false);
			}		
		});
	}
	
	public boolean xFixed() {
		boolean choice = false;
		if (richtungsBox.getSelectionModel().getSelectedItem() == "Y-Achse") {
			if (achsenBox.getSelectionModel().getSelectedItem() == "X-Achse") {
				choice = true;
			}
		}
		return choice;
	}
	
	public boolean yFixed() {
		boolean choice = false;
		if (richtungsBox.getSelectionModel().getSelectedItem() == "X-Achse") {
			if (achsenBox.getSelectionModel().getSelectedItem() == "Y-Achse") {
				choice = true;
			}
		}
		return choice;
	}
	
	public boolean yRichtung() {
		if (richtungsBox.getSelectionModel().getSelectedItem() == "Y-Achse") {
			return true;
		} else {
			return false;
		}
		
	}
	
	public void show() {
		stage.show();
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
	
	public boolean xNullRechts() {
		boolean xRechts = false;
		if (xNullPunktBox.getSelectionModel().getSelectedItem() == "Links") {
			xRechts = false;
		} else if (xNullPunktBox.getSelectionModel().getSelectedItem() == "Rechts") {
			xRechts = true;
		}
		return xRechts;
	}
	
	public boolean yNullUnten() {
		boolean yUnten = false;
		if (yNullPunktBox.getSelectionModel().getSelectedItem() == "Oben") {
			yUnten = false;
		} else if (yNullPunktBox.getSelectionModel().getSelectedItem() == "Unten") {
			yUnten = true;
		}
		return yUnten;
	}
	
}
