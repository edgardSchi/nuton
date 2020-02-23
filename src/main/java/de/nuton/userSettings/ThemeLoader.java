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
package de.nuton.userSettings;

import java.io.File;
import java.io.IOException;

import de.nuton.properties.PropertiesReader;

public class ThemeLoader {
	
	private File[] listOfFiles;
	private PropertiesReader propReader;
	private String path;
	private File defaultTheme;
	private File themesFolder;

	public ThemeLoader() {
		File folder = new File(System.getProperty("user.home") + "/.nuton/Themes");
		if (!folder.exists()) {
			folder.mkdir();
		}
		defaultTheme = new File(folder.getAbsolutePath() + "/Default.css");
		if (!defaultTheme.exists()) {
			try {
				defaultTheme.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
		    } 
		}
		path = folder.getAbsolutePath();
	}
	
	public String[] getFileNames() {
		String[] names = new String[listOfFiles.length];
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].getName().endsWith(".css")) {
				names[i] = listOfFiles[i].getName();
			}
		}
		return names;
	}
	
	public String getTheme() {
		propReader = new PropertiesReader();
		File pathToTheme = new File(path + "\\" + propReader.getTheme());
		return "file:///" + pathToTheme.getAbsolutePath().replace("\\", "/");
	}
	
}
