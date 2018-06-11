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
