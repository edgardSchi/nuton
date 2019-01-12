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
package application;

import java.util.ArrayList;

import math.MathFunctions;
import math.Vector2;
import settings.Settings;

public class PixelManager {

	//Translation
	private ArrayList<Point> points;
//	private ArrayList<Double> distancesX;
//	private ArrayList<Double> distancesY;
	private ArrayList<Double> acc;
	
	//Kreisbewegung
	private ArrayList<Vector2> vectors;
	private ArrayList<Vector2> deltaPhi;
	private ArrayList<Vector2> angleVelo;
	private ArrayList<Vector2> circVelo;
	private ArrayList<Vector2> circFreq;
	
	private double EICHUNG = 0;
	private double LAENGEPIXEL = 0;
	private Point origin;
	private Point[] calibratePoints;
	private MainController mainController;
	
	public PixelManager(MainController mainController) {
		this.mainController = mainController;
		calibratePoints = new Point[2];
//		distancesX = new ArrayList<Double>();
//		distancesY = new ArrayList<Double>();
		points = new ArrayList<Point>();
		acc = new ArrayList<Double>();
		
		vectors = new ArrayList<Vector2>();
		deltaPhi = new ArrayList<Vector2>();
		angleVelo = new ArrayList<Vector2>();
		circVelo = new ArrayList<Vector2>();
		circFreq = new ArrayList<Vector2>();
	}
	
	public static double getDistance(double x1, double y1, double x2, double y2) {
		double temp = ((y1 - y2) * (y1 - y2)) + ((x2 - x1) * (x2 - x1));
		double distance = Math.sqrt(temp);
		return distance;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public void calcPixelLength() {
		LAENGEPIXEL = getDistance(calibratePoints[0].getX(), calibratePoints[0].getY(), calibratePoints[1].getX(), calibratePoints[1].getY());
	}
	
	public void calcMeter(ArrayList<Point> pointArray) {
		double eichungMeter = mainController.getSettings().getEichung();
		double tmp = eichungMeter/LAENGEPIXEL;
		double xNull = points.get(0).getX();
		double yNull = points.get(0).getY();
		double tNull = points.get(0).getTime();
		
		for (Point p : pointArray) {
				if (mainController.getSettings().getxNull() == Settings.NULL_Y_BOTTOM) {
					p.setEntfernungMeterY(-(tmp * (p.getY() - yNull)));
				} else {
					p.setEntfernungMeterY(tmp * (p.getY() - yNull));		
				}
				if (mainController.getSettings().getxNull() == Settings.NULL_X_RIGHT) {
					p.setEntfernungMeterX(-(tmp * (p.getX() - xNull)));
				} else {
					p.setEntfernungMeterX(tmp * (p.getX() - xNull));
				}
			p.setDeltaTime(p.getTime() - tNull);
		}
	}
	
	/**
	 * Berechnet die Geschwindigkeit zwischen den Punkten in der y-Richtung. Dabei wird die Entfernung in Metern der Punkte-Objekte verwendet.
	 * @param pointArray Punkte
	 * @return Die Geschwindigkeit zwischen den Punkten auf der y-Achse
	 */
	public ArrayList<Double> calcYVelo(ArrayList<Point> pointArray) {
		ArrayList<Double> velo = new ArrayList<Double>();
		
		for (int i = 1; i < pointArray.size(); i++) {
			if (i == 0) {
				velo.add(0.0);
			} else {
				double tmp = (pointArray.get(i).getEntfernungMeterY() / ((pointArray.get(i).getDeltaTime()) / 1000));
				velo.add(tmp);					
			}
			
		}
		return velo;
	}
	
	/**
	 * Berechnet die Geschwindigkeit zwischen den Punkten in der x-Richtung. Dabei wird die Entfernung in Metern der Punkte-Objekte verwendet.
	 * @param pointArray Punkte
	 * @return Die Geschwindigkeit zwischen den Punkten auf der x-Achse
	 */
	public ArrayList<Double> calcXVelo(ArrayList<Point> pointArray) {
		ArrayList<Double> velo = new ArrayList<Double>();
		
		for (int i = 1; i < pointArray.size(); i++) {
			if (i == 0) {
				velo.add(0.0);
			} else {		
				double tmp = (pointArray.get(i).getEntfernungMeterX() / ((pointArray.get(i).getDeltaTime()) / 1000));
				velo.add(tmp);					
			}			
		}
		return velo;
	}
	
	public void createVectors(ArrayList<Point> pointArray) {
		vectors.clear();
		for(int i = 0; i < pointArray.size(); i++) {
			vectors.add(new Vector2(origin, pointArray.get(i)));
		}
		this.points = pointArray;
	}
	
	public void checkVectorDirection() {
		boolean a = MathFunctions.isAgainstClock(vectors);
		System.out.println("Gegen Uhrzeigersinn: " + a);
	}
	
	public void calcDeltaPhi() {
		deltaPhi.clear();
		double tNull = points.get(0).getTime();
		double phi;
		//Überprüft in welche Richtung die Vektoren platziert wurden, wenn dies automatisch ermittelt werden soll
		if(mainController.getSettings().getCircleDirection() == Settings.CircleDirection.Automatic) {
			boolean c = MathFunctions.isAgainstClock(vectors);
			if(c) {
				mainController.getSettings().setCircleDirection(Settings.CircleDirection.GegenUhrzeiger);
			} else {
				mainController.getSettings().setCircleDirection(Settings.CircleDirection.Uhrzeiger);
			}
		}
		for (int i = 1; i < vectors.size(); i++) {
			if (mainController.getSettings().getCircleDirection() == Settings.CircleDirection.GegenUhrzeiger) {
				phi = Vector2.getAngleWithATan2(vectors.get(i - 1), vectors.get(i));
			} else {
				phi = Vector2.getAngleWithATan2(vectors.get(i), vectors.get(i - 1));
			}
			
			deltaPhi.add(new Vector2(phi, ((points.get(i).getTime() - tNull))/1000));
		}	
	}
	
	public double calcCircum() {
		double eichungMeter = EICHUNG;
		double circum = 2 * Math.PI * eichungMeter;
		return circum;
	}
	
	public void calcAngleVelo() {		
		for(int i = 0; i < deltaPhi.size(); i++) {
			double tmp = MathFunctions.degreeToRadian(deltaPhi.get(i).getX()) / ((points.get(i + 1).getTime() - points.get(i).getTime())/1000);
			angleVelo.add(new Vector2(tmp, deltaPhi.get(i).getY()));
		}
	}
	
	public void calcCircVelo() { 
		double eichungMeter = EICHUNG;
		for(int i = 0; i < angleVelo.size(); i++) {
			circVelo.add(new Vector2(angleVelo.get(i).getX() * eichungMeter, angleVelo.get(i).getY()));
		}
	}
	
	public void calcCircFreq() {
		for(int i = 0; i < angleVelo.size(); i++) {
			circFreq.add(new Vector2(angleVelo.get(i).getX() / (Math.PI * 2), angleVelo.get(i).getY()));
		}
	}
	
	public ArrayList<Vector2> getDeltaPhi() {
		return deltaPhi;
	}
	
	public void reset() {
			points.clear();
			//distances.clear();
			//distM.clear();
			acc.clear();
			vectors.clear();
			deltaPhi.clear();
			angleVelo.clear();
			circVelo.clear();
			circFreq.clear();
	}
	
	public Settings getSettings() {
		return mainController.getSettings();
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public void setLaengePixel(double LAENGEPIXEL) {
		this.LAENGEPIXEL = LAENGEPIXEL;
	}
	
	public void setEichung(double EICHUNG) {
		this.EICHUNG = EICHUNG;
	}

	public double getLAENGEPIXEL() {
		return LAENGEPIXEL;
	}
	
	public void setOrigin(Point o) {
		this.origin = o;
	}
	
	public Point getOrigin() {
		return origin;
	}

	public ArrayList<Vector2> getAngleVelo() {
		return angleVelo;
	}

	public ArrayList<Vector2> getCircVelo() {
		return circVelo;
	}

	public ArrayList<Vector2> getCircFreq() {
		return circFreq;
	}

	public Point[] getCalibratePoints() {
		return calibratePoints;
	}

	public void setCalibratePoints(Point[] calibratePoints) {
		this.calibratePoints = calibratePoints;
	}
	
	
	
	
	
	
}
