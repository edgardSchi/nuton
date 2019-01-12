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
package tracking;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import math.Vector3;

public class KernelManager {
	
	private int stepsize;
	private int radius;
	
	private int cpuCores;
	//private Kernel[] kernels;
	private List<Kernel> kernels;
	//private BufferedImage[] images;
	
	private Kernel calibrateKernel;
	
	private int oWidth;
	private int oHeight;
	
	private int[] heights;
	
	private ArrayList<Vector3> kernelResults;

	public KernelManager(int radius, int stepsize) {
		this.radius = radius;
		this.stepsize = stepsize;
		calibrateKernel = new Kernel(radius, stepsize);
		kernelResults = new ArrayList<Vector3>();
		cpuCores = Runtime.getRuntime().availableProcessors();
		kernels = new ArrayList<Kernel>();
	}
	
	private void initKernels() {
		kernels.clear();
		for(int i = 0; i < cpuCores; i++) {
			kernels.add(new Kernel(radius, stepsize));
			kernels.get(i).setData(calibrateKernel.getData());
		}
	}
	
	public void calibrate(BufferedImage image, int x, int y) {
		calibrateKernel.calibrate(image, x, y);
		oWidth = image.getWidth();
		oHeight = image.getHeight();
		initKernels();
	}
	
	private void initImages(BufferedImage image) {
		calcBounds();
		int y = 0;
		for(int i = 0; i < kernels.size(); i++) {
			BufferedImage kImage = image.getSubimage(0, y, image.getWidth(), heights[i]);
			kernels.get(i).setImage(kImage);
			y = y + heights[i];
		}
	}
	
	private void calcBounds() {
		int[] heights = new int[cpuCores];
		int rows = oHeight / cpuCores;
		int remainder = oHeight - cpuCores * rows;
		for(int i = 0; i < heights.length; i++) {
			heights[i] = rows;
		}
		if(remainder != 0) {
			for(int i = 0; remainder > 0; i++) {
				heights[i] += 1;
				remainder--;
			}
		}
		this.heights = heights;
	}
	
	public int[] runKernels(BufferedImage oImage) {
		initKernels();
		initImages(oImage);
		int[] cords = new int[2];
		System.out.println("CPU Cores: " + cpuCores);
		for(int i = 0; i < kernels.size(); i++) {
			kernels.get(i).start();
			System.out.println("Kernel_" + i + " gestartet!");
		}
		try {
			for(Kernel k : kernels) {
				k.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		kernelResults.clear();
		for(int i = 0; i < cpuCores; i++) {			
			kernelResults.add(kernels.get(i).getResult());
		}
		int index = checkForLowestError();
		cords[0] = (int)kernelResults.get(index).getX();
		int offset = 0;
		for(int i = 1; i <= index; i++) {
			offset += heights[i - 1];
		}
		cords[1] = (int)kernelResults.get(index).getY() + offset;
		return cords;
	}
	
	private int checkForLowestError() {
		double minError = Integer.MAX_VALUE;
		int index = 0;
		for(int i = 0; i < kernelResults.size(); i++) {
			double error = kernelResults.get(i).getZ();
			if(error <= minError) {
				index = i;
				minError = error;
			}
		}
		return index;
	}
}
