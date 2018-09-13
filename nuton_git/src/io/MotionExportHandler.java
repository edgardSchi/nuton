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
package io;

import java.util.ArrayList;

import application.PixelManager;
import application.Point;
import javafx.stage.FileChooser;

public abstract class MotionExportHandler {

	protected FileChooser fileChooser;
	protected ArrayList<Point> points;
	protected PixelManager pManager;
	
	public MotionExportHandler(ArrayList<Point> points, PixelManager pManager) {
		this.points = points;
		this.pManager = pManager;
	}
	
	public abstract void exportData();
	
	
}
