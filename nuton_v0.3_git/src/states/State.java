package states;

import java.util.ArrayList;

import application.MainController;
import application.Point;
import javafx.scene.input.MouseEvent;

public abstract class State {

	public abstract void init();
	public abstract void onClick(MouseEvent e);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public abstract void fertigBtnClick();
	public abstract ArrayList<Point> getPoints();
	public abstract ArrayList<Point> setPoints(ArrayList<Point> points);
	public abstract MainController getMainController();
	public abstract void reset();
	
}
