package states;

import java.util.ArrayList;

import application.MainController;
import application.PixelManager;
import application.Point;
import javafx.scene.input.MouseEvent;
import settings.Settings;

public abstract class State {

	protected ArrayList<Point> points;
	protected MainController mainController;
	protected PixelManager pManager;
	protected Settings settings;
	
	public State(MainController mainController) {
		this.mainController = mainController;
		this.pManager = mainController.getPManager();
		this.settings = mainController.getSettings();
		points = new ArrayList<Point>();
	}
	
	public abstract void init();
	public abstract void onClick(MouseEvent e);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public abstract void fertigBtnClick();
	public abstract void reset();
	public abstract void redraw();
	
	
	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public MainController getMainController() {
		return mainController;
	}
	
}
