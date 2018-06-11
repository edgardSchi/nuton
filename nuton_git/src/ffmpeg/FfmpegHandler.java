package ffmpeg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.StageStyle;
import properties.PropertiesReader;

public class FfmpegHandler {

	Task<Void> task;

	private PropertiesReader propReader;
	private File outputFile;
	private String outputPath;
	private List<String> ffmpegArguments;

	private String videoPath;
	private String outputName;
	private String ffmpegPath;

	private boolean ffmpegStarted;
	private Alert alert;

	public FfmpegHandler(String videoPath, String outputPath, String outputName) {
		propReader = new PropertiesReader();
		outputName = genFileName(outputName);
		ffmpegArguments = new ArrayList<String>();
		ffmpegPath = propReader.getFfmpegPath();
		File logFolder = new File(System.getProperty("user.home") + "/.nuton/ffmpeg_log");
		if (!logFolder.exists()) {
			createLogFolder();
		}
		if (outputPath == null) {
			if (propReader.getFfmpegSameOutputPath().isEmpty()) {
				outputPath = logFolder.getAbsolutePath();
			} else {
				outputPath = propReader.getFfmpegSameOutputPath();
			}
			outputPath = outputPath.replaceAll("\\\\", "/");
		}
		this.outputPath = outputPath;
		this.videoPath = videoPath;
		this.outputName = outputName;

		ffmpegPath = ffmpegPath.replaceAll("\\\\", "/");
		System.out.println(">" + ffmpegPath + "<");
		try {
			File[] files = logFolder.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			System.out.println("\"" + videoPath + "\"");

			loadFfmpegArguments();
			ProcessBuilder pb = new ProcessBuilder(ffmpegArguments);
			pb.redirectErrorStream(true);

			for (String s : pb.command()) {
				System.out.println(s);
			}
			
			Process process = pb.start();

			InputStream inputStream = process.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);
			String line = null;
			StringBuilder s = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				s.append(line);
				s.append(System.lineSeparator());
			}
			process.waitFor();
			writeLogFile(s.toString());
			outputFile = new File(outputPath + "/" + outputName + "." + propReader.getPrefVideoFormat());

			System.out.println("\"" + outputPath + "/" + outputName + "." + propReader.getPrefVideoFormat() + "\"");
			ffmpegStarted = true;
			process.destroy();
		} catch (Exception e) {
			e.printStackTrace();

			createErrorDialog(e);
		}
	}

	public File getVideo() {
		return outputFile;
	}

	private void createAlert() {
		alert = new Alert(Alert.AlertType.NONE);
		alert.setHeaderText("Ffmpeg verabeitet das Video.");
		alert.setContentText("Das Video wird importiert.");
		alert.initStyle(StageStyle.TRANSPARENT);
		alert.show();

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

	private void loadFfmpegArguments() {
		ffmpegArguments.clear();
		propReader.update();
		ffmpegArguments.add(ffmpegPath + "/ffmpeg");
		if (propReader.getFfmpegArguments()[0].isEmpty()) {
			ffmpegArguments.add("-i");
			ffmpegArguments.add(videoPath);
			ffmpegArguments.add(outputPath + "/" + outputName + "." + propReader.getPrefVideoFormat());
		} else {
			ffmpegArguments.add("-i");
			ffmpegArguments.add(videoPath);
			ffmpegArguments.addAll(Arrays.asList(propReader.getFfmpegArguments()));
			ffmpegArguments.add(outputPath + "/" + outputName + "." + propReader.getPrefVideoFormat());
		}
	}

	private void createLogFolder() {
		boolean success = (new File(System.getProperty("user.home") + "/.nuton/ffmpeg_log")).mkdir();
		if (success) {
			System.out.println("Directory created!");
		}

	}

	public boolean getffmpegStarted() {
		return ffmpegStarted;
	}

	private void createErrorDialog(Exception e) {
		ffmpegStarted = false;
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Es ist ein Fehler aufgetreten!");
		alert.setHeaderText("Ffmpeg konnte nicht ausgeführt werden.");
		alert.setContentText(
				"Überprüfen Sie den Pfad zu ffmpeg. Weitere Informationen finden Sie in der Fehlermeldung.");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Die Fehlermeldung:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
}
