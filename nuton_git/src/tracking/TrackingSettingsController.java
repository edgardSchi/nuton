package tracking;

import java.util.Optional;

import application.MainController;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import userSettings.ThemeLoader;

public class TrackingSettingsController extends Dialog<ButtonType> {
	@FXML
	private TextField radiusField;
	@FXML
	private TextField stepsizeField;
	//@FXML
	//private TextField scaleField;
	
	private MainController mainController;
	private ThemeLoader themeLoader;
	
	public TrackingSettingsController(MainController mainController, ThemeLoader themeLoader) {
		this.mainController = mainController;
		this.themeLoader = themeLoader;
		try {
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("TrackingSettingsPane.fxml"));
			loader.setController(this);
			Node node = (Node) loader.load();
			Stage stage = (Stage) getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/nutonLogo.png")));
			getDialogPane().setContent(node);
			
			setTitle("Tracking");
			
			getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
			stage.getScene().getStylesheets().add(themeLoader.getTheme());
			
			loadGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void showDialog() {
		Optional<ButtonType> result = showAndWait();
	    if (result.get() == ButtonType.APPLY) {
	    	TrackingManager tm = new TrackingManager(mainController, themeLoader, Integer.parseInt(radiusField.getText()),  Integer.parseInt(stepsizeField.getText()),  1);
	    	tm.openDialog();
	    } else {
	    	
	    }
	}
	
	private void loadGUI() {
		Button applyBtn = (Button) this.getDialogPane().lookupButton(ButtonType.APPLY);
		ChangeListener<String> listener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int i = 1;
				StringProperty stringProp = (StringProperty) observable;
				TextField textField = (TextField) stringProp.getBean();
				if (!newValue.matches("\\d*")) {
					textField.setText(newValue.replaceAll("[^\\d]", "1"));
				} else {
					if (!newValue.isEmpty()) {
						i = Integer.parseInt(newValue);
					}
				}
				
				if (i > 10000) {
					textField.setText(newValue.replaceAll(newValue, "10000"));
				}
				
				if (i == 0) {
					textField.setText(newValue.replaceAll(newValue, "1"));
				}
				
				if (newValue.isEmpty()) {
					applyBtn.setDisable(true);
				}
				
				if(radiusField.getText().isEmpty() || stepsizeField.getText().isEmpty()) {
					applyBtn.setDisable(true);
				} else {
					applyBtn.setDisable(false);
				}
			}
		};
		radiusField.textProperty().addListener(listener);
		stepsizeField.textProperty().addListener(listener);
		//scaleField.textProperty().addListener(listener);
		
		radiusField.setText("7");
		stepsizeField.setText("2");
		//scaleField.setText("100");
	}
	
	private ButtonType getApplyButton() {
		return this.getDialogPane().getButtonTypes().get(0);
	}

}
