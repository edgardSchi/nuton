/*******************************************************************************
 * Nuton
 * Copyright (C) 2018 Edgard Schiebelbein
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package states;

import application.MainController;
import application.Point;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public abstract class CalibrateState extends State{

	protected GraphicsContext gc;
	protected Point[] calibratePoints;
	protected int clickCounter;
	
	public CalibrateState(MainController mainController) {
		super(mainController);
		clickCounter = 0;
		gc = mainController.getGc();
		mainController.getFertigBtn().setDisable(true);
		calibratePoints = new Point[2];
	}
	
	public void resetSlider() {
		mainController.getSlider().setSnapToTicks(false);
		mainController.getSlider().setValue(0);
	}
	
	public void setPointAmount(int n) {
		calibratePoints = new Point[n];
	}
	
	protected void addPointByMouse(MouseEvent e) {
		calibratePoints[clickCounter] = new Point((int)e.getX(), (int)e.getY(), -1);
		clickCounter++;
	}
	
	protected TextInputDialog createDialog(String contentText, int lowerBound, int upperBound) {
		TextInputDialog dialog = new TextInputDialog("" + (int)mainController.getSettings().getEichung());
		dialog.setTitle("Bestätigen");
		dialog.setHeaderText(null);
		dialog.setContentText(contentText);
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/nutonLogo.png")));
		
		dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int i = 1;
				if(!newValue.matches("\\d*")) {
					dialog.getEditor().setText(newValue.replaceAll("[^\\d]", "1"));
				} else {
					if (!newValue.isEmpty()) {
						i = Integer.parseInt(newValue);
					}
				}
				
				if(upperBound != 0) {
					if (i > upperBound) {
						dialog.getEditor().setText(newValue.replaceAll(newValue, Integer.toString(upperBound)));
					}
				} else {
					if (i > Integer.MAX_VALUE) {
						dialog.getEditor().setText(newValue.replaceAll(newValue, Integer.toString(Integer.MAX_VALUE)));
					}
				}
			
				if (i < lowerBound) {
					dialog.getEditor().setText(newValue.replaceAll(newValue, Integer.toString(lowerBound)));				
				}
				
				if(newValue.isEmpty()) {
					dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
				} else {
					dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
				}
			}
			
		});
		return dialog;
	}
	
	protected void resetCalibrate() {
		gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
		clickCounter = 0;
	}

}
