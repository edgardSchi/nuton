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
package de.nuton.math;

import de.nuton.application.Point;

public class Matrix {
	
	/*
	 * [a][b]
	 * [c][d]
	 */

	private double[][] data;
	
	public Matrix(double a, double b, double c, double d) {
		data = new double[2][2];
		data[0][0] = a;
		data[0][1] = b;
		data[1][0] = c;
		data[1][1] = d;
	}
	
	public Matrix(double[][] data) {
		this.data = data;
	}
	
	public Matrix(Vector2 a, Vector2 b) {
		data[0][0] = a.getX();
		data[0][1] = b.getX();
		data[1][0] = a.getY();
		data[1][1] = b.getY();
	}
	
	public Matrix(Point a, Point b) {
		data[0][0] = a.getX();
		data[0][1] = b.getX();
		data[1][0] = a.getY();
		data[1][1] = b.getY();
	}
	
	public static Matrix matrixMult(Matrix a, Matrix b) {
		double[][] newData = new double[2][2];
		newData[0][0] = b.getA() * a.getA() + b.getC() * a.getB();
		newData[1][0] = b.getA() * a.getC() + b.getC() * a.getD();
		newData[0][1] = b.getB() * a.getA() + b.getD() * a.getB();
		newData[1][1] = b.getB() * a.getC() + b.getD() * a.getD();
		return new Matrix(newData);
	}
	
	public Vector2 multWithVector2(Vector2 v) {
		double x = v.getX() * getA() + v.getY() * getB();
		double y = v.getX() * getC() + v.getY() * getD();
		return new Vector2(x, y);
	}
	
	public double[][] getData() {
		return data;
	}
	
	public double getA() {
		return data[0][0];
	}
	
	public double getB() {
		return data[0][1];
	}
	
	public double getC() {
		return data[1][0];
	}
	
	public double getD() {
		return data[1][1];
	}
	
	public String toString() {
		return getA() + " | " + getB() + System.getProperty("line.separator") + "---------" + System.getProperty("line.separator") + getC() + " | " + getD();
	}
	
}
