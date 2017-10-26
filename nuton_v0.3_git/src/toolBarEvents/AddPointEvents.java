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
	
	private static int recCounter = 0;
	private static double x = 0;
	private static double y = 0;
	public static void addRectangle(State state, MouseEvent e) {
		GraphicsContext gc = state.getMainController().getGc();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		gc.setFill(Color.rgb(255, 119, 0, 0.80));
		
		System.out.println(e.getEventType().toString());
		
		
		
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
			x = e.getX();
			y = e.getY();
		}		
		
		if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			gc.setStroke(Color.RED);
			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			for(Point p : points) {
				p.drawPoint(gc);
			}
			
			gc.strokeLine(x, y, e.getX(), y);
			gc.strokeLine(e.getX(), y, e.getX(), e.getY());
			gc.strokeLine(x, y, x, e.getY());
			gc.strokeLine(x, e.getY(), e.getX(), e.getY());
			
			//Kreuz
			gc.strokeLine(((e.getX() - x) / 2 + x) - 5, ((e.getY() - y) / 2 + y), ((e.getX() - x) / 2 + x) + 5, ((e.getY() - y) / 2 + y));
			gc.strokeLine(((e.getX() - x) / 2 + x), ((e.getY() - y) / 2 + y) - 5, ((e.getX() - x) / 2 + x), ((e.getY() - y) / 2 + y) + 5);
			
			//Diagonalen
//			gc.strokeLine(e.getX(), e.getY(), me.getX(), me.getY());
//			gc.strokeLine(me.getX(), e.getY(), e.getX(), me.getY());
			
			System.out.println("Counter: " + recCounter);
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			addPoint(state, e, (e.getX() - x) / 2 + x, (e.getY() - y) / 2 + y);
			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			for(Point p : points) {
				p.drawPoint(gc);
			}
		}
	}
	
	public static void addEllipse(State state, MouseEvent e) {
		GraphicsContext gc = state.getMainController().getGc();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		gc.setFill(Color.rgb(255, 119, 0, 0.80));
		
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
			x = e.getX();
			y = e.getY();
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			gc.setStroke(Color.RED);
			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			for(Point p : points) {
				p.drawPoint(gc);
			}
			
			int w = (int) Math.abs(e.getX() - x);
			int h = (int) Math.abs(e.getY() - y);
			
			
			gc.strokeOval(x, y, w, h);
			gc.strokeLine(((e.getX() - x) / 2 + x) - 5, ((e.getY() - y) / 2 + y), ((e.getX() - x) / 2 + x) + 5, ((e.getY() - y) / 2 + y));
			gc.strokeLine(((e.getX() - x) / 2 + x), ((e.getY() - y) / 2 + y) - 5, ((e.getX() - x) / 2 + x), ((e.getY() - y) / 2 + y) + 5);
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			addPoint(state, e, (e.getX() - x) / 2 + x, (e.getY() - y) / 2 + y);
			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			for(Point p : points) {
				p.drawPoint(gc);
			}
		}
	}
	
	private static void addPoint(State state, MouseEvent e, double x, double y) {
		Slider slider = state.getMainController().getSlider();
		GraphicsContext gc = state.getMainController().getGc();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		ListView<Integer> listX = state.getMainController().getListX();
		ListView<Integer> listY = state.getMainController().getListY();
		slider.setDisable(true);
		gc.setFill(Color.rgb(255, 119, 0, 0.80));						
		if (mainController.getSettingsController().yFixed() == true && yFix == 0) {
			//gc.fillRect(e.getX() - 10, e.getY() - 10, 20, 20);
			yFix = y;
			Point p = new Point((int)x, (int)y, slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
			System.out.println(p.getId());
		} else if (mainController.getSettingsController().yFixed() == true) {
			Point p = new Point((int)x, (int)yFix, slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
		} else if (mainController.getSettingsController().xFixed() == true && xFix == 0) {
			xFix = x;
			Point p = new Point((int)x, (int)y, slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
		} else if (mainController.getSettingsController().xFixed() == true) {
			Point p = new Point((int)xFix, (int)y, slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
		} else {
			Point p = new Point((int)x, (int)y, slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
		}
		for (Point p : points) {
			p.drawPoint(gc);
		}
		slider.setValue(slider.getValue() + mainController.getSCHRITTWEITE());
	}
	
	private static void addPoint(State state, MouseEvent e) {
		Slider slider = state.getMainController().getSlider();
		GraphicsContext gc = state.getMainController().getGc();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		ListView<Integer> listX = state.getMainController().getListX();
		ListView<Integer> listY = state.getMainController().getListY();
		slider.setDisable(true);
		gc.setFill(Color.rgb(255, 119, 0, 0.80));						
		if (mainController.getSettingsController().yFixed() == true && yFix == 0) {
			//gc.fillRect(e.getX() - 10, e.getY() - 10, 20, 20);
			yFix = e.getY();
			Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
			System.out.println(p.getId());
		} else if (mainController.getSettingsController().yFixed() == true) {
			Point p = new Point((int)e.getX(), (int)yFix, slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
		} else if (mainController.getSettingsController().xFixed() == true && xFix == 0) {
			xFix = e.getX();
			Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
		} else if (mainController.getSettingsController().xFixed() == true) {
			Point p = new Point((int)xFix, (int)e.getY(), slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
		} else {
			Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue(), points.size());
			points.add(p);
			p.drawPoint(gc);
			listX.getItems().add(p.getX());
			listY.getItems().add(p.getY());
		}
		slider.setValue(slider.getValue() + mainController.getSCHRITTWEITE());
	}
	

}
