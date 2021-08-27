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

/**
 * Class that handles the correct coordinates of points due to scaling of the window
 */
public class ScalingManager {

	private static ScalingManager instance;

	private double mediaW;
	private double mediaH;
	private double canvasW;
	private double canvasH;
	

	private ScalingManager() {
		
	}

	/**
	 * Get an instance of the scaling manager
	 * @return instance of scaling manager
	 */
	public static ScalingManager getInstance() {
		if (instance == null) {
			instance = new ScalingManager();
		}
		return instance;
	}
	
	/**
	 * Set the dimension of the current media
	 * @param width width of the media
	 * @param height height of the media
	 */
	public void setMediaDimension(int width, int height) {
		mediaW = width;
		mediaH = height;
	}
	
	/**
	 * Set the dimension of the canvas. Should be called everytime the dimension of the canvas changes
	 * @param width width of the canvas
	 * @param height height of the canvas
	 */
	public void setCanvasDimension(double width, double height) {
		canvasW = width;
		canvasH = height;
	}
	
	/**
	 * Calculates and sets the coordinates of the point on the media using the normal coordinates
	 * @param p point
	 */
	public void calcMediaCordWithNorm(Point p) {
		double x = p.getNormX() * mediaW;
		double y = p.getNormY() * mediaH;
		p.setMediaX((int)x);
		p.setMediaY((int)y);
	}
	
	/**
	 * Calculates and sets the normalized coordinates of a point. It uses the canvas size for normalization
	 * @param p point that needs to be normalized
	 */
	public void normalizePoint(Point p) {
		//setCanvasDimension();
		//setMediaDimension();
		double normX = p.getX()/canvasW;
		double normY = p.getY()/canvasH;
		p.setNormCord(normX, normY);
		p.setX((int)(mediaW * normX));
		p.setY((int)(mediaH * normY));
	}
	
	/**
	 * Calculates and sets the normalized coordinates of a point. It uses the media size for normalization
	 * @param p point that needs to be normalized
	 */
	public void normalizeWithMediaSize(Point p) {
		double normX = p.getX()/mediaW;
		double normY = p.getY()/mediaH;
		p.setNormCord(normX, normY);
		updatePointPos(p);
	}
	
	/**
	 * Updates the draw coordinates of the point using the normal coordinates and the canvas size
	 * @param p point that needs to be updated
	 */
	public void updatePointPos(Point p) {
		double x = p.getNormX() * canvasW;
		double y = p.getNormY() * canvasH;
		p.setDrawX((int)x);
		p.setDrawY((int)y);
	}
	
	/**
	 * Calculates the normalized coordinates of a point. It uses the canvas size for normalization
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return array with normalized x at index 0 and normalized y at index 1
	 */
	public double[] calcNormalize(int x, int y) {
		double[] norm = new double[2];
		norm[0] = x/canvasW;
		norm[1] = y/canvasH;
		return norm;
	}
	
	/**
	 * Calculates the relative coordinates of a point on the media with respect to the canvas size
	 * @param x x coordinate of the point on the canvas
	 * @param y y coordinate of the point on the canvas
	 * @param canvasWidth width of the canvas
	 * @param canvasHeight height of the canvas
	 * @param mediaWidth width of the media
	 * @param mediaHeight height of the media
	 * @return array with the coordinates on the media with x at index 0 and y at index 1
	 */
	public static int[] getCordRelativeToMedia(int x, int y, double canvasWidth, double canvasHeight, double mediaWidth, double mediaHeight) {
		int[] cords = new int[2];
		cords[0] = (int)((x/canvasWidth)*mediaWidth);
		cords[1] = (int)((y/canvasHeight)*mediaHeight);
		return cords;
	}
	
	/*
	public int[] getCordRelativeToMedia(int x, int y) {
		int[] cords = new int[2];
		cords[0] = (int)((x/mediaW)*canvasW);
		cords[1] = (int)((y/mediaH)*canvasH);
		return cords;
	}
	*/
}
