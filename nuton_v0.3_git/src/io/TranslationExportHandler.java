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

public class TranslationExportHandler extends MotionExportHandler{
	
	private String x = "x[px]";
	private String y = "y[px]";
	private String t = "t[s]";
	private String leer = "";
	private String xm = "x[m]";
	private String ym = "y[m]";
	private String eich = "Eichung[m]";
	private String eichungInPixel = "Eichung[px]";
	
	private double eichung;
	private double pixelLänge;

	public TranslationExportHandler(ArrayList<Point> points, PixelManager pManager) {
		super(points, pManager);	
		eichung = pManager.getSettings().getEichung() / 100;
		pixelLänge = pManager.getLAENGEPIXEL();
	}

	@Override
	public void exportData() {
		fileChooser = new FileChooser();
		fileChooser.setInitialFileName("Videoanalyse_Translation.csv");
		FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("CSV Dateien (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(exFilter);
		
		String pfad = fileChooser.showSaveDialog(Main.getStage()).getAbsolutePath();
		Path pfadDatei = Paths.get(pfad);

		if (pfad != null) {
			try (BufferedWriter schreibPuffer = Files.newBufferedWriter(pfadDatei)){
				String zeile0 = String.format("%s; %s; %s; %s; %s; %s; %s; %s; %s", t, x, y, leer, xm, ym, leer, eich, eichungInPixel);
				schreibPuffer.write(zeile0);
				for (int i = 0; i < points.size(); i++) {
					schreibPuffer.newLine();
					String zeile;
					if (i == 0) {
						zeile = String.format("%f; %d; %d; %s; %f; %f; %s; %f; %f", points.get(i).getTime()/1000, points.get(i).getX(), points.get(i).getY(), leer, points.get(i).getEntfernungMeterX(), points.get(i).getEntfernungMeterY(), leer, eichung, pixelLänge);
					} else {
						zeile = String.format("%f; %d; %d; %s; %f; %f", points.get(i).getTime()/1000, points.get(i).getX(), points.get(i).getY(), leer, points.get(i).getEntfernungMeterX(), points.get(i).getEntfernungMeterY());
					}		
					schreibPuffer.write(zeile);
				}
				
			} catch (IOException e) {
				
			}
		}
		
		
		
	}

}
