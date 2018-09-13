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
package states.circl;

import application.DrawHandler;
import application.FertigDialogController;
import application.MainController;
import application.Point;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import savingFile.TempSaving;
import settings.Settings;
import states.Motion;
import states.PointState;

public class CircState extends PointState{

	private Point origin;
	
	public CircState(MainController mainController) {
		super(mainController);
	}

	@Override
	public void init() {
		defaultInit();
		this.origin = pManager.getOrigin();
		redraw();
		mainController.setHelpLabel("Punkte anklicken");
	}

	@Override
	public void onClick(MouseEvent e) {
		defaultOnClick(e);
		this.redraw();
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fertigBtnClick() {
		if (points.size() < 2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Bitte wählen Sie mindestens zwei Punke aus.");
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					alert.close();
				}
			});
		} else {	
			pManager.calcPixelLength();
			pManager.setPoints(points);
			pManager.getSettings().setMotion(Settings.CIRCULAR);
			pManager.calcMeter(points);
			pManager.createVectors(points);
			pManager.checkVectorDirection();
			pManager.calcDeltaPhi();
			pManager.calcAngleVelo();
			pManager.calcCircVelo();
			pManager.calcCircFreq();
			FertigDialogController fController = new FertigDialogController(mainController, pManager, points, Motion.CIRCULAR);
			fController.showDialog();
		}
	}

	@Override
	public void reset() {
		defaultReset();
	}
	
	private void drawVector(Point p) {
		gc.strokeLine(origin.getDrawX(), origin.getDrawY(), p.getDrawX(), p.getDrawY());
	}

	@Override
	public void redraw() {
		gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
		if(TempSaving.isShowPoints()) {
			origin.drawPoint(gc);
			for(Point p : points) {
				p.drawPoint(gc);
				drawVector(p);
			}
		}
		if(TempSaving.isShowDistance()) {
			DrawHandler.drawDistance(gc, getCalibratePoints()[0], getCalibratePoints()[1], Double.toString(settings.getEichung()).concat(" cm"));
		}
	}
	
	public void setOrigin(Point origin) {
		this.origin = origin;
	}

}