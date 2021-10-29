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
package de.nuton.states.translation;

import de.nuton.draw.VideoPainter;
import de.nuton.application.FertigDialogController;
import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.math.Vector2;
import de.nuton.savingFile.TempSaving;
import de.nuton.settings.TranslationSettings;
import de.nuton.states.Motion;
import de.nuton.states.PointState;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class TranslationState extends PointState {

	private TranslationSettings settings;
	private Point[] calibrationPoints;

	public TranslationState(MainController mainController) {
		super(mainController);
	}

	@Override
	public void init() {
		this.defaultInit();
		setHelpLabel();

		try {
			settings = (TranslationSettings) getStateData("settings");
			calibrationPoints = (Point[]) getStateData("calibrationPoints");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(MouseEvent e) {
		this.defaultOnClick(e);
	}

	@Override
	public void keyPressed(int k) {

		
	}

	@Override
	public void keyReleased(int k) {

		
	}
	
	public void reset() {
		this.defaultReset();
	}
	
	@Override
	public void fertigBtnClick() {
		if (getPoints().size() < 2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Bitte wählen Sie mindestens zwei Punke aus.");
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					alert.close();
				}
			});
		} else {
			//TODO: Give collected values for diagrams
			FertigDialogController fController = new FertigDialogController(mainController, null, (ArrayList<Point>) getPoints(), Motion.TRANSLATION);
			fController.showDialog();
		}
	}

	@Override
	public void redraw() {
		VideoPainter.getInstance().clearScreen();
		if(TempSaving.isShowPoints()) {
			for(Point p : getPoints()) {
				VideoPainter.getInstance().drawPoint(p);
			}
		}
		if(TempSaving.isShowDistance()) {
			//TODO: After fixing settings
			VideoPainter.getInstance().drawCalibrationDistance(calibrationPoints[0], calibrationPoints[1]/*, Double.toString(settings.getCalibration()).concat(" cm")*/);
		}
	}

	@Override
	public void onKill() {

		
	}

	private void setHelpLabel() {
		mainController.setHelpLabel("Punkte anklicken");
	}
	
	@Override
	public void onUnpause() {
		mainController.getSlider().setSnapToTicks(true);
		//TODO: Fix slider thingy
		//mainController.getSlider().setValue(slider.getValue());
		redraw();
		mainController.getSlider().setSnapToTicks(true);
		setHelpLabel();
	}
	

}
