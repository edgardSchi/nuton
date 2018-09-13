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
package application;

public class ScalingManager {

	private MainController mainController;
	private double mediaW;
	private double mediaH;
	private double canvasW;
	private double canvasH;
	
	public ScalingManager(MainController mainController) {
		this.mainController = mainController;
	}
	
	public void setMediaDimension() {
		mediaW = mainController.getPlayer().getMedia().getWidth();
		mediaH = mainController.getPlayer().getMedia().getHeight();
	}
	
	public void setCanvasDimension() {
		canvasW = mainController.getCanvas().getWidth();
		canvasH = mainController.getCanvas().getHeight();	
	}
	
	public void calcMediaCordWithNorm(Point p) {
		double x = p.getNormX() * mediaW;
		double y = p.getNormY() * mediaH;
		p.setMediaX((int)x);
		p.setMediaY((int)y);
	}
	
	public void normalizePoint(Point p) {
		setCanvasDimension();
		setMediaDimension();
		double normX = p.getX()/canvasW;
		double normY = p.getY()/canvasH;
		p.setNormCord(normX, normY);
		p.setX((int)(mediaW * normX));
		p.setY((int)(mediaH * normY));
	}
	
	public void normalizeWithMediaSize(Point p) {
		double normX = p.getX()/mediaW;
		double normY = p.getY()/mediaH;
		p.setNormCord(normX, normY);
		updatePointPos(p);
	}
	
	public void updatePointPos(Point p) {
		setCanvasDimension();
		setMediaDimension();
		double x = p.getNormX() * canvasW;
		double y = p.getNormY() * canvasH;
		p.setDrawX((int)x);
		p.setDrawY((int)y);
	}
	
	public double[] calcNormalize(int x, int y) {
		double[] norm = new double[2];
		norm[0] = x/canvasW;
		norm[1] = y/canvasH;
		return norm;
	}
	
	
	
}
