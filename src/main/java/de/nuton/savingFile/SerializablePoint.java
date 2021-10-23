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
package de.nuton.savingFile;

import java.io.Serializable;

import de.nuton.application.Point;

public class SerializablePoint implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4293937888368438882L;
	private double x;
	private double y;
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
		this.entfernungMeterY = 0;
		this.entfernungMeterX = 0;
		this.normX = 0;
		this.normY = 0;
		this.mediaX = 0;
		this.mediaY = 0;
		this.drawX = 0;
		this.drawY = 0;
	}

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
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
