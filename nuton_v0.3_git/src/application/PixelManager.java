package application;

import java.util.ArrayList;

import math.MathFunctions;
import math.Vector2;
import settings.Settings;

public class PixelManager {

	private ArrayList<Point> points;
	private ArrayList<Double> distances;
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
	private Settings settings;
	
	public PixelManager(Settings settings) {
		this.settings = settings;
		distances = new ArrayList<Double>();
		points = new ArrayList<Point>();
		acc = new ArrayList<Double>();
		
		vectors = new ArrayList<Vector2>();
		deltaPhi = new ArrayList<Vector2>();
		angleVelo = new ArrayList<Vector2>();
		circVelo = new ArrayList<Vector2>();
		circFreq = new ArrayList<Vector2>();
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
		double tNull = points.get(0).getTime();
		
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
			p.setTime(p.getTime() - tNull);
		}
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
	
	public void createVectors(ArrayList<Point> pointArray) {
		vectors.clear();
		for(int i = 0; i < pointArray.size(); i++) {
			vectors.add(new Vector2(origin, pointArray.get(i)));
		}
		this.points = pointArray;
	}
	
	public void checkVectorDirection() {
		MathFunctions.isAgainstClock(vectors);
	}
	
	public void calcDeltaPhi(boolean againstClock) {
		deltaPhi.clear();
		double tNull = points.get(0).getTime();
		double phi;
		for (int i = 1; i < vectors.size(); i++) {
			if (settings.gegenUhrzeiger()) {
				phi = Vector2.getAngleWithATan2(vectors.get(i - 1), vectors.get(i));
			} else {
				phi = Vector2.getAngleWithATan2(vectors.get(i), vectors.get(i - 1));
			}
			
			deltaPhi.add(new Vector2(phi, ((points.get(i).getTime() - tNull))/1000));
			System.out.println("Winkel: " + phi);
			System.out.println("DeltaPhi Array Größe: " + deltaPhi.size());
		}	
	}
	
	public double calcCircum() {
		double eichungMeter = EICHUNG/100;
		double circum = 2 * Math.PI * eichungMeter;
		return circum;
	}
	
	public void calcAngleVelo() {		
		for(int i = 0; i < deltaPhi.size(); i++) {
			double tmp = MathFunctions.degreeToRadian(deltaPhi.get(i).getX()) / ((points.get(i + 1).getTime() - points.get(i).getTime())/1000);
			System.out.println("Winkelgeschwindigkeit: " + tmp);
			angleVelo.add(new Vector2(tmp, deltaPhi.get(i).getY()));
		}
	}
	
	public void calcCircVelo() { 		
		for(int i = 0; i < angleVelo.size(); i++) {
			circVelo.add(new Vector2(angleVelo.get(i).getX() * EICHUNG/100, angleVelo.get(i).getY()));
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
			vectors.clear();
			deltaPhi.clear();
			angleVelo.clear();
			circVelo.clear();
			circFreq.clear();
	}
	
	public Settings getSettings() {
		return settings;
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
	
	
	
	
	
	
}
