/*******************************************************************************
 * Nuton
 * Copyright (C) 2018 Edgard Schiebelbein
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package savingFile;

import java.io.Serializable;

import application.Point;

public class SerializablePoint implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4293937888368438882L;
	private int x;
	private int y;
	private double time;
	private double entfernungMeterX;
	private double entfernungMeterY;
	private double normX;
	private double normY;
	private int mediaX;
	private int mediaY;
	private int drawX;
	private int drawY;
	
	public SerializablePoint(Point p) {
		this.x = p.getX();
		this.y = p.getY();
		this.time = p.getTime();
		this.entfernungMeterX = p.getEntfernungMeterX();
		this.entfernungMeterY = p.getEntfernungMeterY();
		this.normX = p.getNormX();
		this.normY = p.getNormY();
		this.mediaX = p.getMediaX();
		this.mediaY = p.getMediaY();
		this.drawX = p.getDrawX();
		this.drawY = p.getDrawY();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getEntfernungMeterX() {
		return entfernungMeterX;
	}

	public void setEntfernungMeterX(double entfernungMeterX) {
		this.entfernungMeterX = entfernungMeterX;
	}

	public double getEntfernungMeterY() {
		return entfernungMeterY;
	}

	public void setEntfernungMeterY(double entfernungMeterY) {
		this.entfernungMeterY = entfernungMeterY;
	}

	public double getNormX() {
		return normX;
	}

	public void setNormX(double normX) {
		this.normX = normX;
	}

	public double getNormY() {
		return normY;
	}

	public void setNormY(double normY) {
		this.normY = normY;
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
	
	
	
}
