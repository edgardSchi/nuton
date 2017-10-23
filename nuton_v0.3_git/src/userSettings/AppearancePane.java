package userSettings;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import properties.PropertiesReader;
import properties.PropertiesWriter;

public class AppearancePane extends SettingsPane {
	@FXML
	private AnchorPane pane;
	private @FXML ComboBox<String> themeBox;
	private @FXML Button applyBtn;
	private @FXML ColorPicker colorPicker;
	private PropertiesWriter propWriter;
	private PropertiesReader propReader;
	private ThemeLoader themeLoader;

	public AppearancePane(PropertiesWriter propWriter) {
		this.propWriter = propWriter;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AppearancePane.fxml"));
		loader.setController(this);
		try {
			pane = loader.load();
			setPane(pane);
			propReader = new PropertiesReader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		themeLoader = new ThemeLoader();
		loadThemeBox(themeLoader.getFileNames());
		colorPicker.setValue(propReader.getPointColor());
		
		applyBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				confirmSettings();
			}			
		});
	}
	
	public AnchorPane getPane() {
		return pane;
	}
	
	private void loadThemeBox(String[] themes) {
		for (int i = 0; i < themes.length; i++) {
			themeBox.getItems().add(themes[i]);
			System.out.println(themes[i]);
		}
		themeBox.setValue(propReader.getTheme());
	}

	@Override
	public void confirmSettings() {
		propWriter.setTheme(themeBox.getValue());
		propWriter.setPointColor(colorPicker.getValue().toString());
//		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.setHeaderText(null);
//		alert.setContentText("Sie müssen das Programm neu starten, um das neue Theme zu sehen.");
//		alert.show();
	}
}
