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
		int counter = 0;
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
				counter++;
				System.out.println(counter + " :: " + " :: x: " + xCor + " y: " + yCor +  " " + data[i][j] + " width: " + image.getWidth());
			}
		}
	}
	
	public int[] feed(BufferedImage image, int searchRadius) {
		double minError = Integer.MAX_VALUE;
		int[] bestCor = new int[2];
		int xCor = 0;
		int yCor = 0;
		double diff = 0;
		int counter = 0;
		Vector3[][] feedData = new Vector3[radius + radius - 1][radius + radius - 1];
		for(int x = 0; x < image.getWidth(); x += stepsize) {
			for(int y = 0; y < image.getHeight(); y += stepsize) {
				counter++;
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
						if(xCor == 5 && yCor == 5) {
							System.out.println(counter + " :: " + " :: x: " + xCor + " y: " + yCor +  " " + feedData[i][j] + " width: " + image.getWidth());
						}
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
