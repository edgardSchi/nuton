package userSettings;

import java.io.File;

import properties.PropertiesReader;

public class ThemeLoader {
	
	File[] listOfFiles;
	PropertiesReader propReader;

	public ThemeLoader() {
		File folder = new File("Themes");
		listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
		    } 
		}
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
		String path = "file:Themes/" + propReader.getTheme();
		return path;
	}
	
}
