package savingFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import application.Main;
import application.MainController;
import application.Point;
import javafx.stage.FileChooser;
import settings.Settings;

public class SaveHandler {

	private File file;
	private String ffmpeg;
	private MainController mainController;
	private Settings settings;
	private ArrayList<Point> points;
	private FileChooser fileChooser;
	private boolean saveAs = false;
	
	public SaveHandler(MainController mainController) {
		this.mainController = mainController;
		settings = mainController.getSettings();
		this.points = mainController.getStateManager().getCurrentState().getPoints();
	}
	
	private String booleanConverter(boolean b) {
		if (b) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public void save() {
		try {
			SaveFile saveFile = new SaveFile(mainController, preparePoints(), prepareCalibratePoints(), prepareOrigin());
			if (TempSaving.isAlreadySaved() && !saveAs) {
				file = new File(TempSaving.getSavingPath());
			} else {
				initFileChooser();
			}
			if (file != null) {
				FileOutputStream f = new FileOutputStream(file);
				ObjectOutputStream o = new ObjectOutputStream(f);
				
				o.writeObject(saveFile);
				
				o.close();
				f.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private SerializablePoint[] prepareCalibratePoints() {
		SerializablePoint[] caliPoints = new SerializablePoint[2];
		caliPoints[0] = new SerializablePoint(mainController.getStateManager().getCurrentState().getCalibratePoints()[0]);
		caliPoints[1] = new SerializablePoint(mainController.getStateManager().getCurrentState().getCalibratePoints()[1]);
		return caliPoints;
	}
	
	private ArrayList<SerializablePoint> preparePoints() {
		ArrayList<SerializablePoint> sPoints = new ArrayList<SerializablePoint>();
		for (Point p : mainController.getStateManager().getPoints()) {
			sPoints.add(new SerializablePoint(p));
		}
		return sPoints;
	}
	
	private SerializablePoint prepareOrigin() {
		if(mainController.getPManager().getOrigin() != null) {
			SerializablePoint p = new SerializablePoint(mainController.getPManager().getOrigin());
			return p;
		} else {
			return null;
		}				
	}
	
	private void initFileChooser() {
		fileChooser = new FileChooser();
		fileChooser.setInitialFileName("Videoanalyse.ntn");
		FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("nuton Dateien (*.ntn)", "*.ntn");
		fileChooser.getExtensionFilters().add(exFilter);
		
		file = fileChooser.showSaveDialog(Main.getStage());
	}
	
	public void setSaveAs(boolean saveAs) {
		this.saveAs = saveAs;
	}
	
}
