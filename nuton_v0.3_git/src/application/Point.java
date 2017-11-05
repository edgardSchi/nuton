package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import properties.PropertiesReader;

public class Point {
	
	private int x;
	private int y;
	private int SEITENLAENGE = 16;
	private double time;
	private double entfernungMeterX;
	private double entfernungMeterY;
	private Color color;
	private PropertiesReader propReader;

	public Point(int x, int y, double time) {
		this.x = x;
		this.y = y;
		this.time = time;
		propReader = new PropertiesReader();
		color = propReader.getPointColor();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void drawPoint(GraphicsContext gc) {
//		gc.setFill(color);
//		gc.fillRect(x - SEITENLAENGE/2, y - SEITENLAENGE/2, SEITENLAENGE, SEITENLAENGE);
		gc.setStroke(color);
		gc.strokeLine(x + 5, y, x - 5, y);
		gc.strokeLine(x, y + 5, x, y - 5);
	}
	
	public void removePoint(GraphicsContext gc) {
		gc.clearRect(x - SEITENLAENGE/2, y - SEITENLAENGE/2, SEITENLAENGE, SEITENLAENGE);
	}
	
	public double getTime() {
		return time;
	}
	
	public void setEntfernungMeterX(double entfernung) {
		entfernungMeterX = entfernung;
	}
	
	public double getEntfernungMeterX() {
		return entfernungMeterX;
	}
	
	public void setEntfernungMeterY(double entfernung) {
		entfernungMeterY = entfernung;
	}
	
	public double getEntfernungMeterY() {
		return entfernungMeterY;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y  + ", SEITENLAENGE=" + SEITENLAENGE + ", time=" + time
				+ ", entfernungMeterX=" + entfernungMeterX + ", entfernungMeterY=" + entfernungMeterY + "]";
	}

	public String saveString() {
		return x + "," + y + "," + time;
	}
	
	

}
