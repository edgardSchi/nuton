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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.scene.paint.Color;

public class PropertiesReader {
	
	private static PropertiesReader instance;
	private Properties prop;
	private String userPath;
	private String propertiesPath;
	
	private PropertiesReader() {
		userPath = System.getProperty("user.home");
		propertiesPath = userPath + "/.nuton/Properties";
		prop = new Properties();
		try {
			InputStream reader = new FileInputStream(propertiesPath + "/user.properties");
			try {
				prop.load(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static PropertiesReader getInstance() {
		if (instance == null) instance = new PropertiesReader();
		return instance;
	}
	
	public void update() {
		prop = new Properties();
		try {
			InputStream reader = new FileInputStream(propertiesPath + "/user.properties");
			try {
				prop.load(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getFfmpegPath() {
		String path = prop.getProperty("ffmpegPath");
		return path;
	}
	
	public String getPrefVideoFormat() {
		String format = prop.getProperty("prefVideoFormat");
		return format;
	}
	
	public String getTheme() {
		String theme = prop.getProperty("theme");
		return theme;
	}
	
	public boolean isFfmpegOutputSame() {
		String b = prop.getProperty("ffmpegSameOutput");
		if (b.equals("true")) {
			return true;
		} else {
			return false;
		} 
	}
	
	public String getFfmpegSameOutput() {
		String s = prop.getProperty("ffmpegSameOutput");
		return s;
	}
	
	public String getFfmpegSameOutputPath() {
		String path = prop.getProperty("ffmpegSameOutputPath");
		return path;
	}
	
	public Color getPointColor() {
		Color c = Color.web(prop.getProperty("pointColor"));
		return c;
	}
	
	public String getLastPath() {
		String s = prop.getProperty("lastPath");
		return s;
	}
	
	public String[] getFfmpegArguments() {
		String[] s = prop.getProperty("ffmpegArguments").split(" ");
		return s;
	}

}
