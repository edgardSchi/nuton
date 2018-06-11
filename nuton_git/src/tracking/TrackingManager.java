package tracking;

import java.awt.image.BufferedImage;

import application.MainController;
import application.Point;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
	
	public TrackingManager(MainController mainController, ThemeLoader themeLoader, int radius, int stepsize, int scale) {
		this.mainController = mainController;
		this.radius = radius;
		Point p = mainController.getStateManager().getPoints().get(0);
		kernel = new Kernel(radius, stepsize);
		try {
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("TrackingManagerPane.fxml"));
			loader.setController(this);
			Node node = (Node) loader.load();
			Stage stage = (Stage) getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/nutonLogo.png")));
			getDialogPane().setContent(node);
			
			setTitle("Tracking");
			
			getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
			stage.getScene().getStylesheets().add(themeLoader.getTheme());
			
		} catch (Exception e) {
			e.printStackTrace();
		}		

//		Platform.runLater(new Runnable() {
//
//			@Override
//			public void run() {
//				
//			}
//			
//		});
		
		calibrateBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selectTrackingPoint(p.getDrawX(), p.getDrawY());
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
	}
	
	public void selectTrackingPoint(int x, int y) {
		double width = mainController.getMv().getFitWidth();
		double height = mainController.getMv().getFitHeight();
		WritableImage wImage = new WritableImage((int)width, (int)height);
		mainController.getMv().snapshot(null, wImage);
		BufferedImage image = SwingFXUtils.fromFXImage(wImage, null);
		kernel.calibrate(image, x, y);
		mainController.getGc().setFill(Color.RED);
		mainController.getGc().fillRect(x-5, y-5, 10, 10);
		BufferedImage kernelBImage = image.getSubimage(x - radius, y - radius, radius + radius - 1 , radius + radius - 1);
		Image kernelImage = SwingFXUtils.toFXImage(kernelBImage, null);
		imageView.setImage(kernelImage);
	}

	public void track() {
		double width = mainController.getMv().getFitWidth();
		double height = mainController.getMv().getFitHeight();
		WritableImage wImage = new WritableImage((int)width, (int)height);
		mainController.getMv().snapshot(null, wImage);
		BufferedImage image = SwingFXUtils.fromFXImage(wImage, null);
		int[] cords = kernel.feed(image, 100);
		mainController.getGc().fillRect(cords[0]-5, cords[1]-5, 10, 10);
		AddPointEvents.addPoint(mainController.getStateManager().getCurrentState(), null, cords[0], cords[1]);
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
