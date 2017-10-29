package properties;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class PropertiesWriter {

	private Properties prop;
	
	private String ffmpegPath;
	private String prefVideoFormat;
	private String theme;
	private String ffmpegSameOutput;
	private String ffmpegSameOutputPath;
	private OutputStream output;
	private PropertiesReader propReader;
	private Color pointColor;
	private String lastPath;
	
	public PropertiesWriter() {
		propReader = new PropertiesReader();
		reset();
	}
	
	public void reset() {
		prop = new Properties();
		try {
			InputStream reader = new FileInputStream("Properties/user.properties");
			try {
				prop.load(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Datei nicht gefunden. Erstelle neue.");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fehlende Properties-Datei");
			alert.setHeaderText(null);
			alert.setContentText("Properties-Datei nicht gefunden. Neue Datei wird erstellt.");
			alert.showAndWait();
			makeNewFile();
		}
		
		ffmpegPath = propReader.getFfmpegPath();
		prefVideoFormat =  propReader.getPrefVideoFormat();
		theme =  propReader.getTheme();
		ffmpegSameOutput = propReader.getFfmpegSameOutput();
		ffmpegSameOutputPath = propReader.getFfmpegSameOutputPath();
		pointColor = propReader.getPointColor();
		lastPath = propReader.getLastPath();
	}
	
	public void confirm() {
//		if (ffmpegPath != "" && prefVideoFormat != "") {
			prop.setProperty("ffmpegPath", ffmpegPath);
			prop.setProperty("prefVideoFormat", prefVideoFormat);
			prop.setProperty("theme", theme);
			prop.setProperty("ffmpegSameOutput", ffmpegSameOutput);
			prop.setProperty("ffmpegSameOutputPath", ffmpegSameOutputPath);
			prop.setProperty("pointColor", pointColor.toString());
			prop.setProperty("lastPath", lastPath);
			try {
				output = new FileOutputStream("Properties/user.properties");
				prop.store(output, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Neuer Pfad: " + ffmpegPath);
//		}
		//System.out.println("Neue Config Datei erstellt.");
	}
	
	private void makeNewFile() {
		String pfad = "user.properties";
		java.nio.file.Path pfadDatei = Paths.get(pfad);
		
		try (BufferedWriter schreibPuffer = Files.newBufferedWriter(pfadDatei)) {
			schreibPuffer.write("ffmpegPath=");
			schreibPuffer.newLine();
			schreibPuffer.write("prefVideoFormat=mp4");
			schreibPuffer.newLine();
			schreibPuffer.write("ffmpegSameOutput=false");
			schreibPuffer.newLine();
			schreibPuffer.write("theme=Default.css");
			schreibPuffer.newLine();
			schreibPuffer.write("ffmpegSameOutputPath=");
			schreibPuffer.newLine();
			schreibPuffer.write("lastPath=");
			System.out.println("Neue Config Datei erstellt.");
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	public void setFfmpegPath(String path) {
		ffmpegPath = path;
	}
	
	public void setPrefVideoFormat(String format) {
		prefVideoFormat = format.substring(2);
	}
	
	public void setTheme(String name) {
		theme = name;
	}
	
	public void sameFfmpegOutput(boolean b) {
		if (b) {
			ffmpegSameOutput = "true";
		} else {
			ffmpegSameOutput = "false";
		}
	}
	
	public void setFfmpegSameOutputPath(String path) {
		ffmpegSameOutputPath = path;
	}
	
	public void setPointColor(String color) {
		pointColor = Color.web(color);
	}
	
	public void setLastPath(String path) {
		lastPath = path;
		System.out.println("Path set");
	}
	
}
