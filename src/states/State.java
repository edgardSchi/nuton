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
package states;

import java.io.Serializable;
import java.util.ArrayList;

import application.MainController;
import application.PixelManager;
import application.Point;
import javafx.scene.input.MouseEvent;
import settings.Settings;


public abstract class State implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6582087039792420458L;
	protected ArrayList<Point> points;
	protected Point[] calibratePoints;
	protected MainController mainController;
	protected PixelManager pManager;
	protected Settings settings;
	
	public State(MainController mainController) {
		this.mainController = mainController;
		this.pManager = mainController.getPManager();
		this.settings = mainController.getSettings();
		calibratePoints = new Point[2];
		points = new ArrayList<Point>();
	}
	
	public abstract void init();
	public abstract void onClick(MouseEvent e);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public abstract void fertigBtnClick();
	public abstract void reset();
	public abstract void redraw();
	public abstract void onKill();
	public abstract void onUnpause();
	
	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public MainController getMainController() {
		return mainController;
	}
	
	public void setCalibratePoints(Point[] points) {
		calibratePoints = points;
	}
	
	public Point[] getCalibratePoints() {
		return calibratePoints;
	}
	
}
