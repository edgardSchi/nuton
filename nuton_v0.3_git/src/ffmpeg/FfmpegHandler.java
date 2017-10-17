package ffmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import properties.PropertiesReader;

public class FfmpegHandler {

	private PropertiesReader propReader;
	private File outputFile;
	
	public FfmpegHandler(String videoPath, String outputPath, String outputName) {
		propReader = new PropertiesReader();
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		LocalDateTime date = LocalDateTime.now();
		outputName = outputName + "_" + date.getNano();
		String ffmpegPath = propReader.getFfmpegPath();
		File logFolder = new File("ffmpeg_log");
		if (outputPath == null) {
			outputPath = logFolder.getAbsolutePath();
			outputPath = outputPath.replaceAll("\\\\", "/");
		}
		
		ffmpegPath = ffmpegPath.replaceAll("\\\\", "/");
		System.out.println(">" + ffmpegPath + "<");
		
		if (isWindows) {
			try {
				File[] files = logFolder.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
				
				ProcessBuilder pb = new ProcessBuilder(ffmpegPath + "/ffmpeg", "-i \"" + videoPath + "\" \"" + outputPath +"/" + outputName + "." + propReader.getPrefVideoFormat() + "\"" );
				Process process = pb.start();
				InputStream inputStream = process.getErrorStream();
				
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);
                System.out.println(bufferedReader.read());
				
				//System.out.println(ffmpegPath + "/ffmpeg.exe -i \"" + videoPath + "\" \"" + outputPath +"/" + outputName + "." + propReader.getPrefVideoFormat() + "\"" );
				//Runtime.getRuntime().exec(ffmpegPath + "/ffmpeg -i \"" + videoPath + "\" \"" + outputPath +"/" + outputName + "." + propReader.getPrefVideoFormat() + "\"" );
				
				//System.out.println("Log: " + log);
				
//				Alert alert = new Alert(AlertType.ERROR);
//				alert.setTitle("Exception Dialog");
//				alert.setHeaderText("Look, an Exception Dialog");
//				alert.setContentText("Could not find file blabla.txt!");
//
//				Exception ex = new FileNotFoundException("Could not find file blabla.txt");
//
//				// Create expandable Exception.
//				StringWriter sw = new StringWriter();
//				PrintWriter pw = new PrintWriter(sw);
//				ex.printStackTrace(pw);
//				String exceptionText = sw.toString();
//
//				Label label = new Label("The exception stacktrace was:");
//
//				TextArea textArea = new TextArea(exceptionText);
//				textArea.setEditable(false);
//				textArea.setWrapText(true);
//
//				textArea.setMaxWidth(Double.MAX_VALUE);
//				textArea.setMaxHeight(Double.MAX_VALUE);
//				GridPane.setVgrow(textArea, Priority.ALWAYS);
//				GridPane.setHgrow(textArea, Priority.ALWAYS);
//
//				GridPane expContent = new GridPane();
//				expContent.setMaxWidth(Double.MAX_VALUE);
//				expContent.add(label, 0, 0);
//				expContent.add(textArea, 0, 1);
//
//				// Set expandable Exception into the dialog pane.
//				alert.getDialogPane().setExpandableContent(expContent);
//
//				alert.showAndWait();
                inputStream.close();
                bufferedReader.close();
				//outputFile = new File(outputPath + "/" + outputName + "." + propReader.getPrefVideoFormat());
				System.out.println("\"" + outputPath + "/" + outputName + "." + propReader.getPrefVideoFormat() + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public File getVideo() {
		return outputFile;
	}
	
	
}
