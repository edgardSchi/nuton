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
package savingFile;

import java.io.Serializable;
import java.util.ArrayList;

import application.MainController;
import settings.Settings;

public class SaveFile implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2451581485648747317L;
	private int stateID;
	private Settings settings;
	private ArrayList<SerializablePoint> sPoints;
	private SerializablePoint[] calibratePoints;
	private SerializablePoint origin;
	private double sliderPos;
	private String videoURL;
	private boolean withFfmpeg;
	
	public SaveFile(MainController mainController, ArrayList<SerializablePoint> points, SerializablePoint[] calibratePoints, SerializablePoint origin) {
		this.calibratePoints = calibratePoints;
		sPoints = points;
		settings = mainController.getSettings();
		stateID = mainController.getStateManager().getCurrentStateID();
		sliderPos = mainController.getSlider().getValue();
		videoURL = TempSaving.getVideoURL();
		withFfmpeg = TempSaving.isFfmpeg();
		this.origin = origin; 
	}

	
	
	public SerializablePoint getOrigin() {
		return origin;
	}

	public SerializablePoint[] getCalibratePoints() {
		return calibratePoints;
	}
	
	public int getStateID() {
		return stateID;
	}


	public Settings getSettings() {
		return settings;
	}


	public ArrayList<SerializablePoint> getsPoints() {
		return sPoints;
	}


	public double getSliderPos() {
		return sliderPos;
	}


	public String getVideoURL() {
		return videoURL;
	}


	public boolean isWithFfmpeg() {
		return withFfmpeg;
	}


	@Override
	public String toString() {
		return "SaveFile [state=" + stateID + ", settings=" + settings + ", points=" + sPoints + ", sliderPos=" + sliderPos
				+ ", videoURL=" + videoURL + ", withFfmpeg=" + withFfmpeg + "]";
	}
	
	

}
