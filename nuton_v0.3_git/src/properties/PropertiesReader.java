package properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.scene.paint.Color;

public class PropertiesReader {
	
	Properties prop;
	
	public PropertiesReader() {
		prop = new Properties();
		try {
			InputStream reader = new FileInputStream("Properties/user.properties");
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

}
