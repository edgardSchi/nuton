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
package de.nuton.savingFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Optional;

import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.application.ScalingManager;
import de.nuton.application.settingsPane.SettingsController;
import de.nuton.settings.Settings;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.media.MediaPlayer;

//TODO: New load handler
public class LoadHandler {

	private MainController mainController;
	private Settings settings;
	private boolean importedVideo = false;
	private ArrayList<Point> points;
	
	public LoadHandler(MainController mainController, Settings settings) {
		this.mainController = mainController;
		this.settings = settings;
		points = new ArrayList<Point>();
	}
	/*public void load(File file) {
		try {
			FileInputStream fi = new FileInputStream(file);
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			SaveFile saveFile = (SaveFile) oi.readObject();
			
			fi.close();
			oi.close();
			
			mainController.reset();
			applySettings(saveFile);
			
			
			mainController.redraw();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
		}
	}*/
	
	private boolean errorDialog(String path) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Video nicht gefunden!");
		alert.setContentText("Das Video konnte nicht gefunden werden. Stellen Sie sicher, dass es diesen Pfad hat: " + path);
		alert.setHeaderText(null);
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				alert.close();
			}
		});
		return true;
		// >> Muss unbedingt gefixed werden! <<
//		Alert alert2 = new Alert(AlertType.CONFIRMATION);
//		alert2.setTitle("Video öffnen?");
//		alert2.setContentText("Möchten Sie das Video manuell öffnen?");
//		alert2.setHeaderText(null);
//		Optional<ButtonType> result = alert2.showAndWait();
//		if (result.get() == ButtonType.OK){
//			loadVideo("");
//			return true;
//		}
//		alert2.close();
//		return false;
	}

	/*private void applySettings(SaveFile saveFile) {
		TempSaving.withFfmpeg(saveFile.isWithFfmpeg());
		File video = new File(saveFile.getVideoURL());
		if(!video.exists()) {
			if(!errorDialog(video.getAbsolutePath())) {
				return;
			}
			if(mainController.getPlayer() == null) {
				return;
			}
		} else {
			loadVideo(saveFile.getVideoURL());
		}
			
		mainController.getSettings().overloadSettings(saveFile.getSettings());
		if(saveFile.getOrigin() != null) {
			mainController.getPManager().setOrigin(convertOrigin(saveFile.getOrigin()));
		}	
		mainController.getPManager().setCalibratePoints(convertCalibratePoints(saveFile.getCalibratePoints()));
		mainController.getStateManager().setState(saveFile.getStateID());
		mainController.getStateManager().getCurrentState().setCalibratePoints(convertCalibratePoints(saveFile.getCalibratePoints()));
		mainController.getStateManager().getCurrentState().setPoints(convertPoints(saveFile.getsPoints()));
		mainController.updateLists();
		mainController.getStartBtn().setDisable(true);
		mainController.getRestartBtn().setDisable(false);
		//mainController.getScalingManager().setCanvasDimension();
		mainController.getSlider().setMinorTickCount(0);
		mainController.getSlider().setMajorTickUnit(mainController.getSettings().getSchrittweite());
		mainController.getSlider().setSnapToTicks(true);
		
		mainController.getSlider().setMax(SettingsController.calcMaxSlider(saveFile.getSettings().getSchrittweite(), mainController.getPlayer().getTotalDuration().toMillis()));
		ScalingManager.getInstance().setMediaDimension(mainController.getPlayer().getMedia().getWidth(), mainController.getPlayer().getMedia().getHeight());
		ScalingManager.getInstance().setCanvasDimension(mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
		updatePoints();
		TempSaving.setURL(saveFile.getVideoURL());
		System.out.println("LOADED");
		System.out.println(mainController.getSettings());
	}*/

	private void loadVideo(String path) {
		File video;
		if(TempSaving.isFfmpeg()) {
			if(path.equals("")) {
				mainController.openVideoWithFfmpegDialog();
			} else {
				video = new File(path);
				mainController.loadVideoWithFfmpeg(video);
			}
		} else {
			if(path.equals("")) {
				//Wieso ist das ohne reset?
				//mainController.getMainEventHandler().openMediaDialogWithoutReset();
				throw new Error("Das sollte nicht passieren, hier wurde was vergessen!");
			} else {
				video = new File(path);
				mainController.openMedia(video);
			}
			
		}
	}

/*	private Point[] convertCalibratePoints(SerializablePoint[] sPoints) {
		Point[] points = new Point[2];
		for(int i = 0; i < sPoints.length; i++) {
			points[i] = new Point(sPoints[i].getX(), sPoints[i].getY(), 0);
			points[i].setNormX(sPoints[i].getNormX());
			points[i].setNormY(sPoints[i].getNormY());
			//System.out.println(points[i]);
		}
		return points;
	}*/


/*	private ArrayList<Point> convertPoints(ArrayList<SerializablePoint> sPoints) {
		ArrayList<Point> points = new ArrayList<Point>();
		ScalingManager.getInstance().setMediaDimension(mainController.getPlayer().getMedia().getWidth(), mainController.getPlayer().getMedia().getHeight());
		for(SerializablePoint sPoint : sPoints) {
			Point p = new Point(sPoint.getX(), sPoint.getY(), sPoint.getTime());
			p.setNormCord(sPoint.getNormX(), sPoint.getNormY());
			p.setDrawX(sPoint.getDrawX());
			p.setDrawY(sPoint.getDrawY());
			p.setEntfernungMeterX(sPoint.getEntfernungMeterX());
			p.setEntfernungMeterY(sPoint.getEntfernungMeterY());
			p.setMediaX(sPoint.getX());
			p.setMediaY(sPoint.getY());
			ScalingManager.getInstance().updatePointPos(p);
			points.add(p);
			System.out.println(p.toString());
		}
		return points;
	}*/


/*	private Point convertOrigin(SerializablePoint sPoint) {
		Point o = new Point(sPoint.getX(), sPoint.getY(), sPoint.getTime());
		o.setNormX(sPoint.getNormX());
		o.setNormY(sPoint.getNormY());
		o.setMediaX(sPoint.getMediaX());
		o.setMediaY(sPoint.getMediaY());
		ScalingManager.getInstance().updatePointPos(o);
		return o;
	}*/


/*	private void updatePoints() {
		for(Point p : mainController.getStateManager().getPoints()) {
			ScalingManager.getInstance().updatePointPos(p);
		}
		for(Point p : mainController.getStateManager().getCurrentState().getCalibratePoints()) {
			ScalingManager.getInstance().updatePointPos(p);
		}
		if(mainController.getPManager().getOrigin() != null) {
			ScalingManager.getInstance().updatePointPos(mainController.getPManager().getOrigin());
		}
	}*/
}
