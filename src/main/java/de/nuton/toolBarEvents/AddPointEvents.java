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
package de.nuton.toolBarEvents;

import java.util.ArrayList;

import de.nuton.draw.DrawController;
import de.nuton.draw.DrawController;
import de.nuton.application.MainController;
import de.nuton.application.ScalingManager;
import de.nuton.application.Point;
import de.nuton.states.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
				DrawController.getInstance().drawPoint(p);
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
				DrawController.getInstance().drawPoint(p);
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
				DrawController.getInstance().drawPoint(p);
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
					DrawController.getInstance().drawPoint(p);
				}
				leftClicked = false;
		}
	}
	
	public static void addPoint(State state, MouseEvent e, double x, double y) {
		Slider slider = state.getMainController().getSlider();
		GraphicsContext gc = state.getMainController().getGc();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		if (slider.getValue() < slider.getMax()) {
			gc.setFill(Color.rgb(255, 119, 0, 0.80));	
			Point p;
			if (mainController.getSettings().isyFixed() && yFix == 0) {
				yFix = y;
				p = new Point((int)x, (int)y, slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else if (mainController.getSettings().isyFixed()) {
				p = new Point((int)x, (int)yFix, slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else if (mainController.getSettings().isxFixed() && xFix == 0) {
				xFix = x;
				p = new Point((int)x, (int)y, slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else if (mainController.getSettings().isxFixed()) {
				p = new Point((int)xFix, (int)y, slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else {
				p = new Point((int)x, (int)y, slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			}
			ScalingManager.getInstance().normalizePoint(p);
			state.getMainController().updateLists();
			for (Point po : points) {
				DrawController.getInstance().drawPoint(po);
			}
			slider.setValue(slider.getValue() + mainController.getSettings().getSchrittweite());
		}		
	}
	
	public static void addTrackingPoint(State state, MouseEvent e, double x, double y, double time) {
		Slider slider = state.getMainController().getSlider();
		GraphicsContext gc = state.getMainController().getGc();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		if (slider.getValue() < slider.getMax()) {
			gc.setFill(Color.rgb(255, 119, 0, 0.80));	
			Point p;
			if (mainController.getSettings().isyFixed() && yFix == 0) {
				yFix = y;
				p = new Point((int)x, (int)y, time);
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else if (mainController.getSettings().isyFixed()) {
				p = new Point((int)x, (int)yFix, time);
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else if (mainController.getSettings().isxFixed() && xFix == 0) {
				xFix = x;
				p = new Point((int)x, (int)y, time);
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else if (mainController.getSettings().isxFixed()) {
				p = new Point((int)xFix, (int)y, time);
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else {
				p = new Point((int)x, (int)y, time);
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			}
			ScalingManager.getInstance().normalizeWithMediaSize(p);
			state.getMainController().updateLists();
			for (Point po : points) {
				DrawController.getInstance().drawPoint(po);
			}
			slider.setValue(slider.getValue() + mainController.getSettings().getSchrittweite());
		}		
	}
	
	private static void addPoint(State state, MouseEvent e) {
		Slider slider = state.getMainController().getSlider();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		if (slider.getValue() < slider.getMax()) {
			Point p;
			if (mainController.getSettings().isyFixed() && yFix == 0) {
				yFix = e.getY();
				p = new Point((int)e.getX(), (int)e.getY(), slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else if (mainController.getSettings().isyFixed() == true) {
				p = new Point((int)e.getX(), (int)yFix, slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else if (mainController.getSettings().isxFixed() && xFix == 0) {
				xFix = e.getX();
				p = new Point((int)e.getX(), (int)e.getY(), slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else if (mainController.getSettings().isxFixed() == true) {
				p = new Point((int)xFix, (int)e.getY(), slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			} else {
				p = new Point((int)e.getX(), (int)e.getY(), slider.getValue());
				points.add(p);
				DrawController.getInstance().drawPoint(p);
			}
			ScalingManager.getInstance().normalizePoint(p);
			state.getMainController().updateLists();
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
