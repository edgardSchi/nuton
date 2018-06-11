package plugins;

import application.MainController;
import application.PixelManager;
import application.ScalingManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class PluginInterface {

	private MainController mainController;
	
	public PluginInterface(MainController mainController) {
		this.mainController = mainController;
	}
	
	public Canvas getCanvas() {
		return mainController.getCanvas();
	}
	
	public Button getFinishButton() {
		return mainController.getFertigBtn();
	}
	
	public GraphicsContext getGraphicsContext() {
		return mainController.getGc();
	}
	
	public Media getMedia() {
		return mainController.getMedia();
	}
	
	public double getMediaLength() {
		return mainController.getMediaLength();
	}
	
	public MediaView getMediaView() {
		return mainController.getMv();
	}
	
	public MediaPlayer getMediaPlayer() {
		return mainController.getPlayer();
	}
	
	public PixelManager getPixelManager() {
		return mainController.getPManager();
	}
	
	public Button getRestartButton() {
		return mainController.getRestartBtn();
	}
	
	public ScalingManager getScalingManager() {
		return mainController.getScalingManager();
	}
	
	// >> WEITERARBEITEN << 
	public void get() {
		
	}
 }
