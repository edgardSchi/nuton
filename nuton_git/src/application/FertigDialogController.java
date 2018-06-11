package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import diagrams.DiagramsController;
import io.Exporter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import states.Motion;
import userSettings.ThemeLoader;

public class FertigDialogController {
	@FXML
	private Button showDiagramButton;
	@FXML
	private Button exportButton;
	private Dialog<ButtonType> dialog;
	private ButtonType btnType;
	private DiagramsController diaController;
	private ArrayList<Point> points;
	private Exporter exporter;
	private PixelManager pManager;
	private MainController mainController;
	private Motion motion;

	public FertigDialogController(MainController mainController, PixelManager pixelManager, ArrayList<Point> points, Motion motion) {
		try {
			this.mainController = mainController;
			this.points = points;
			this.pManager = pixelManager;
			this.motion = motion;
			dialog = new Dialog<ButtonType>();
			dialog.setTitle("Optionen f�r Daten");
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("FertigDialog.fxml"));
			loader.setController(this);
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			ThemeLoader themeLoader = new ThemeLoader();
			scene.getStylesheets().add(themeLoader.getTheme());
			Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/nutonLogo.png")));
			
			btnType = new ButtonType("Schlie�en", ButtonData.FINISH);
			dialog.getDialogPane().getButtonTypes().add(btnType);
			dialog.initOwner(Main.getStage());
			dialog.initModality(Modality.WINDOW_MODAL);
			
			dialog.getDialogPane().setContent(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void showDialog() {
		showDiagramButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				diaController = new DiagramsController(mainController, pManager, motion);
				diaController.setPoints(points);
				diaController.calculateDiagrams();
				diaController.show();
			}
			
		});
		exportButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				exporter = new Exporter(points, pManager);
				exporter.exportData();
			}
			
		});
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (result.get() == btnType) {
				dialog.close();
			} else {
				dialog.close();
			}
		} else {
			dialog.close();
		}
	}
}
