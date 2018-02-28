package io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import application.Main;
import application.PixelManager;
import application.Point;
import javafx.stage.FileChooser;
import math.Vector2;

public class CircExportHandler extends MotionExportHandler {

	private String x = "x[px]";
	private String y = "y[px]";
	private String t = "t[s]";
	private String leer = "";
	private String eich = "Eichung[m]";
	private String eichungInPixel = "Eichung[px]";
	private String deltaPhi = "Delta Phi[°]";
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
		
		String pfad = fileChooser.showSaveDialog(Main.getStage()).getAbsolutePath();
		if (pfad != null) {
			Path pfadDatei = Paths.get(pfad);

			
			try (BufferedWriter schreibPuffer = Files.newBufferedWriter(pfadDatei)){
				String zeile0 = String.format("%s; %s; %s; %s; %s; %s; %s; %s; %s", t, x, y, leer, deltaPhi, leer, mittelPunkt, eich, eichungInPixel);
				schreibPuffer.write(zeile0);
				for (int i = 0; i < points.size(); i++) {
					schreibPuffer.newLine();
					String zeile;
					if (i == 0) {
						zeile = String.format("%f; %d; %d; %s; %d; %s; %s; %f; %f", points.get(i).getTime()/1000, points.get(i).getX(), points.get(i).getY(), leer, 0, leer, mittelP, eichung, pixelLänge);
					} else {
						zeile = String.format("%f; %d; %d; %s; %f", points.get(i).getTime()/1000, points.get(i).getX(), points.get(i).getY(), leer, deltaPhiArray.get(i - 1).getX());
					}		
					schreibPuffer.write(zeile);
				}
				
			} catch (IOException e) {
				
			}
		}
		
	}

}
