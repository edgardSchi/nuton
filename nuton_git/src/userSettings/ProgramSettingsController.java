package userSettings;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import application.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import properties.PropertiesReader;
import properties.PropertiesWriter;

public class ProgramSettingsController extends Dialog<ButtonType> {
	
	private @FXML AnchorPane rootPane;
	private @FXML AnchorPane centerPane;
	private @FXML SplitPane splitPane;
	private @FXML TreeView<String> treeView;
	
	//Appearance
	private @FXML ComboBox<String> themeBox;
	private @FXML ColorPicker colorPicker;
	private @FXML Button helpTheme;
	private @FXML Button helpColor;
	
	
	//ffmpeg Pane
	private @FXML TextField pathField;
	private @FXML ComboBox<String> formatBox;
	private @FXML Button pathBtn;
	private @FXML Button sameDirPathBtn;
	private @FXML TextField sameDirPathField;
	private @FXML TextField argumentField;
	private @FXML Button helpPath;
	private @FXML Button helpFormat;
	private @FXML Button helpSavePath;
	private @FXML Button helpArguments;
	private PropertiesWriter propWriter;
	private PropertiesReader propReader;
	
	//ffmpeg EventHandler Wiki
	private EventHandler<ActionEvent> ffmpegWikiEvent;
	
	private MainController mainController;
	private ThemeLoader themeLoader;
	
	public ProgramSettingsController(MainController mainController, ThemeLoader themeLoader) {
		try {
			this.mainController = mainController;
			propWriter = new PropertiesWriter();
			propReader = new PropertiesReader();
			this.themeLoader = themeLoader;
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("ProgramSettings.fxml"));
			loader.setController(this);
			Node node = (Node) loader.load();
			Stage stage = (Stage) getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/nutonLogo.png")));
			getDialogPane().setContent(node);
			
			setTitle("Einstellungen");
			
			getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
			stage.getScene().getStylesheets().add(themeLoader.getTheme());
			
			ffmpegWikiEvent = new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					mainController.getHostServices().showDocument("https://github.com/edgardSchi/nuton/wiki/ffmpeg#ffmpeg-einstellungen");
				}
				
			};
			
			initFfmpegPart();
			initAppearancePart();
		} catch (IOException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Exception Dialog");
			alert.setHeaderText("Look, an Exception Dialog");
			alert.setContentText("Fehler!");

			// Create expandable Exception.
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String exceptionText = sw.toString();

			Label label = new Label("The exception stacktrace was:");

			TextArea textArea = new TextArea(exceptionText);
			textArea.setEditable(false);
			textArea.setWrapText(true);

			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(textArea, 0, 1);

			// Set expandable Exception into the dialog pane.
			alert.getDialogPane().setExpandableContent(expContent);

			alert.showAndWait();
		}
		
	}
	
	public void showDialog() {
		Optional<ButtonType> result = showAndWait();
	    if (result.get() == ButtonType.OK) {
	    	mainController.redraw();
	    	confirmSettings();
	        propWriter.confirm();
	    } else {
	    	 propWriter.reset();
	    }
	}
	
	private void initFfmpegPart() {
		formatBox.getItems().addAll("*.mp4", "*.m4v");
		formatBox.setValue("*." + propReader.getPrefVideoFormat());
		pathField.setText(propReader.getFfmpegPath());
		sameDirPathField.setText(propReader.getFfmpegSameOutputPath());
		argumentField.setText(propWriter.convertFfmpegArguments(propReader.getFfmpegArguments()));
		
		pathBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				FileChooser chooser = new FileChooser();
				File path = chooser.showOpenDialog(null);
				if (path != null) {
					pathField.setText(path.getParentFile().getAbsolutePath());
				}				
			}
			
		});
		
		sameDirPathBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				DirectoryChooser chooser = new DirectoryChooser();
				File dir = chooser.showDialog(null);
				if(dir != null) {
					sameDirPathField.setText(dir.getAbsolutePath());	
					
				}			
			}
			
		});
		
		helpPath.setOnAction(ffmpegWikiEvent);
		helpFormat.setOnAction(ffmpegWikiEvent);
		helpSavePath.setOnAction(ffmpegWikiEvent);
		helpArguments.setOnAction(ffmpegWikiEvent);
	}
	
	private void initAppearancePart() {
		loadThemeBox(themeLoader.getFileNames());
		colorPicker.setValue(propReader.getPointColor());	
	}
	
	private void loadThemeBox(String[] themes) {
		for (int i = 0; i < themes.length; i++) {
			themeBox.getItems().add(themes[i]);
			System.out.println(themes[i]);
		}
		themeBox.setValue(propReader.getTheme());
	}
	
	
	private void confirmSettings() {
		propWriter.setFfmpegPath(pathField.getText());
		propWriter.setPrefVideoFormat(formatBox.getValue());	
		propWriter.setFfmpegSameOutputPath(sameDirPathField.getText());
		
		propWriter.setTheme(themeBox.getValue());
		propWriter.setPointColor(colorPicker.getValue().toString());
		propWriter.setFfmpegArguments(argumentField.getText().split(" "));
	}
	

}
