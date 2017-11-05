package savingFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application.MainController;
import application.Point;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import settings.Settings;
import states.StateManager;

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
	
	public void loadFile(File file) {
		mainController.reset();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    int lineNr = 0;
		    while ((line = br.readLine()) != null) {
		    	switch (lineNr) {
		    		case 0: System.out.println(line);
		    				if (line.equals("0")) {
		    					importedVideo = false;
		    				} else {
		    					importedVideo = true;
		    				}
		    				break;
		    		case 1: if (importedVideo) {
		    				System.out.println("Video importiert");
		    				} else {
		    					File media = new File(line);
		    					if (!media.exists()) {
		    						errorDialog(media.getAbsolutePath());
		    					} else {
		    						mainController.getMainEventHandler().openMedia(media);
		    					}		    					
		    					System.out.println("Test");
		    				}
		    				break;
		    		case 3: if (line.equals("0")) {
		    					System.out.println("Translation");
		    				}
		    				break;
		    		case 4: settings.setSchrittweite(Double.parseDouble(line));
		    				break;
		    		case 5: settings.setEichung(Double.parseDouble(line));
		    				mainController.getPManager().setEichung(Double.parseDouble(line));
		    				break;
		    		case 6: settings.setDirection(Integer.parseInt(line));
		    				break;
		    		case 7: settings.setxNull(Integer.parseInt(line));
		    				break;
		    		case 8: settings.setyNull(Integer.parseInt(line));
		    				break;
		    		case 9: if (line.equals("1")) {
		    					settings.setxFixed(true);
		    				} else {
		    					settings.setxFixed(false);
		    				}
		    				break;
		    		case 10: if (line.equals("1")) {
		    					settings.setyFixed(true);
		    				} else {
		    					settings.setyFixed(false);
		    				}
		    				break;
		    	}
		    	mainController.getStateManager().setState(StateManager.TRANSLATION);
		    	points = mainController.getStateManager().getCurrentState().getPoints();
		    	mainController.getPManager().setLaengePixel(100);
		    	if (lineNr > 11) {
		    		String[] x = line.split(",");
		    		points.add(new Point(Integer.parseInt(x[0]), Integer.parseInt(x[1]), Double.parseDouble(x[2])));
		    		mainController.redraw();
		    		mainController.getListX().getItems().add(Integer.parseInt(x[0]));
					mainController.getListY().getItems().add(Integer.parseInt(x[1]));
		    		System.out.println("x: " + x[0]);
		    		System.out.println("y: " + x[1]);
		    		System.out.println("time: " + x[2]);
		    	}
		      lineNr++;
		    }
		    TempSaving.setAlreadySaved(true);
			TempSaving.setSavingPath(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void errorDialog(String path) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Video nicht gefunden!");
		alert.setContentText("Das Video konnte nicht gefunden werden. Stellen Sie sicher, dass es diesen Pfad hat: " + path);
		alert.setHeaderText(null);
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				alert.close();
			}
		});
		Alert alert2 = new Alert(AlertType.CONFIRMATION);
		alert2.setTitle("Video öffnen?");
		alert2.setContentText("Möchten Sie das Video manuell öffnen?");
		alert2.setHeaderText(null);
		Optional<ButtonType> result = alert2.showAndWait();
		if (result.get() == ButtonType.OK){
			System.out.println("OK");
			mainController.getMainEventHandler().openMediaDialogWithoutReset();
		} 
		alert2.close();
	}
	
}
