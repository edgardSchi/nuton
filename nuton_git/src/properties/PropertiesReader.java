package properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.scene.paint.Color;

public class PropertiesReader {
	
	private Properties prop;
	private String userPath;
	private String propertiesPath;
	
	public PropertiesReader() {
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
