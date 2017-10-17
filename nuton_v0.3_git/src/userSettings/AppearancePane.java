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

public class AppearancePane {
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
			propReader = new PropertiesReader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		themeLoader = new ThemeLoader();
		loadThemeBox(themeLoader.getFileNames());
		
		applyBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				propWriter.setTheme(themeBox.getValue());
				propWriter.setPointColor(colorPicker.getValue().toString());
				System.out.println(colorPicker.getValue().toString());
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Sie müssen das Programm neu starten, um das neue Theme zu sehen.");
				alert.show();
			}			
		});
	}
	
	@SuppressWarnings("static-access")
	public void anchorPane(AnchorPane aPane) {
		aPane.setBottomAnchor(pane, 0.0);
		aPane.setRightAnchor(pane, 0.0);
		aPane.setLeftAnchor(pane, 0.0);
		aPane.setTopAnchor(pane, 0.0);
		//aPane.getChildren().setAll(pane);
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
}
