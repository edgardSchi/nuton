package ffmpeg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import properties.PropertiesReader;

public class FfmpegHandler {

	private PropertiesReader propReader;
	private File outputFile;
	private String outputPath;
	
	public FfmpegHandler(String videoPath, String outputPath, String outputName) {
		propReader = new PropertiesReader();
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		outputName = genFileName(outputName);
		String ffmpegPath = propReader.getFfmpegPath();
		File logFolder = new File("ffmpeg_log");
		if (!logFolder.exists()) {
			createLogFolder();
		}
		if (outputPath == null) {
			outputPath = logFolder.getAbsolutePath();
			outputPath = outputPath.replaceAll("\\\\", "/");
		}
		this.outputPath = outputPath;
		
		ffmpegPath = ffmpegPath.replaceAll("\\\\", "/");
		System.out.println(">" + ffmpegPath + "<");
		
		if (isWindows) {
			try {
				File[] files = logFolder.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
				String[] command = {ffmpegPath + "/ffmpeg", "-i ", "\"" + videoPath + "\"", "\"" + outputPath + "/" + outputName + "." + propReader.getPrefVideoFormat() + "\""}; 
				System.out.println("\"" + videoPath + "\"");
				ProcessBuilder pb = new ProcessBuilder("cmd", "/c", ffmpegPath + "/ffmpeg -i \"" + videoPath + "\" \"" + outputPath +"/" + outputName + "." + propReader.getPrefVideoFormat() + "\"");
				pb.redirectErrorStream(true);
				
				Process process = pb.start();
				InputStream inputStream = process.getInputStream();
				
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);
				String line = null;
				StringBuilder s = new StringBuilder();
				while((line = bufferedReader.readLine()) != null) {
					s.append(line);
					s.append(System.lineSeparator());
				}
				process.waitFor();
				writeLogFile(s.toString());
				outputFile = new File(outputPath + "/" + outputName + "." + propReader.getPrefVideoFormat());
				
				System.out.println("\"" + outputPath + "/" + outputName + "." + propReader.getPrefVideoFormat() + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public File getVideo() {
		return outputFile;
	}
	
	private String genFileName(String outName) {
		Calendar date = Calendar.getInstance();
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DATE);
		int hour = date.get(Calendar.HOUR);
		int minute = date.get(Calendar.MINUTE);
		int second = date.get(Calendar.SECOND);
		String name = outName + "-" + hour + "-" + minute + "-" + second + "-" + day + "-" + month + "-" + year;  
		return name;
	}
	
	private void writeLogFile(String s) {
		BufferedWriter writer = null;
		try {
			File logFile = new File(outputPath + "/log.txt");
			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void createLogFolder() {
		boolean success = (new File("ffmpeg_log")).mkdir();
	    if (success) {
	      System.out.println("Directory created!");
	    }    

	}
	
}
