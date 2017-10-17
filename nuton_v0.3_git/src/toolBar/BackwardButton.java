package toolBar;

import java.util.ArrayList;

import application.MainController;
import application.MainEventHandler;
import application.Point;
import javafx.scene.image.Image;

public class BackwardButton extends ToolBarItem {

	private MainController controller;
	private ArrayList<Point> points;
	
	public BackwardButton(MainEventHandler eventHandler) {
		controller = eventHandler.getMainController();
		setIcon(new Image("file:icons/backwardIcon.png"));
		points = controller.getPoints();
	}
	
	@Override
	public void onClick() {
		double schrittweite = controller.getSCHRITTWEITE();
		controller.getSlider().setValue(controller.getSlider().getValue() - schrittweite);
		points.remove(points.get(points.size()-1));
//		controller.getCanvas().
	}


}
