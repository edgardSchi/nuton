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

import de.nuton.draw.VideoPainter;
import de.nuton.application.MainController;
import de.nuton.application.ScalingManager;
import de.nuton.application.Point;
import de.nuton.math.MathUtils;
import de.nuton.math.Vector2;
import de.nuton.states.State;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class AddPointEvents {
	
	private static double yFix = 0;
	private static double xFix = 0;
	
	public static void point(State state, MouseEvent e) {
		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
			addPoint(state, e.getX(), e.getY());
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
		ArrayList<Point> points = state.getPoints();
		
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.isPrimaryButtonDown() && !e.isSecondaryButtonDown()) {
			x = e.getX();
			y = e.getY();
			leftClicked = true;
		}		
		
		if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.isPrimaryButtonDown()) {
			VideoPainter.getInstance().clearScreen();
			for(Point p : points) {
				VideoPainter.getInstance().drawPoint(p);
			}
			
			if (e.isSecondaryButtonDown()) {			
				x = x - dragX + e.getX();
				x2 = x2 - dragX + e.getX();
				y = y - dragY + e.getY();
				y2 = y2 - dragY + e.getY();
			} else {
				x2 = e.getX();
				y2 = e.getY();
			}

			VideoPainter.getInstance().drawSelectionRectangle(x, y, x2, y2);

			dragX = e.getX();
			dragY = e.getY();	
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED && !e.isPrimaryButtonDown() && leftClicked) {
			addPoint(state, (x2 - x) / 2 + x, (y2 - y) / 2 + y);
			VideoPainter.getInstance().clearScreen();
			for(Point p : points) {
				VideoPainter.getInstance().drawPoint(p);
			}
			leftClicked = false;
		}
	}
	
	public static void addEllipse(State state, MouseEvent e) {
		ArrayList<Point> points = state.getPoints();
		
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.isPrimaryButtonDown() && !e.isSecondaryButtonDown()) {
			x = e.getX();
			y = e.getY();
			leftClicked = true;
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.isPrimaryButtonDown()) {
			VideoPainter.getInstance().clearScreen();
			for(Point p : points) {
				VideoPainter.getInstance().drawPoint(p);
			}
			
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

			VideoPainter.getInstance().drawSelectionOval(x, y, x2, y2, w, h);
			
			dragX = e.getX();
			dragY = e.getY();	
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED && !e.isPrimaryButtonDown() && leftClicked) {
			addPoint(state, (x2 - x) / 2 + x, (y2 - y) / 2 + y);
			VideoPainter.getInstance().clearScreen();
			for(Point p : points) {
				VideoPainter.getInstance().drawPoint(p);
			}
			leftClicked = false;
		}
	}
	
	public static void addPoint(State state, double x, double y) {
		Slider slider = state.getMainController().getSlider();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		double newX = x;
		double newY = y;
		if (slider.getValue() < slider.getMax()) {
			if (mainController.getSettings().isyFixed() && yFix == 0) {
				yFix = y;
			} else if (mainController.getSettings().isyFixed()) {
				newY = yFix;
			} else if (mainController.getSettings().isxFixed() && xFix == 0) {
				xFix = x;
				newX = x;
				newY = y;
			} else if (mainController.getSettings().isxFixed()) {
				newX = xFix;
				newY = y;
			} else {
				newX = x;
				newY = y;
			}
			Vector2 normCord = MathUtils.toNormalizedCoordinates(newX, newY, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			Point p = new Point(normCord.getX(), normCord.getY(), slider.getValue());
			points.add(p);
			state.getMainController().updateLists();
			//TODO: Vielleicht den state manager notifien fürs redraw
			for (Point po : points) {
				VideoPainter.getInstance().drawPoint(po);
			}
			slider.setValue(slider.getValue() + mainController.getSettings().getSchrittweite());
		}		
	}

	//Deprecated
/*	public static void addTrackingPoint(State state, MouseEvent e, double x, double y, double time) {
		Slider slider = state.getMainController().getSlider();
		MainController mainController = state.getMainController();
		ArrayList<Point> points = state.getPoints();
		if (slider.getValue() < slider.getMax()) {
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
	}*/

	
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
