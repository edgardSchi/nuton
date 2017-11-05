package savingFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
	
	public void write() {
		try {
			this.points = mainController.getStateManager().getCurrentState().getPoints();
			if (TempSaving.isAlreadySaved() && !saveAs) {
				file = new File(TempSaving.getSavingPath());
			} else {
				initFileChooser();
			}
			if (file != null) {
				PrintWriter writer = new PrintWriter(file);
				writer.println(booleanConverter(TempSaving.isFfmpeg()));
				writer.println(TempSaving.getVideoURL());
				writer.println("00");
				writer.println("0");
				writer.println(settings.getSchrittweite());
				writer.println(settings.getEichung());
				writer.println(settings.getDirection());
				writer.println(settings.getxNull());
				writer.println(settings.getyNull());
				writer.println(booleanConverter(settings.isxFixed()));
				writer.println(booleanConverter(settings.isyFixed()));
				writer.println("00");
				writer.println(mainController.getSlider().getValue());
				writer.println(mainController.getPManager().getLAENGEPIXEL());
				writer.println("00");
				if (points != null) {
					for (Point p : points) {
						writer.println(p.saveString());
					}
					System.out.println("Points Size from saveHandler: " + points.size());
				}
				writer.close();
				TempSaving.setAlreadySaved(true);
				TempSaving.setSavingPath(file.getAbsolutePath());
				mainController.getSaveFileMenu().setDisable(false);
			}
			saveAs = false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
