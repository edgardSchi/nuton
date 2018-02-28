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
