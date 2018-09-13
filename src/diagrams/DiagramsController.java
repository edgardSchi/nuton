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
package diagrams;

import java.io.IOException;
import java.util.ArrayList;

import application.MainController;
import application.PixelManager;
import application.Point;
import io.Exporter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import math.Vector2;
import states.Motion;

public class DiagramsController {

	private LineChart<Double, Double> diagram; 
	@FXML private LineChart<NumberAxis, NumberAxis> aDiagram;
	public static Stage stage;
	private ArrayList<Point> points;
	private PixelManager pManager;
	@FXML private AnchorPane anchorPane;
	@FXML private ComboBox<String> diagramBox;
	private Exporter exporter;
	@FXML private Button exportButton;
	
	private ArrayList<Vector2> deltaPhi;
	
	private NumberAxis xAxis;
	private NumberAxis yAxis;

	/*
	 * seriesArray
	 * 0 = t-s (x)
	 * 1 = t-s (y)
	 * 2 = t-v (x)
	 * 3 = t-v (y)
	 * 4 = t-x
	 * 5 = t-y
	 * 6 = x-y
	 * 
	 * 7 = t-angle
	 * 8 = t-angleV
	 * 9 = t-v
	 * 10 = t-f
	 * 11 = x-y = 6
	 */
	private XYChart.Series<Double, Double>[] seriesArray;
	private Motion motion;

	@SuppressWarnings({ "unchecked", "static-access" })
	public DiagramsController(MainController mainController, PixelManager pManager, Motion m) {
		try {
			this.motion = m;
			this.pManager = pManager;
			seriesArray = new XYChart.Series[11];
			//settings = mainController.getSettings();
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("Diagrams.fxml"));
			loader.setController(this);
			Parent root = (Parent) loader.load();
	
			
			Scene scene = new Scene(root);
			
			stage = new Stage();
			stage.setTitle("Diagramme");
			stage.setScene(scene);
			stage.initModality(Modality.NONE);
			stage.getIcons().add(new Image(DiagramsController.class.getResourceAsStream("/nutonLogo.png")));						
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		points = new ArrayList<Point>();
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		xAxis.setLabel("t[s]");
		
		yAxis.setAutoRanging(true);
		
		diagram = new LineChart(xAxis, yAxis);
		diagram.setLegendVisible(false);
	
//		
		anchorPane.setTopAnchor(diagram, 50.0);
		anchorPane.setLeftAnchor(diagram, 0.0);
		anchorPane.setBottomAnchor(diagram, 0.0);
		anchorPane.setRightAnchor(diagram, 10.0);		

		anchorPane.getChildren().add(diagram);
		
		if (motion == Motion.TRANSLATION) {
			diagramBox.getItems().addAll("Zeit-Weg (X)", "Zeit-Weg (Y)", "Zeit-Geschwindigkeit (X)", "Zeit-Geschwindigkeit (Y)", "T-X", "T-Y", "X-Y");
		} else if (motion == Motion.CIRCULAR) {
			diagramBox.getItems().addAll("Zeit-Winkel", "Zeit-Winkelgeschwindigkeit", "Zeit-Bahngeschwindigkeit", "Zeit-Frequenz", "X-Y");
		}
		
		
		
		diagramBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (motion == Motion.TRANSLATION) {
					if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Weg (X)") {		
						diagram.getData().clear();
						yAxis.setLabel("s[m]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						diagram.getData().add(seriesArray[0]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Weg (Y)") {
						diagram.getData().clear();
						yAxis.setLabel("s[m]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						diagram.getData().add(seriesArray[1]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Geschwindigkeit (X)") {
						diagram.getData().clear();
						yAxis.setLabel("v[m/s]");
						diagram.getData().add(seriesArray[2]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Geschwindigkeit (Y)") {
						diagram.getData().clear();
						yAxis.setLabel("v[m/s]");
						diagram.getData().add(seriesArray[3]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "T-X") {
						diagram.getData().clear();
						yAxis.setLabel("x[px]");
						xAxis.setLabel("t[s]");
						diagram.getData().add(seriesArray[4]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "T-Y") {
						diagram.getData().clear();
						yAxis.setLabel("y[px]");
						xAxis.setLabel("t[s]");
						diagram.getData().add(seriesArray[5]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "X-Y") {
						diagram.getData().clear();
						yAxis.setLabel("y[px]");
						xAxis.setLabel("x[px]");
						diagram.getData().add(seriesArray[6]);
					}
				} else if(motion == Motion.CIRCULAR) {
					if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Winkel") {
						diagram.getData().clear();
						yAxis.setLabel("Winkel" + "[�]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						diagram.getData().add(seriesArray[7]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Winkelgeschwindigkeit") {
						diagram.getData().clear();
						yAxis.setLabel("Winkelgeschwindigkeit" + "[1/s]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						diagram.getData().add(seriesArray[8]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Bahngeschwindigkeit") {
						diagram.getData().clear();
						yAxis.setLabel("Bahngeschwindigkeit" + "[m/s]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						diagram.getData().add(seriesArray[9]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Frequenz") {
						diagram.getData().clear();
						yAxis.setLabel("f" + "[Hz]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						diagram.getData().add(seriesArray[10]);
					} else if(diagramBox.getSelectionModel().getSelectedItem() == "X-Y") {
						diagram.getData().clear();
						yAxis.setLabel("y[px]");
						xAxis.setLabel("x[px]");
						diagram.getData().add(seriesArray[6]);
					}
				}

			}
			
		});
		
		diagramBox.setValue("Zeit-Weg");
		
		exportButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				exporter = new Exporter(points, pManager);
				exporter.exportData();
			}
			
		});
	}
	
	public void timePathX() {
		XYChart.Series<Double, Double> series = new XYChart.Series<>();
		for(Point p : points) {
			series.getData().add(new XYChart.Data<Double, Double>(p.getDeltaTime() / 1000, p.getEntfernungMeterX()));
		}
		seriesArray[0] = series;
	}
	
	public void timePathY() {
		XYChart.Series<Double, Double> series = new XYChart.Series<>();
		for(Point p : points) {
			series.getData().add(new XYChart.Data<Double, Double>(p.getDeltaTime() / 1000, p.getEntfernungMeterY()));
		}
		seriesArray[1] = series;
	}
	
	
	public void show() {
		diagram.getData().clear();
		if (motion == Motion.TRANSLATION){
			diagram.getData().add(seriesArray[0]);
			diagramBox.getItems().clear();
			diagramBox.getItems().addAll("Zeit-Weg (X)", "Zeit-Weg (Y)", "Zeit-Geschwindigkeit (X)", "Zeit-Geschwindigkeit (Y)", "T-X", "T-Y", "X-Y");
			diagramBox.setValue("Zeit-Weg (X)");
		} else if (motion == Motion.CIRCULAR) {		
			diagram.getData().add(seriesArray[7]);
			diagramBox.getItems().clear();
			diagramBox.getItems().addAll("Zeit-Winkel", "Zeit-Winkelgeschwindigkeit", "Zeit-Bahngeschwindigkeit", "Zeit-Frequenz", "X-Y");
			diagramBox.setValue("Zeit-Winkel");
		}
		stage.show();
	}
	
	public void calculateDiagrams() {
		if (motion == Motion.TRANSLATION) {
			timePathX();
			timePathY();
			timeVeloX();
			timeVeloY();
			txDiagram();
			tyDiagram();
		} else if (motion == Motion.CIRCULAR) {
			angleDiagram();
			angleVeloDiagram();
			circVeloDiagram();
			circFreqDiagram();
		}
		xyDiagram();
	}
	
	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public void timeVeloY() {
		ArrayList<Double> velo = pManager.calcYVelo(points);
		XYChart.Series<Double, Double> series = new XYChart.Series<>();
		for(int i = 0; i < velo.size(); i++) {
			series.getData().add(new XYChart.Data<>(points.get(i).getDeltaTime() / 1000, velo.get(i)));
		}
		seriesArray[3] = series;
	}
	
	public void timeVeloX() {
		ArrayList<Double> velo = pManager.calcXVelo(points);
		XYChart.Series<Double, Double> series = new XYChart.Series<>();
		for (int i = 0; i < velo.size(); i++) {
			series.getData().add(new XYChart.Data<>(points.get(i).getDeltaTime() / 1000, velo.get(i)));
		}
		seriesArray[2] = series;
	}
	
	private void xyDiagram() {
		XYChart.Series<Double, Double> series = new XYChart.Series<>();
		for (Point p : points) {
			series.getData().add(new XYChart.Data<>((double)p.getX(), (double)p.getY()));
		}
		seriesArray[6] = series;
	}
	
	private void txDiagram() {
		XYChart.Series<Double, Double> series = new XYChart.Series<>();
		for (Point p : points) {
			series.getData().add(new XYChart.Data<>((double)p.getDeltaTime(), (double)p.getX()));
		}
		seriesArray[4] = series;
	}
	
	private void tyDiagram() {
		XYChart.Series<Double, Double> series = new XYChart.Series<>();
		for (Point p : points) {
			series.getData().add(new XYChart.Data<>((double)p.getDeltaTime(), (double)p.getY()));
		}
		seriesArray[5] = series;
	}
	
	private void angleDiagram() {
		Series<Double, Double> series = new XYChart.Series<>();
		this.deltaPhi = pManager.getDeltaPhi();
		for(Vector2 v : deltaPhi) {
			series.getData().add(new XYChart.Data<>(v.getY(), v.getX()));
		}
		seriesArray[7] = series;
	}
	
	private void angleVeloDiagram() {
		Series<Double, Double> series = new XYChart.Series<>();
		ArrayList<Vector2> velo = pManager.getAngleVelo();
		for(int i = 0; i < velo.size(); i++) {
			series.getData().add(new XYChart.Data<>(velo.get(i).getY(), velo.get(i).getX()));
		}
		seriesArray[8] = series;
	}
	
	private void circVeloDiagram() {
		Series<Double, Double> series = new XYChart.Series<>();
		ArrayList<Vector2> velo = pManager.getCircVelo();
		for(int i = 0; i < velo.size(); i++) {
			series.getData().add(new XYChart.Data<>(velo.get(i).getY(), velo.get(i).getX()));
		}
		seriesArray[9] = series;
	}
	
	private void circFreqDiagram() {
		Series<Double, Double> series = new XYChart.Series<>();
		ArrayList<Vector2> freq = pManager.getCircFreq();
		for(int i = 0; i < freq.size(); i++) {
			series.getData().add(new XYChart.Data<>(freq.get(i).getY(), freq.get(i).getX()));
		}
		seriesArray[10] = series;
	}
	
	public void reset() {
		diagram.getData().clear();
		stage.close();
	}
}
