package userSettings;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import properties.PropertiesWriter;

public class ProgramSettingsController extends Dialog<ButtonType> {
	
	private @FXML AnchorPane rootPane;
	private @FXML AnchorPane centerPane;
	private @FXML SplitPane splitPane;
	//private @FXML AnchorPane ffmpegPane;
	private @FXML TreeView<String> treeView;
	private Stage stage;
	private AnchorPane ff;
	private @FXML Button applyBtn;
	private @FXML Button cancelBtn;
	private FfmpegPane ffmpegPane;
	private AppearancePane appPane;
	
	//ffmpeg Pane
	private @FXML TextField pathField;
	private @FXML ComboBox<String> formatBox;
	private @FXML Button pathBtn;
	//private @FXML Button applyBtn;
	private PropertiesWriter propWriter;
	
	public ProgramSettingsController() {
		try {
			propWriter = new PropertiesWriter();
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("ProgramSettings.fxml"));
			loader.setController(this);
			loader.load();
			Stage stage = (Stage) getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(ProgramSettingsController.class.getResourceAsStream("Nuton_logo.png")));
			ffmpegPane = new FfmpegPane(propWriter);
			ffmpegPane.anchorPane(centerPane);
			appPane = new AppearancePane(propWriter);
			appPane.anchorPane(centerPane);
			centerPane.getChildren().setAll(ffmpegPane.getPane());
			
			setTitle("Einstellungen");
			
			getDialogPane().setContent(rootPane);
			getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
			initTree();
			ThemeLoader themeLoader = new ThemeLoader();
			stage.getScene().getStylesheets().add(themeLoader.getTheme());
			
			Optional<ButtonType> result = showAndWait();
		    if (result.get() == ButtonType.OK) {
		        System.out.println("OK");
		        propWriter.confirm();
		    } else {
		    	 propWriter.reset();
		    }
			
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
	
	@SuppressWarnings("unchecked")
	private void initTree() {
		TreeItem<String> rootItem = new TreeItem<String>();
		rootItem.setExpanded(true);
		TreeItem<String> ffmpegItem = new TreeItem<String>("ffmpeg");
		TreeItem<String> aussehenItem = new TreeItem<String>("Aussehen");
		rootItem.getChildren().addAll(ffmpegItem, aussehenItem);
		treeView.setRoot(rootItem);
		treeView.setShowRoot(false);
		MultipleSelectionModel<TreeItem<String>> msm = treeView.getSelectionModel();
		msm.select(0);
		
		treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateSelectedItem(newValue));
	}
	
	private void updateSelectedItem(Object newValue) {      
		if (treeView.getSelectionModel().isSelected(0)) {
			ffmpegPane = new FfmpegPane(propWriter);
			centerPane.getChildren().setAll(ffmpegPane.getPane());
			System.out.println("ffmpeg");
		} else if (treeView.getSelectionModel().isSelected(1)) {
			appPane = new AppearancePane(propWriter);
			centerPane.getChildren().setAll(appPane.getPane());
			System.out.println("Aussehen");
		}
		System.out.println("Click");
	}
	

}
