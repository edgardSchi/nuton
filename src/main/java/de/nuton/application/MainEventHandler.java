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
package de.nuton.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.nuton.ffmpeg.FfmpegHandler;
import de.nuton.properties.PropertiesReader;
import de.nuton.properties.PropertiesWriter;
import de.nuton.states.StateManager;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainEventHandler {

	private MainController mainController;
	private PropertiesReader propReader;
	private PropertiesWriter propWriter;
	private ScalingManager scalingManager;

	public MainEventHandler(MainController mainController) {
		this.mainController = mainController;
		propWriter = new PropertiesWriter();
		scalingManager = mainController.getScalingManager();
	}

	public void openMedia(File media) {
		reset();
		if (media != null) {
			mainController.setPlayer(new MediaPlayer(new Media(media.toURI().toString())));
			mainController.getPlayer().setOnReady(new Runnable() {

				@Override
				public void run() {
					// slider.setMin(0);
					mainController.getMv().setPreserveRatio(true);
					DoubleProperty mvw = mainController.getMv().fitWidthProperty();
					DoubleProperty mvh = mainController.getMv().fitHeightProperty();
					DoubleProperty canvasX = mainController.getCanvas().layoutXProperty();
					DoubleProperty canvasW = mainController.getCanvas().widthProperty();
					DoubleProperty canvasH = mainController.getCanvas().heightProperty();

					mvw.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "width"));
					mvh.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "height"));
					canvasW.bind(Bindings.selectDouble(mainController.getMv().boundsInParentProperty(), "width"));
					canvasH.bind(Bindings.selectDouble(mainController.getMv().boundsInParentProperty(), "height"));

					System.out.println("MEDIA READY");

					mainController.getSlider().setMinorTickCount(0);
					mainController.getSlider().setMajorTickUnit(1000);
					mainController.getSlider().setMax(mainController.getPlayer().getTotalDuration().toMillis());
					mainController.setMediaLength(mainController.getPlayer().getTotalDuration().toMillis());

					scalingManager.setMediaDimension();
					scalingManager.setCanvasDimension();
				}

			});
			mainController.getMv().setMediaPlayer(mainController.getPlayer());
			mainController.getPlayer().setMute(true);

			mainController.getSettingsController().reset();
			mainController.reset();
			mainController.getStartBtn().setDisable(false);
			// mainController.getSettingsController().showDialog();
		} else {
			System.out.println("MEDIA NULL");
		}

	}

	public void openFileDialog() {
		propReader = new PropertiesReader();
		propWriter = new PropertiesWriter();
		FileChooser fileChooser = new FileChooser();

		if (!propReader.getLastPath().isEmpty()) {
			if (new File(propReader.getLastPath()).isDirectory()) {
				fileChooser.setInitialDirectory(new File(propReader.getLastPath()));
			} else {
				propWriter.setLastPath("");
			}
		}

		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
				"Video Dateien (*.fxm), (*.flv), (*.mp4), (*.m4v)", "*.fxm", "*.flv", "*.mp4", "*.m4v");
		fileChooser.getExtensionFilters().add(filter);
		File mediaFile = fileChooser.showOpenDialog(MainFX.getStage());

		if (mediaFile != null) {
			openMedia(mediaFile);
			System.out.println(mediaFile.getAbsolutePath());
			propWriter.setLastPath(mediaFile.getParent());
			propWriter.confirm();
			de.nuton.savingFile.TempSaving.setURL(mediaFile.getAbsolutePath());
			de.nuton.savingFile.TempSaving.withFfmpeg(false);
		}
	}

	public void ffmpegOpenFileDialog() {
		propReader = new PropertiesReader();
		propWriter = new PropertiesWriter();
		FileChooser chooser = new FileChooser();

		if (!propReader.getLastPath().isEmpty()) {
			if (new File(propReader.getLastPath()).exists()) {
				chooser.setInitialDirectory(new File(propReader.getLastPath()));
			} else {
				propWriter.setLastPath("");
			}
		}

		File video;
		video = chooser.showOpenDialog(MainFX.getStage());

		loadMediaWithFfmpeg(video);

	}

	public boolean loadMediaWithFfmpeg(File video) {
		propReader = new PropertiesReader();
		Alert waitAlert = new Alert(Alert.AlertType.NONE);
		waitAlert.setHeaderText("Ffmpeg verabeitet das Video.");
		waitAlert.setContentText("Das Video wird importiert.");
		waitAlert.initStyle(StageStyle.TRANSPARENT);
		String videoPath = "";
		if (video != null) {
			videoPath = video.getAbsolutePath();
			String outputPath = null;
			if (propReader.isFfmpegOutputSame()) {
				outputPath = propReader.getFfmpegSameOutputPath();
			}
			String name = "output";
			
			FfmpegHandler handler = new FfmpegHandler(videoPath, outputPath, name);

			if (!handler.getffmpegStarted()) {
				System.out.println("ffmpeg nicht gefunden");
				return false;
			}

			if (!handler.getVideo().exists()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Video nicht gefunden");
				alert.setHeaderText("Ein Fehler ist aufgetreten!");
				alert.setContentText(
						"Das Video konnte nicht gefunden werden. Überprüfen Sie die Log-Datei für weitere Informationen.");

				alert.showAndWait();
				return false;
			} else {
				propWriter.setLastPath(video.getParent());
				propWriter.confirm();
				openMedia(handler.getVideo());
				de.nuton.savingFile.TempSaving.setURL(video.getAbsolutePath());
				de.nuton.savingFile.TempSaving.withFfmpeg(true);

			}
		}
		waitAlert.close();
		waitAlert.hide();
		return true;
	}

	public void openMediaDialogWithoutReset() {
		propReader = new PropertiesReader();
		FileChooser fileChooser = new FileChooser();

		if (!propReader.getLastPath().isEmpty()) {
			if (new File(propReader.getLastPath()).isDirectory()) {
				fileChooser.setInitialDirectory(new File(propReader.getLastPath()));
			} else {
				propWriter.setLastPath("");
			}
		}

		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
				"Video Dateien (*.fxm), (*.flv), (*.mp4), (*.m4v)", "*.fxm", "*.flv", "*.mp4", "*.m4v");
		fileChooser.getExtensionFilters().add(filter);
		File mediaFile = fileChooser.showOpenDialog(MainFX.getStage());

		if (mediaFile != null) {
			mainController.setPlayer(new MediaPlayer(new Media(mediaFile.toURI().toString())));
			mainController.getPlayer().setOnReady(new Runnable() {

				@Override
				public void run() {
					// slider.setMin(0);
					DoubleProperty mvw = mainController.getMv().fitWidthProperty();
					DoubleProperty mvh = mainController.getMv().fitHeightProperty();
					DoubleProperty canvasW = mainController.getCanvas().widthProperty();
					DoubleProperty canvasH = mainController.getCanvas().heightProperty();

					mvw.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "width"));
					mvh.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "height"));
					canvasW.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "width"));
					canvasH.bind(Bindings.selectDouble(mainController.getMv().parentProperty(), "height"));

					mainController.getSlider().setMinorTickCount(0);
					mainController.getSlider().setMajorTickUnit(1000);
					mainController.getSlider().setMax(mainController.getPlayer().getTotalDuration().toMillis());
					mainController.setMediaLength(mainController.getPlayer().getTotalDuration().toMillis());

					System.out.println("Running");
				}

			});
			mainController.getMv().setMediaPlayer(mainController.getPlayer());
			mainController.getPlayer().setMute(true);

			mainController.getSettingsController().reset();
			mainController.reset();
			mainController.getStartBtn().setDisable(false);
			// mainController.getSettingsController().showDialog();
		}

		System.out.println(mediaFile.getAbsolutePath());
		propWriter.setLastPath(mediaFile.getParent());
		propWriter.confirm();
		de.nuton.savingFile.TempSaving.setURL(mediaFile.getAbsolutePath());
		de.nuton.savingFile.TempSaving.withFfmpeg(false);
	}

	public void openAnleitungDialog() {
		try {
			Parent loader = FXMLLoader.load(getClass().getResource("Anleitung.fxml"));
			Scene scene = new Scene(loader);
			Stage stage = new Stage();
			stage.setTitle("Anleitung");
			stage.setScene(scene);
			stage.getIcons().add(new Image(MainEventHandler.class.getResourceAsStream("/nutonLogo.png")));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void openAboutDialog() {
		try {
			Parent loader = FXMLLoader.load(getClass().getResource("About.fxml"));
			Scene scene = new Scene(loader);
			Stage stage = new Stage();
			stage.setTitle("Über Nuton");
			stage.setScene(scene);
			stage.getIcons().add(new Image(MainEventHandler.class.getResourceAsStream("/nutonLogo.png")));
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void closeProgram() {
		Platform.exit();
		System.exit(0);
	}

	public void openSettings() {
		if (mainController.getMedia() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Videodatei auswählen!");
			alert.setContentText("Wählen Sie zuerst eine geeignete Videodatei aus.");
			alert.setHeaderText(null);
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					alert.close();
				}
			});
		} else {
			mainController.getStartBtn().setDisable(true);
			mainController.getSettingsController().showDialog();
		}
	}

	public void reset() {
		mainController.getStartBtn().setDisable(false);
		mainController.getStateManager().getCurrentState().reset();
		mainController.getStateManager().setState(StateManager.DEFAULT);
		de.nuton.toolBarEvents.AddPointEvents.reset();
		mainController.updateLists();
	}

	public void backwardButton() {
		ArrayList<Point> points = mainController.getStateManager().getPoints();
		double schrittweite = mainController.getSettings().getSchrittweite();
		if (points != null) {
			double time = 0;
			if (points.size() - 1 > 0) {
				time = points.get(points.size() - 1).getTime();
			}

			if (time == mainController.getSlider().getValue() - schrittweite
					&& mainController.getSlider().getValue() - schrittweite >= 0) {
				mainController.getSlider().setValue(mainController.getSlider().getValue() - schrittweite);
			} else if (mainController.getSlider().getValue() - schrittweite >= 0) {
				mainController.getSlider().setValue(mainController.getSlider().getValue() - schrittweite);
			}
		} else if (mainController.getSlider().getValue() - schrittweite >= 0) {
			mainController.getSlider().setValue(mainController.getSlider().getValue() - schrittweite);
		}
	}

	public MainController getMainController() {
		return mainController;
	}

}
