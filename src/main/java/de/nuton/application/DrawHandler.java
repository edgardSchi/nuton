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


import de.nuton.properties.PropertiesReader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawHandler {

	public static void drawDistance(GraphicsContext gc, Point a, Point b, String label) {
		gc.setFill(Color.AQUAMARINE);
		gc.setStroke(Color.AQUAMARINE);
		gc.fillRect(a.getDrawX() - 4, a.getDrawY() - 4, 8, 8);
		gc.fillRect(b.getDrawX() - 4, b.getDrawY() - 4, 8, 8);
		gc.strokeLine(a.getDrawX(), a.getDrawY(), b.getDrawX(), b.getDrawY());
		int d = 15;
		gc.strokeText(label, a.getDrawX() + d, a.getDrawY() + d);
	}
	
	public static void drawPoint(GraphicsContext gc, Point p, boolean highlight) {
		int drawX = p.getDrawX();
		int drawY = p.getDrawY();
		Color color = PropertiesReader.getInstance().getPointColor();
		if (highlight) {
			color = color.invert();
		}
		gc.setStroke(color);
		gc.strokeLine(drawX + 5, drawY, drawX - 5, drawY);
		gc.strokeLine(drawX, drawY + 5, drawX, drawY - 5);
	}
}
