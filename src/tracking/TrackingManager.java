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
package tracking;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import application.MainController;
import application.Point;
import ffmpeg.FfmpegHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import savingFile.TempSaving;
import toolBarEvents.AddPointEvents;
import userSettings.ThemeLoader;

public class TrackingManager extends Dialog<String> implements Runnable {
	
	private MainController mainController;
	private Kernel kernel;
	private int radius;
	
	private @FXML ImageView imageView;
	
	private @FXML Button backBtn;
	private @FXML Button forwardBtn;
	private @FXML Button calibrateBtn;
	private @FXML Button seekBtn;
	private @FXML ChoiceBox<String> pointBox;
	
	
	private Point calibratePoint;
	
	private KernelManager kernelManager;
	
	private boolean multiThreading = false;
	
	public TrackingManager(MainController mainController, ThemeLoader themeLoader, int radius, int stepsize, boolean multiThreading) {
		System.out.println("Neuer Tracking Manager");
		this.mainController = mainController;
		this.radius = radius;	
		this.multiThreading = multiThreading;
		if(multiThreading) {
			kernelManager = new KernelManager(radius, stepsize);
		} else {
			kernel = new Kernel(radius, stepsize);
		}
		
		calibratePoint = new Point(0, 0, 0);
		
		try {
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("TrackingManagerPane.fxml"));
			loader.setController(this);
			Node node = (Node) loader.load();
			Stage stage = (Stage) getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/nutonLogo.png")));
			getDialogPane().setContent(node);
			
			setTitle("Tracking");
			
			getDialogPane().getButtonTypes().addAll(ButtonType.APPLY);
			stage.getScene().getStylesheets().add(themeLoader.getTheme());
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		calibrateBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selectTrackingPoint(calibratePoint.getDrawX(), calibratePoint.getDrawY());			
			}
			
		});
		
		
		backBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainController.getSlider().setValue(mainController.getSlider().getValue() - mainController.getSettings().getSchrittweite());
			}
			
		});
		
		forwardBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainController.getSlider().setValue(mainController.getSlider().getValue() + mainController.getSettings().getSchrittweite());
			}
			
		});
		
		seekBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {				
				track();
			}
			
		});
		
		generateDarkImage();
		loadBox();
	}
	
	private void loadBox() {
		if(mainController.getStateManager().getPoints() != null) {
			for(Point p : mainController.getStateManager().getPoints()) {
				String t = "Punkt bei " + p.getTime() + " ms";
				pointBox.getItems().add(t);
			}
		
		pointBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				calibratePoint = mainController.getStateManager().getPoints().get((int)arg2);
				mainController.getSlider().setValue(calibratePoint.getTime());
			}
			
		});
		pointBox.getSelectionModel().selectFirst();
		}
	}
	
//	public void calibrateKernel(int width, int height) {
//		kernel = new Kernel(width, height);
//	}
	
	public void selectTrackingPointFfmpeg(int x, int y) {
		BufferedImage image = loadFrame(0);
		//kernel.calibrate(image, x, y);
		System.out.println(x + " " + y);
		mainController.getGc().setFill(Color.RED);
		mainController.getGc().fillRect(x-5, y-5, 10, 10);
		BufferedImage kernelBImage = image.getSubimage(x - radius, y - radius, radius + radius - 1 , radius + radius - 1);
		Image kernelImage = SwingFXUtils.toFXImage(kernelBImage, null);
		imageView.setImage(kernelImage);
	}

	public void trackFfmpeg() {
		double max = mainController.getSlider().getMax();
		double delta = mainController.getSettings().getSchrittweite();
		int frames = (int)(max / delta) - (int)(mainController.getStateManager().getPoints().get(0).getTime() / mainController.getSettings().getSchrittweite());
		for(int i = 1; i < frames; i++) {
			BufferedImage image = loadFrame(i);
			//int[] cords = kernel.feed(image);
//			mainController.getGc().fillRect(cords[0]-5, cords[1]-5, 10, 10);
//			double time = mainController.getStateManager().getPoints().get(0).getTime() + i * mainController.getSettings().getSchrittweite();
//			AddPointEvents.addTrackingPoint(mainController.getStateManager().getCurrentState(), null, cords[0], cords[1], time);
		}		
	}
	
	public void selectTrackingPoint(int x, int y) {
		double width = mainController.getMv().getFitWidth();
		double height = mainController.getMv().getFitHeight();
		WritableImage wImage = new WritableImage((int)width, (int)height);
		mainController.getMv().snapshot(null, wImage);
		BufferedImage image = SwingFXUtils.fromFXImage(wImage, null);
		if(multiThreading) {
			kernelManager.calibrate(image, x, y);
		} else {
			kernel.calibrate(image, x, y);
		}
		mainController.getGc().setFill(Color.RED);
		mainController.getGc().fillRect(x-5, y-5, 10, 10);
		BufferedImage kernelBImage = image.getSubimage(x - radius, y - radius, radius + radius - 1 , radius + radius - 1);
		Image kernelImage = SwingFXUtils.toFXImage(kernelBImage, null);
		imageView.setImage(kernelImage);
	}

	public void track() {
		for(Point p : mainController.getStateManager().getPoints()) {
			if(p.getTime() == mainController.getSlider().getValue()) {
				return;
			}
		}
		double width = mainController.getMv().getFitWidth();
		double height = mainController.getMv().getFitHeight();
		WritableImage wImage = new WritableImage((int)width, (int)height);
		mainController.getMv().snapshot(null, wImage);
		BufferedImage image = SwingFXUtils.fromFXImage(wImage, null);
		int[] cords;
		if(multiThreading ) {
			cords = kernelManager.runKernels(image);
		} else {
			cords = kernel.feed(image, 0);
		}
		mainController.getGc().fillRect(cords[0]-5, cords[1]-5, 10, 10);
		AddPointEvents.addPoint(mainController.getStateManager().getCurrentState(), null, cords[0], cords[1]);
		
	}
	
	public void track(Image frame) {
//		for(Point p : mainController.getStateManager().getPoints()) {
//			if(p.getTime() == mainController.getSlider().getValue()) {
//				return;
//			}
//		}
		
		
//		BufferedImage image = SwingFXUtils.fromFXImage(frame, null);
//		int[] cords = kernel.feed(image);
//		mainController.getScalingManager().setCanvasDimension();
//		int[] drawCords = mainController.getScalingManager().getCordRelativeToMedia(cords[0], cords[1]);
//		mainController.getGc().fillRect(drawCords[0]-5, drawCords[1]-5, 10, 10);
//		System.out.println(Arrays.toString(cords));
		
		
		//AddPointEvents.addPoint(mainController.getStateManager().getCurrentState(), null, cords[0], cords[1]);
	}
	
//	public void selectTrackingPoint(Image frame, int x, int y) {
//		BufferedImage image = SwingFXUtils.fromFXImage(frame, null);
//		kernel.calibrate(image, x, y);
//		mainController.getGc().setFill(Color.RED);
//		mainController.getGc().fillRect(x-5, y-5, 10, 10);
//		BufferedImage kernelBImage = image.getSubimage(x - radius, y - radius, radius + radius - 1 , radius + radius - 1);
//		Image kernelImage = SwingFXUtils.toFXImage(kernelBImage, null);
//		mainController.getCanvas().getGraphicsContext2D().drawImage(kernelImage, 0.0, 0.0);
//		imageView.setImage(kernelImage);
//	}
	
	private BufferedImage loadFrame(int n) {
		
		String userPath = System.getProperty("user.home") + "/.nuton/ffmpeg_log";
		File frame = new File(userPath + "/frame.png");
		if(frame.exists()) {
			frame.delete();
		}
		BufferedImage in = null;
		double time = mainController.getStateManager().getPoints().get(0).getTime() + n * mainController.getSettings().getSchrittweite();
		FfmpegHandler.getFrame("\"" + TempSaving.getVideoURL() + "\"", time);
		try {
			if(frame.exists()) {
				in = ImageIO.read(frame);
				int width;
				int height;
				System.out.println("Media Rotated " + mediaRotated());
				if(mediaRotated()) {
					width = mainController.getPlayer().getMedia().getHeight();
					height = mainController.getPlayer().getMedia().getWidth();
				} else {
					width = mainController.getPlayer().getMedia().getWidth();
					height = mainController.getPlayer().getMedia().getHeight();
				}
				BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);	
				Graphics2D g = newImage.createGraphics();
				g.drawImage(in, 0, 0, null);
				g.dispose();
				//frame.delete();
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return in;
	}
	
	private boolean mediaRotated() {
		double sourceRatio = mainController.getPlayer().getMedia().getWidth() / mainController.getPlayer().getMedia().getHeight();
		if(sourceRatio > 1) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	private void generateDarkImage() {
		BufferedImage bImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < 80; i++) {
			for(int j = 0; j < 80; j++) {
				bImage.setRGB(i, j, 0);
			}
		}
		Image image = SwingFXUtils.toFXImage(bImage, null);
		imageView.setImage(image);
	}

	
	public void openDialog() {
		this.show();
	}
}
