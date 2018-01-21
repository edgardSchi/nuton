package userSettings;

import java.io.File;
import java.io.IOException;

import properties.PropertiesReader;

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
		System.out.println(pathToTheme.getAbsolutePath());
		return "file:///" + pathToTheme.getAbsolutePath().replace("\\", "/");
	}
	
}
