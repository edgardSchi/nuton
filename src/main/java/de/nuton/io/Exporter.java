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
package de.nuton.io;

import java.util.ArrayList;

import de.nuton.application.PixelManager;
import de.nuton.application.Point;

public class Exporter {
	
	private TranslationExportHandler translationHandler;
	private CircExportHandler circHandler;
	
	private int handlerID;
	
	public Exporter(ArrayList<Point> points, PixelManager pManager) {
		translationHandler = new TranslationExportHandler(points, pManager);
		circHandler = new CircExportHandler(points, pManager);
		handlerID = pManager.getSettings().getMotion();
	}
	
	public void exportData() {
		if(handlerID == 0) {
			translationHandler.exportData();
		}
		if(handlerID == 1) {
			circHandler.exportData();
		}

	}
}
