package math;

import application.Point;

public class Vector2 {

	private double x;
	private double y;
	
	public Vector2() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Point p1, Point p2) {
		this.x = p2.getX() - p1.getX();
		this.y = p2.getY() - p1.getY();
	}
	
	public double length() {
		return Math.sqrt(x*x + y * y);
	}
	
	public static double dotProduct(Vector2 v1, Vector2 v2) {
		return v1.getX() * v2.getX() + v1.getY() * v2.getY();
	}
	
	public static double getAngle(Vector2 v1, Vector2 v2) {
		double dot = Vector2.dotProduct(v1, v2);
		double lengths = v1.length() * v2.length();
		return Math.acos(dot / lengths) * ((180)/Math.PI);
	}
	
	
	/**
	 * Gibt den Winkel zwischen zwei Vektoren. Dieser ist der Winkel vom ersten Vektor bis zum zweiten Vektor,
	 * wenn man sich gegen den Uhrzeigersinn bewegt. Die Funktion gibt den Winkel im Gradmaß zurück.
	 * @param v1 Erster Vektor
	 * @param v2 Zweiter Vektor
	 * @return Winkel zwischen beiden Vektoren im Gradmaß
	 */
	public static double getAngleWithATan2(Vector2 v1, Vector2 v2) {
		double angle = (Math.atan2(v1.getY(), v1.getX()) - Math.atan2(v2.getY(), v2.getX()));
		if (angle < 0) {
			angle += 2 * Math.PI;
		}
		angle *= ((180)/Math.PI);
		return angle;
	}
	
	/**
	 * Erzeugt einen neuen Vektor, der zwischen den beiden Vektoren liegt.
	 * V1 - V2
	 * @param v1 Erster Vektor
	 * @param v2 Zweiter Vektor
	 * @return Neuer Vektor zwischen den Vektoren
	 */
	public static Vector2 subtract(Vector2 v1, Vector2 v2) {
		double newX = v1.getX() - v2.getX();
		double newY = v1.getY() - v2.getY();
		return new Vector2(newX, newY);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Vector2 [x=" + x + ", y=" + y + "]";
	}
	
	
	
}
