package toolBarEvents;

import java.util.ArrayList;

import application.MainController;
import application.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import states.State;

public class AddPointEvents {
	
	private static double yFix = 0;
	private static double xFix = 0;
	
	public static void point(State state, MouseEvent e) {
		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
			addPoint(state, e);
		}
		
	}
	
	private static double x = 0;
	private static double y = 0;
	private static double dragX = 0;
	private static double dragY = 0;
	private static double x2 = 0;
	private static double y2 = 0;
	private static boolean leftClicked = false;
	
	public static void addRectangle(State state, MouseEvent e) {
		GraphicsContext gc = state.getMainController().getGc();
		gc.setStroke(Color.RED);
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		gc.setFill(Color.rgb(255, 119, 0, 0.80));		
		
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.isPrimaryButtonDown() && !e.isSecondaryButtonDown()) {
			x = e.getX();
			y = e.getY();
			leftClicked = true;
		}		
		
		if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.isPrimaryButtonDown()) {
			gc.setStroke(Color.RED);
			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			for(Point p : points) {
				p.drawPoint(gc);
			}
			gc.setStroke(Color.RED);
			
			if (e.isSecondaryButtonDown()) {			
				x = x - dragX + e.getX();
				x2 = x2 - dragX + e.getX();
				y = y - dragY + e.getY();
				y2 = y2 - dragY + e.getY();
			} else {
				x2 = e.getX();
				y2 = e.getY();
			}
			
			gc.strokeLine(x, y, x2, y);
			gc.strokeLine(x2, y, x2, y2);
			gc.strokeLine(x, y, x, y2);
			gc.strokeLine(x, y2, x2, y2);
			
			//Kreuz
			gc.strokeLine(((x2 - x) / 2 + x) - 5, ((y2 - y) / 2 + y), ((x2 - x) / 2 + x) + 5, ((y2 - y) / 2 + y));
			gc.strokeLine(((x2 - x) / 2 + x), ((y2 - y) / 2 + y) - 5, ((x2 - x) / 2 + x), ((y2 - y) / 2 + y) + 5);
			
			//Diagonalen
//			gc.strokeLine(e.getX(), e.getY(), me.getX(), me.getY());
//			gc.strokeLine(me.getX(), e.getY(), e.getX(), me.getY());
			dragX = e.getX();
			dragY = e.getY();	
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED && !e.isPrimaryButtonDown() && leftClicked) {
			addPoint(state, e, (x2 - x) / 2 + x, (y2 - y) / 2 + y);
			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			for(Point p : points) {
				p.drawPoint(gc);
			}
			leftClicked = false;
		}
	}
	
	public static void addEllipse(State state, MouseEvent e) {
		GraphicsContext gc = state.getMainController().getGc();
		gc.setStroke(Color.RED);
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		gc.setFill(Color.rgb(255, 119, 0, 0.80));
		
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.isPrimaryButtonDown() && !e.isSecondaryButtonDown()) {
			x = e.getX();
			y = e.getY();
			leftClicked = true;
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.isPrimaryButtonDown()) {			
			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			for(Point p : points) {
				p.drawPoint(gc);
			}
			gc.setStroke(Color.RED);
			
			if (e.isSecondaryButtonDown()) {
				x = x - dragX + e.getX();
				x2 = x2 - dragX + e.getX();
				y = y - dragY + e.getY();
				y2 = y2 - dragY + e.getY();
			} else {
				x2 = e.getX();
				y2 = e.getY();
			}
			
			
			int w = (int) Math.abs(x2 - x);
			int h = (int) Math.abs(y2 - y);
			
			
			gc.strokeOval(x, y, w, h);
			gc.strokeLine((Math.abs(x2 - x) / 2 + x) - 5, (Math.abs(y2 - y) / 2 + y), (Math.abs(x2 - x) / 2 + x) + 5, (Math.abs(y2 - y) / 2 + y));
			gc.strokeLine((Math.abs(x2 - x) / 2 + x), (Math.abs(y2 - y) / 2 + y) - 5, (Math.abs(x2 - x) / 2 + x), (Math.abs(y2 - y) / 2 + y) + 5);
			
			dragX = e.getX();
			dragY = e.getY();	
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED && !e.isPrimaryButtonDown() && leftClicked) {
				addPoint(state, e, (x2 - x) / 2 + x, (y2 - y) / 2 + y);
				gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
				for(Point p : points) {
					p.drawPoint(gc);
				}
				leftClicked = false;
		}
	}
	
	private static void addPoint(State state, MouseEvent e, double x, double y) {
		Slider slider = state.getMainController().getSlider();
		GraphicsContext gc = state.getMainController().getGc();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		ListView<Integer> listX = state.getMainController().getListX();
		ListView<Integer> listY = state.getMainController().getListY();
		if (slider.getValue() < slider.getMax()) {
			gc.setFill(Color.rgb(255, 119, 0, 0.80));						
			if (mainController.getSettings().isyFixed() && yFix == 0) {
				//gc.fillRect(e.getX() - 10, e.getY() - 10, 20, 20);
				yFix = y;
				Point p = new Point((int)x, (int)y, slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			} else if (mainController.getSettings().isyFixed()) {
				Point p = new Point((int)x, (int)yFix, slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			} else if (mainController.getSettings().isxFixed() && xFix == 0) {
				xFix = x;
				Point p = new Point((int)x, (int)y, slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			} else if (mainController.getSettings().isxFixed()) {
				Point p = new Point((int)xFix, (int)y, slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			} else {
				Point p = new Point((int)x, (int)y, slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			}
			for (Point p : points) {
				p.drawPoint(gc);
			}
			slider.setValue(slider.getValue() + mainController.getSettings().getSchrittweite());
		}		
	}
	
	private static void addPoint(State state, MouseEvent e) {
		Slider slider = state.getMainController().getSlider();
		GraphicsContext gc = state.getMainController().getGc();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		ListView<Integer> listX = state.getMainController().getListX();
		ListView<Integer> listY = state.getMainController().getListY();
		if (slider.getValue() < slider.getMax()) {
			gc.setFill(Color.rgb(255, 119, 0, 0.80));						
			if (mainController.getSettings().isyFixed() && yFix == 0) {
				//gc.fillRect(e.getX() - 10, e.getY() - 10, 20, 20);
				yFix = e.getY();
				Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			} else if (mainController.getSettings().isyFixed() == true) {
				Point p = new Point((int)e.getX(), (int)yFix, slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			} else if (mainController.getSettings().isxFixed() && xFix == 0) {
				xFix = e.getX();
				Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			} else if (mainController.getSettings().isxFixed() == true) {
				Point p = new Point((int)xFix, (int)e.getY(), slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			} else {
				Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue());
				points.add(p);
				p.drawPoint(gc);
				listX.getItems().add(p.getX());
				listY.getItems().add(p.getY());
			}
			slider.setValue(slider.getValue() + mainController.getSettings().getSchrittweite());
		}
	}
	
	public static void reset() {
		x = 0;
		y = 0;
		dragX = 0;
		dragY = 0;
		x2 = 0;
		y2 = 0;
		xFix = 0;
		yFix = 0;
	}

}
