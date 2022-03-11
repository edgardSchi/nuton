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
package de.nuton.states.circl;

import de.nuton.draw.VideoPainter;
import de.nuton.application.FinishedDialogController;
import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.savingFile.TempSaving;
import de.nuton.settings.CircularSettings;
import de.nuton.settings.TranslationSettings;
import de.nuton.states.Motion;
import de.nuton.states.PointState;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class CircularState extends PointState {

	private Point origin;
	private Point[] calibrationPoints;
	private CircularSettings settings;
	
	public CircularState(MainController mainController) {
		super(mainController);
	}

	@Override
	public void init() {
		defaultInit();
		onUnpause();
		try {
			settings = (CircularSettings) getStateData("settings");
			origin = (Point) getStateData("calibrationPoint");
			calibrationPoints = (Point[]) getStateData("calibrationPoints");
			System.out.println("Origin Punkt: " + origin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(MouseEvent e) {
		defaultOnClick(e);
		this.redraw();
	}

	@Override
	public void keyPressed(int k) {

		
	}

	@Override
	public void keyReleased(int k) {

		
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
			FinishedDialogController fController = new FinishedDialogController(mainController, (ArrayList<Point>) getPoints(), settings, Motion.CIRCULAR, calibrationPoints);
			fController.showDialog();
		}
	}

	@Override
	public void reset() {
		defaultReset();
	}
	
	private void drawVector(Point p) {
		VideoPainter.getInstance().drawDistance(origin, p);
	}

	@Override
	public void redraw() {
		VideoPainter.getInstance().clearScreen();
		if(TempSaving.isShowPoints()) {
			if (origin != null) {
				VideoPainter.getInstance().drawPoint(origin);
			}
			for(Point p : getPoints()) {
				VideoPainter.getInstance().drawPoint(p);
				drawVector(p);
			}
		}
		if(TempSaving.isShowDistance()) {
			VideoPainter.getInstance().drawDistance(calibrationPoints[0], calibrationPoints[1]/*Double.toString(settings.getEichung()).concat(" cm")*/);
		}
	}
	
	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	@Override
	public void onKill() {

		
	}

	@Override
	public void onUnpause() {
		redraw();
		mainController.setHelpLabel("Punkte anklicken");
	}

}
