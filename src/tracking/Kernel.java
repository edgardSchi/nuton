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
package tracking;

import java.awt.Color;
import java.awt.image.BufferedImage;

import math.Vector3;

public class Kernel {

	private int radius;
	private Vector3[][] data;
	private int x;
	private int y;
	private int stepsize;
	
	public Kernel(int radius, int stepsize) {
		this.radius = radius;
		this.stepsize = stepsize;
		data = new Vector3[radius + radius - 1][radius + radius - 1];
	}
	
	public void calibrate(BufferedImage image, int x, int y) {
		int xCor = 0;
		int yCor = 0;
		for(int i = 0; i < 2 * radius - 1; i++) {
			for(int j = 0; j < 2 * radius - 1; j++) {
				xCor = x + i - (radius -1);
				yCor = y + j - (radius - 1);
				if(xCor >= 0 && xCor < image.getWidth() && yCor >= 0 && yCor < image.getHeight()) {
					Color c = new Color(image.getRGB(xCor, yCor));
					data[i][j] = new Vector3(c.getRed(), c.getGreen(), c.getBlue());
				} else {
					if(xCor < 0) {
						xCor = 0;
					}
					if(xCor >= image.getWidth()) {
						xCor = image.getWidth() - 1;
					}
					if(yCor < 0) {
						yCor = 0;
					}
					if(yCor >= image.getHeight()) {
						yCor = image.getHeight() - 1;
					}
					Color c = new Color(image.getRGB(xCor, yCor));
					data[i][j] = new Vector3(c.getRed(), c.getGreen(), c.getBlue());
				}
			}
		}
	}
	
	public int[] feed(BufferedImage image) {
		double minError = Integer.MAX_VALUE;
		int[] bestCor = new int[2];
		int xCor = 0;
		int yCor = 0;
		double diff = 0;
		Vector3[][] feedData = new Vector3[radius + radius - 1][radius + radius - 1];
		for(int x = 0; x < image.getWidth(); x += stepsize) {
			for(int y = 0; y < image.getHeight(); y += stepsize) {
				for(int i = 0; i < 2 * radius - 1; i++) {
					for(int j = 0; j < 2 * radius - 1; j++) {
						xCor = x + i - (radius -1);
						yCor = y + j - (radius - 1);
						if(xCor >= 0 && xCor < image.getWidth() && yCor >= 0 && yCor < image.getHeight()) {
							Color c = new Color(image.getRGB(xCor, yCor));
							feedData[i][j] = new Vector3(c.getRed(), c.getGreen(), c.getBlue());
						} else {
							if(xCor < 0) {
								xCor = 0;
							}
							if(xCor >= image.getWidth()) {
								xCor = image.getWidth() - 1;
							}
							if(yCor < 0) {
								yCor = 0;
							}
							if(yCor >= image.getHeight()) {
								yCor = image.getHeight() - 1;
							}
							Color c = new Color(image.getRGB(xCor, yCor));
							feedData[i][j] = new Vector3(c.getRed(), c.getGreen(), c.getBlue());
						}
						Vector3 diffV = Vector3.subtract(data[i][j], feedData[i][j]);
						double diffL = diffV.getLength();
						diff += diffL;
					}
				}
				if(diff < minError) {
					minError = diff;
					bestCor[0] = x;
					bestCor[1] = y;
				}
				diff = 0;
			}
		}
		return bestCor;
	}

	public void generateImage(int size) {
		
	}
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Vector3[][] getData() {
		return data;
	}

	public void setData(Vector3[][] data) {
		this.data = data;
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
	
	
	
}
