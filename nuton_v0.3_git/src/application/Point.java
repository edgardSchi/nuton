package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import properties.PropertiesReader;

public class Point {
	
	private int x;
	private int y;
	private int id;
	private int SEITENLAENGE = 16;
	private double time;
	private double entfernungMeterX;
	private double entfernungMeterY;
	private Color color;
	private PropertiesReader propReader;

	public Point(int x, int y, double time, int id) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.id = id;
		propReader = new PropertiesReader();
		color = propReader.getPointColor();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getId() {
		return id;
	}
	
	public void drawPoint(GraphicsContext gc) {
		gc.setFill(color);
		gc.fillRect(x - SEITENLAENGE/2, y - SEITENLAENGE/2, SEITENLAENGE, SEITENLAENGE);
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
		return "Point [x=" + x + ", y=" + y + ", id=" + id + ", SEITENLAENGE=" + SEITENLAENGE + ", time=" + time
				+ ", entfernungMeterX=" + entfernungMeterX + ", entfernungMeterY=" + entfernungMeterY + "]";
	}


	
	

}
