package application;

import java.util.ArrayList;

import settings.Settings;

public class PixelManager {

	private ArrayList<Point> points;
	private ArrayList<Double> distances;
	private ArrayList<Double> acc;
	private double EICHUNG = 0;
	private double LAENGEPIXEL = 0;
	private Settings settings;
	
	public PixelManager(Settings settings) {
		this.settings = settings;
		distances = new ArrayList<Double>();
		points = new ArrayList<Point>();
		acc = new ArrayList<Double>();
	}
	
	public double getDistance(double x1, double y1, double x2, double y2) {
		double temp = ((y1 - y2) * (y1 - y2)) + ((x2 - x1) * (x2 - x1));
		double distance = Math.sqrt(temp);
		return distance;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public void calcDistances() {
		for (int i = 0; i < points.size() - 1; i++) {
			double distance = 0;
			if (points.get(i + 1).getX() <  points.get(i).getX() - 10) {
				distance = -getDistance(points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
			} else if (points.get(i + 1).getY() < points.get(i).getY() - 10) {
				distance = -getDistance(points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
			} else {
				distance = getDistance(points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
			}
			distances.add(distance);
		}
		
		int i = 0;
		for (double d : distances) {			
			System.out.println(i + "'te Distanz = " + d);
			i++;
		}
	}
	
	public void calcMeter(ArrayList<Point> pointArray) {
		System.out.println("Eichung: " + EICHUNG);
		System.out.println("Pixellänge: " + LAENGEPIXEL);
		double eichungMeter = EICHUNG/100;
		double tmp = eichungMeter/LAENGEPIXEL;
		double xNull = points.get(0).getX();
		double yNull = points.get(0).getY();
		
		for (Point p : pointArray) {
			if (settings.getDirection() == Settings.DIRECTION_Y) {
				if (settings.getxNull() == Settings.NULL_Y_BOTTOM) {
					p.setEntfernungMeterY(-(tmp * (p.getY() - yNull)));
					System.out.println("ClacMeter: " + (p.getY() - yNull));
				} else {
					p.setEntfernungMeterY(tmp * (p.getY() - yNull));
					System.out.println("ClacMeter: " + (p.getY() - yNull));
				}				
			} else {
				if (settings.getxNull() == Settings.NULL_X_RIGHT) {
					p.setEntfernungMeterX(-(tmp * (p.getX() - xNull)));
					System.out.println("ClacMeter: " + (tmp * (p.getX() - xNull)));
				} else {
					p.setEntfernungMeterX(tmp * (p.getX() - xNull));
					System.out.println("ClacMeter: " + (tmp * (p.getX() - xNull)));
				}
			}			
		}
	}
	
	public void calcAcc(ArrayList<Point> pointArray) {
	}
	
	public ArrayList<Double> calcV(ArrayList<Point> pointArray) {
		ArrayList<Double> velo = new ArrayList<Double>();
		
		for (int i = 1; i < pointArray.size(); i++) {
			
//			double deltaS = pointArray.get(i + 1).getEntfernungMeterX() - (pointArray.get(i).getEntfernungMeterX() - pointArray.get(0).getEntfernungMeterX());
//			double deltaT = pointArray.get(i + 1).getTime() - pointArray.get(i).getTime();
			if (i == 0) {
				velo.add(0.0);
			} else {
				if (settings.getDirection() == Settings.DIRECTION_Y) {
					double tmp = (pointArray.get(i).getEntfernungMeterY() / ((pointArray.get(i).getTime()) / 1000));
					velo.add(tmp);			
					System.out.println("a: " + tmp);	
				} else {
						double tmp = (pointArray.get(i).getEntfernungMeterX() / ((pointArray.get(i).getTime()) / 1000));
						velo.add(tmp);			
						System.out.println("a: " + tmp);
				}		
			}
			
		}
		return velo;
	}
	
	public static void calcRectangleMiddle() {
		
	}
	
	public boolean yRichtung() {
		if (settings.getDirection() == Settings.DIRECTION_Y) {
			return true;
		} else {
			return false;
		}
	}
	
	public void reset() {
			points.clear();
			distances.clear();
			acc.clear();
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
	
	
}
