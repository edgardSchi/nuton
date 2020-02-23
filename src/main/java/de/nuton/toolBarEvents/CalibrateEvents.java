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
package de.nuton.toolBarEvents;

import de.nuton.application.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CalibrateEvents {

	private static int clickCounter = 0;
	private static double x1;
	private static double x2;
	private static double y1;
	private static double y2;
	
	
	public static void calibrate(MouseEvent e, MainController mainController) {
		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
			mainController.getGc().setFill(Color.rgb(255, 119, 0, 0.80));
			mainController.getGc().fillRect(e.getX() - 5, e.getY() - 5, 10, 10);
			
			if (clickCounter == 0) {
				x1 = e.getX();
				y1 = e.getY();
			}
			
			if (clickCounter == 1) {
				x2 = e.getX();
				y2 = e.getY();
			}
			
			clickCounter++;
			
			if (clickCounter == 2) {
				mainController.getGc().setStroke(Color.RED);
				mainController.getGc().strokeLine(x1, y1, x2, y2);
				
				TextInputDialog dialog = createCalibWindow((int)mainController.getSettings().getEichung(), "Distanz für folgenen Wert speichern? (cm):");
			}
		}
	}
	
	public static TextInputDialog createCalibWindow(int eichung, String contextText) {
		TextInputDialog dialog = new TextInputDialog("" + eichung);
		dialog.setTitle("Bestätigen");
		dialog.setHeaderText(null);
		dialog.setContentText(contextText);
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(CalibrateEvents.class.getClassLoader().getResourceAsStream("/nutonLogo.png")));
		
		dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int i = 1;
				if (!newValue.matches("\\d*")) {
					dialog.getEditor().setText(newValue.replaceAll("[^\\d]", "1"));
				} else {
					if (!newValue.isEmpty()) {
						i = Integer.parseInt(newValue);
					}						
				}
								
				if (i > 1000) {
					dialog.getEditor().setText(newValue.replaceAll(newValue, "10000"));
				}
				
				if (newValue.isEmpty()) {
					dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
				} else {
					dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
				}
			}
			
		});
		return dialog;
	}
	
}
