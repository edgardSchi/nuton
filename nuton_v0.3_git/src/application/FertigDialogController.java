package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

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
	

	public FertigDialogController(DiagramsController diaController, PixelManager pixelManager, ArrayList<Point> points) {
		try {
			this.diaController = diaController;
			this.points = points;
			this.pManager = pixelManager;
			dialog = new Dialog<ButtonType>();
			dialog.setTitle("Optionen für Daten");
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("FertigDialog.fxml"));
			loader.setController(this);
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			ThemeLoader themeLoader = new ThemeLoader();
			scene.getStylesheets().add(themeLoader.getTheme());
			Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/nutonLogo.png")));
			
			btnType = new ButtonType("Schließen", ButtonData.FINISH);
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
