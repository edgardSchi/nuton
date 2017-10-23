package userSettings;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import properties.PropertiesReader;
import properties.PropertiesWriter;

public class FfmpegPane extends SettingsPane {

	private AnchorPane pane;
	private @FXML TextField pathField;
	private @FXML ComboBox<String> formatBox;
	private @FXML Button pathBtn;
	private @FXML Button applyBtn;
	private @FXML CheckBox sameDirBox;
	private @FXML HBox sameDirHBox;
	private @FXML Button sameDirPathBtn;
	private @FXML TextField sameDirPathField;
	private PropertiesReader propReader;
	private PropertiesWriter propWriter;
	
	public FfmpegPane(PropertiesWriter propWriter) {
		super.pane = this.pane;
		this.propWriter = propWriter;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FfmpegPane.fxml"));
		loader.setController(this);
		try {
			pane = loader.load();
			setPane(pane);
			propReader = new PropertiesReader();
			formatBox.getItems().addAll("*.mp4", "*.m4v");
			formatBox.setValue("*." + propReader.getPrefVideoFormat());
			pathField.setText(propReader.getFfmpegPath());
			sameDirPathField.setText(propReader.getFfmpegSameOutputPath());
			if (propReader.isFfmpegOutputSame()) {
				sameDirBox.setSelected(true);
			} else {
				sameDirBox.setSelected(false);
			}
			System.out.println("Pfad: " + propReader.getFfmpegPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pathBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				FileChooser chooser = new FileChooser();
				FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("ffmpeg", "*.exe");
				chooser.getExtensionFilters().add(filter);
				pathField.setText(chooser.showOpenDialog(null).getParentFile().getAbsolutePath());
			}
			
		});
		
		sameDirPathBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				DirectoryChooser chooser = new DirectoryChooser();
				sameDirPathField.setText(chooser.showDialog(null).getAbsolutePath());
			}
			
		});
		
		applyBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				confirmSettings();
			}
			
		});
		
		if (!sameDirBox.isSelected()) {
			sameDirHBox.setDisable(true);
		}
		
		sameDirBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (sameDirBox.isSelected()) {
					sameDirHBox.setDisable(false);
				} else {
					sameDirHBox.setDisable(true);
				}
			}
			
		});
	}
	
	

	
	public void confirmSettings() {
		propWriter.setFfmpegPath(pathField.getText());
		propWriter.setPrefVideoFormat(formatBox.getValue());
		propWriter.sameFfmpegOutput(sameDirBox.isSelected());		
		propWriter.setFfmpegSameOutputPath(sameDirPathField.getText());
	}
}
