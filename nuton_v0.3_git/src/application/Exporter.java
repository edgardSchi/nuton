package application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.stage.FileChooser;

public class Exporter {
	
	private ArrayList<Point> points;
	private ArrayList<Double> velo;
	private String x = "x[px]";
	private String y = "y[px]";
	private String t = "t[s]";
	private String leer = "";
	private String xm = "x[m]";
	private String ym = "y[m]";
	private FileChooser fileChooser;

	public Exporter(ArrayList<Point> points /*ArrayList<Double> velo*/) {
		this.points = points;
//		this.velo = velo;
	}
	
	public void exportData() {
		
		fileChooser = new FileChooser();
		fileChooser.setInitialFileName("Videoanalyse.csv");
		FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("CSV Dateien (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(exFilter);
		
		String pfad = fileChooser.showSaveDialog(Main.getStage()).getAbsolutePath();
		Path pfadDatei = Paths.get(pfad);

		
		try (BufferedWriter schreibPuffer = Files.newBufferedWriter(pfadDatei)){
//			schreibPuffer.write("t");
//			schreibPuffer.newLine();
//			schreibPuffer.write("x");
//			schreibPuffer.newLine();
//			schreibPuffer.write("y");
			String zeile0 = String.format("%s; %s; %s; %s; %s; %s", t, x, y, leer, xm, ym);
			schreibPuffer.write(zeile0);
			for (int i = 0; i < points.size(); i++) {
				schreibPuffer.newLine();
				String zeile = String.format("%f; %d; %d; %s; %f; %f", points.get(i).getTime()/1000, points.get(i).getX(), points.get(i).getY(), leer, points.get(i).getEntfernungMeterX(), points.get(i).getEntfernungMeterY());
				schreibPuffer.write(zeile);
			}
			
		} catch (IOException e) {
			
		}

	}
}
