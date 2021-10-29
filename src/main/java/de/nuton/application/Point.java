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
package de.nuton.application;

import java.io.Serializable;

public class Point implements Serializable {

	//Normalized coordinates
	private double x;
	private double y;

	private double time;
	private boolean highlight;

	public Point(double x, double y, double time) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.highlight = false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	//Dirty Hack for highlighting point
	public void highlightPoint(boolean highlight) {
		this.highlight = highlight;
	}

	public boolean isHighlight() {
		return highlight;
	}
	
	public double getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "Point{" +
				"x=" + x +
				", y=" + y +
				", time=" + time +
				", highlight=" + highlight +
				'}';
	}

	public String saveString() {
		return x + "," + y + "," + time;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void setTime(double time) {
		this.time = time;
	}

}
