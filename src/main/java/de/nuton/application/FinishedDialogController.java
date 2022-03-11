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
package de.nuton.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import de.nuton.diagrams.DiagramsController;
import de.nuton.io.Exporter;
import de.nuton.settings.MotionSettings;
import de.nuton.states.Motion;
import de.nuton.userSettings.ThemeLoader;
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

public class FinishedDialogController {
	@FXML
	private Button showDiagramButton;
	@FXML
	private Button exportButton;
	private Dialog<ButtonType> dialog;
	private ButtonType btnType;
	private DiagramsController diaController;
	private ArrayList<Point> points;
	private Exporter exporter;
	private MainController mainController;
	private Motion motion;
	private MotionSettings settings;
	private Point[] calibrationPoints;

	//TODO: New Controller with a better name
	public FinishedDialogController(MainController mainController, ArrayList<Point> points, MotionSettings motionSettings, Motion motion, Point[] calibrationPoints) {
		try {
			this.mainController = mainController;
			this.points = points;
			this.motion = motion;
			this.settings = motionSettings;
			this.calibrationPoints = calibrationPoints;
			dialog = new Dialog<>();
			dialog.setTitle("Optionen für Daten");
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("/fxml/FertigDialog.fxml"));
			loader.setController(this);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			ThemeLoader themeLoader = new ThemeLoader();
			scene.getStylesheets().add(themeLoader.getTheme());
			Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/nutonLogo.png")));
			
			btnType = new ButtonType("Schließen", ButtonData.FINISH);
			dialog.getDialogPane().getButtonTypes().add(btnType);
			dialog.initOwner(MainFX.getStage());
			dialog.initModality(Modality.WINDOW_MODAL);
			
			dialog.getDialogPane().setContent(root);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void showDialog() {
		showDiagramButton.setOnAction(event -> {
			diaController = new DiagramsController(mainController, points, motion, settings, calibrationPoints);
			diaController.setPoints(points);
			diaController.calculateDiagrams();
			diaController.show();
		});
		exportButton.setOnAction(event -> {
			//exporter = new Exporter(points, pManager);
			exporter.exportData();
		});
		Optional<ButtonType> result = dialog.showAndWait();
		//TODO: WHAT IN THE FUCK IS THIS?!
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
