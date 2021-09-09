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
package de.nuton.properties;

import java.io.BufferedWriter;
import java.io.File;
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
	private Color pointColor;
	private String lastPath;
	private String userPath;
	private String propertiesPath;
	private String ffmpegArguments;

	
	public PropertiesWriter() {
		userPath = System.getProperty("user.home");
		propertiesPath = userPath + "/.nuton/Properties";
		checkFiles();
		reset();
	}
	
	public void reset() {
		prop = new Properties();
		try {
			InputStream reader = new FileInputStream(propertiesPath + "/user.properties");
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
		
		ffmpegPath = PropertiesReader.getInstance().getFfmpegPath();
		prefVideoFormat =  PropertiesReader.getInstance().getPrefVideoFormat();
		theme =  PropertiesReader.getInstance().getTheme();
		ffmpegSameOutput = PropertiesReader.getInstance().getFfmpegSameOutput();
		ffmpegSameOutputPath = PropertiesReader.getInstance().getFfmpegSameOutputPath();
		pointColor = PropertiesReader.getInstance().getPointColor();
		lastPath = PropertiesReader.getInstance().getLastPath();
		ffmpegArguments = convertFfmpegArguments(PropertiesReader.getInstance().getFfmpegArguments());
	}
	
	public void confirm() {
			prop.setProperty("ffmpegPath", ffmpegPath);
			prop.setProperty("prefVideoFormat", prefVideoFormat);
			prop.setProperty("theme", theme);
			prop.setProperty("ffmpegSameOutput", ffmpegSameOutput);
			prop.setProperty("ffmpegSameOutputPath", ffmpegSameOutputPath);
			prop.setProperty("pointColor", pointColor.toString());
			prop.setProperty("lastPath", lastPath);
			prop.setProperty("ffmpegArguments", ffmpegArguments);
			try {
				output = new FileOutputStream(propertiesPath + "/user.properties");
				prop.store(output, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Neuer Pfad: " + ffmpegPath);
	}
	
	public void checkFiles() {
		File file = new File(propertiesPath + "/user.properties");
		
		if (!file.exists()) {
			makeNewFile();
			System.out.println("Datei existiert jetzt");
		}
	}
	
	private void makeNewFile() {
		String pfad = propertiesPath;
		File folder = new File(propertiesPath);
		folder.mkdirs();
		java.nio.file.Path pfadDatei = Paths.get(pfad + "/user.properties");
		
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
			schreibPuffer.newLine();
			schreibPuffer.write("pointColor=0xff0000ff");
			schreibPuffer.newLine();
			schreibPuffer.write("ffmpegArguments=");
			System.out.println("Neue Config Datei erstellt.");
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	public String convertFfmpegArguments(String[] strings) {
		String merg = "";
		for(int i = 0; i < strings.length; i++) {
			merg += strings[i];
			if (i < strings.length-1) {
				merg += " ";
			}
			
		}
		return merg;
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
	
	public void setFfmpegArguments(String[] strings) {
		ffmpegArguments = convertFfmpegArguments(strings);
	}
	
}
