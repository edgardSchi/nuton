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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import de.nuton.application.MainFX;
import de.nuton.application.PixelManager;
import de.nuton.application.Point;
import de.nuton.math.Vector2;
import javafx.stage.FileChooser;

public class CircExportHandler extends MotionExportHandler {

	private String x = "x[px]";
	private String y = "y[px]";
	private String t = "t[s]";
	private String leer = "";
	private String eich = "Eichung[m]";
	private String eichungInPixel = "Eichung[px]";
	private String deltaPhi = "Delta Phi[°]";
	private String freq = "Frequenz [Hz]";
	private String velo = "Bahngeschwindigkeit [m/s]";
	private String angleVelo = "Winkelgeschwindigkeit [1/s]";
	private String mittelPunkt = "Mittelpunkt[px],[px]";
	
	private Point origin;
	private String mittelP;
	private double eichung;
	private double pixelLänge;
	
	private ArrayList<Vector2> deltaPhiArray;
	
	public CircExportHandler(ArrayList<Point> points, PixelManager pManager) {
		super(points, pManager);
		eichung = pManager.getSettings().getEichung() / 100;
		pixelLänge = pManager.getLAENGEPIXEL();
	}

	@Override
	public void exportData() {
		origin = pManager.getOrigin();
		mittelP = "(" + origin.getX() + "), (" + origin.getY() + ")";
		deltaPhiArray = pManager.getDeltaPhi();
		fileChooser = new FileChooser();
		fileChooser.setInitialFileName("Videoanalyse_Kreis.csv");
		FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("CSV Dateien (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(exFilter);
		
		String pfad = fileChooser.showSaveDialog(MainFX.getStage()).getAbsolutePath();
		if (pfad != null) {
			Path pfadDatei = Paths.get(pfad);

			
			try (BufferedWriter schreibPuffer = Files.newBufferedWriter(pfadDatei)){
				String zeile0 = String.format("%s; %s; %s; %s; %s; %s; %s; %s; %s; %s; %s", t, x, y, leer, deltaPhi, angleVelo, freq, leer, mittelPunkt, eich, eichungInPixel);
				schreibPuffer.write(zeile0);
				for (int i = 0; i < points.size(); i++) {
					schreibPuffer.newLine();
					String zeile;
					if (i == 0) {
						zeile = String.format("%f; %d; %d; %s; %f; %f; %f; %s; %s; %f; %f", points.get(i).getTime()/1000, points.get(i).getX(), points.get(i).getY(), leer, deltaPhiArray.get(i).getX(), pManager.getAngleVelo().get(i).getX(), pManager.getCircFreq().get(i).getX(), leer, mittelP, eichung, pixelLänge);
					} else {
						zeile = String.format("%f; %d; %d; %s; %f; %f; %f", points.get(i).getTime()/1000, points.get(i).getX(), points.get(i).getY(), leer, deltaPhiArray.get(i - 1).getX(), pManager.getAngleVelo().get(i - 1).getX(), pManager.getCircFreq().get(i - 1).getX());
					}		
					schreibPuffer.write(zeile);
				}
				
			} catch (IOException e) {
				
			}
		}
		
	}

}
