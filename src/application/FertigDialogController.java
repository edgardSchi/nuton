/*******************************************************************************
 * Nuton
 *   Copyright (C) 2018-2019 Edgard Schiebelbein
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
