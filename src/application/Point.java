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
	private Color highlightColor;
	private Color normalColor;
	private PropertiesReader propReader;
	private double normX;
	private double normY;
	private int mediaX;
	private int mediaY;
	private int drawX;
	private int drawY;
	private double deltaTime;
	
	public Point(int x, int y, double time) {
		this.x = x;
		this.y = y;
		this.drawX = x;
		this.drawY = y;
		this.time = time;
		propReader = new PropertiesReader();
		normalColor = propReader.getPointColor();
		highlightColor = normalColor.invert();
		color = normalColor;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void drawPoint(GraphicsContext gc) {
		gc.setStroke(color);
		gc.strokeLine(drawX + 5, drawY, drawX - 5, drawY);
		gc.strokeLine(drawX, drawY + 5, drawX, drawY - 5);
	}
	
	public void highlightPoint(boolean highlight) {
		if (highlight) {
			color = highlightColor;
		} else {
			color = normalColor;
		}
		
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
		return "Point [x=" + x + ", y=" + y + ", SEITENLAENGE=" + SEITENLAENGE + ", time=" + time
				+ ", entfernungMeterX=" + entfernungMeterX + ", entfernungMeterY=" + entfernungMeterY + ", color="
				+ color + ", highlightColor=" + highlightColor + ", normalColor=" + normalColor + ", propReader="
				+ propReader + ", normX=" + normX + ", normY=" + normY + ", mediaX=" + mediaX + ", mediaY=" + mediaY
				+ ", drawX=" + drawX + ", drawY=" + drawY + ", deltaTime=" + deltaTime + "]";
	}

	public String saveString() {
		return x + "," + y + "," + time;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setTime(double time) {
		this.time = time;
	}
	
	public void updateColor() {
		propReader.update();
		normalColor = propReader.getPointColor();
		highlightColor = normalColor.invert();
	}
	
	public void setNormCord(double x, double y) {
		this.normX = x;
		this.normY = y;
	}

	public void setNormX(double normX) {
		this.normX = normX;
	}

	public void setNormY(double normY) {
		this.normY = normY;
	}

	public double getNormX() {
		return normX;
	}

	public double getNormY() {
		return normY;
	}

	public int getMediaX() {
		return mediaX;
	}

	public void setMediaX(int mediaX) {
		this.mediaX = mediaX;
	}

	public int getMediaY() {
		return mediaY;
	}

	public void setMediaY(int mediaY) {
		this.mediaY = mediaY;
	}

	public int getDrawX() {
		return drawX;
	}

	public void setDrawX(int drawX) {
		this.drawX = drawX;
	}

	public int getDrawY() {
		return drawY;
	}

	public void setDrawY(int drawY) {
		this.drawY = drawY;
	}

	public double getDeltaTime() {
		return deltaTime;
	}

	public void setDeltaTime(double deltaTime) {
		this.deltaTime = deltaTime;
	}
	
	
	

}
