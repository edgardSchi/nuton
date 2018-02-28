package application;

import java.io.IOException;
import java.util.ArrayList;

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
import settings.Settings;

public class DiagramsController {

	private MainController mainController;
	private LineChart<NumberAxis, NumberAxis> vDiagram; 
	@FXML private LineChart<NumberAxis, NumberAxis> aDiagram;
	public static Stage stage;
	private ArrayList<Point> points;
	private PixelManager pManager;
	@FXML private AnchorPane anchorPane;
	@FXML private ComboBox<String> diagramBox;
	@FXML ArrayList<Double> velo;
	private Exporter exporter;
	@FXML private Button exportButton;
	
	private ArrayList<Vector2> deltaPhi;
	//private ArrayList<Double> angleVelo;
	
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	private Settings settings;
	
	private XYChart.Series<NumberAxis, NumberAxis>[] seriesArray;
	
	private int stateID;
	
	public static final int TRANSLATION = 0;
	public static final int CIRCULAR = 1;

	@SuppressWarnings({ "unchecked", "static-access" })
	public DiagramsController(MainController mainController, PixelManager pManager, int stateID) {
		try {
			this.mainController = mainController;
			this.pManager = pManager;
			this.stateID = stateID;
			seriesArray = new XYChart.Series[8];
			settings = mainController.getSettings();
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
		
		vDiagram = new LineChart(xAxis, yAxis);
		vDiagram.setLegendVisible(false);
//		
		anchorPane.setTopAnchor(vDiagram, 50.0);
		anchorPane.setLeftAnchor(vDiagram, 0.0);
		anchorPane.setBottomAnchor(vDiagram, 0.0);
		anchorPane.setRightAnchor(vDiagram, 10.0);		

		anchorPane.getChildren().add(vDiagram);
		
		if (stateID == 0) {
			diagramBox.getItems().addAll("Zeit-Weg", "Zeit-Geschwindigkeit", "T-X", "X-Y");
		} else if (stateID == 1) {
			diagramBox.getItems().addAll("Zeit-Winkel", "Zeit-Winkelgeschwindigkeit", "Zeit-Bahngeschwindigkeit", "Zeit-Frequenz", "X-Y");
		}
		
		
		
		diagramBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (stateID == 0) {
					if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Weg") {		
						vDiagram.getData().clear();
						yAxis.setLabel("s[m]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						vDiagram.getData().add(seriesArray[0]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Geschwindigkeit") {
						vDiagram.getData().clear();
						yAxis.setLabel("v[m/s]");
						vDiagram.getData().add(seriesArray[1]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "X-Y") {
						vDiagram.getData().clear();
						yAxis.setLabel("y[px]");
						xAxis.setLabel("x[px]");
						vDiagram.getData().add(seriesArray[3]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "T-X" || diagramBox.getSelectionModel().getSelectedItem() == "T-Y") {
						vDiagram.getData().clear();
						if (settings.getDirection() == Settings.DIRECTION_Y) {
							yAxis.setLabel("y[px]");
							xAxis.setLabel("t[s]");
						} else {
							yAxis.setLabel("x[px]");
							xAxis.setLabel("t[s]");
						}
						vDiagram.getData().add(seriesArray[2]);
					}
				} else if(stateID == 1) {
					if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Winkel") {
						vDiagram.getData().clear();
						yAxis.setLabel("Winkel" + "[°]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						vDiagram.getData().add(seriesArray[4]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Winkelgeschwindigkeit") {
						vDiagram.getData().clear();
						yAxis.setLabel("Winkelgeschwindigkeit" + "[1/s]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						vDiagram.getData().add(seriesArray[5]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Bahngeschwindigkeit") {
						vDiagram.getData().clear();
						yAxis.setLabel("Bahngeschwindigkeit" + "[m/s]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						vDiagram.getData().add(seriesArray[6]);
					} else if (diagramBox.getSelectionModel().getSelectedItem() == "Zeit-Frequenz") {
						vDiagram.getData().clear();
						yAxis.setLabel("f" + "[Hz]");
						xAxis.setUpperBound(mainController.getMediaLength());
						xAxis.setTickUnit(mainController.getSettings().getSchrittweite());
						vDiagram.getData().add(seriesArray[7]);
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void makeDiagram() {
		XYChart.Series series = new XYChart.Series();
		for (Point p : points) {
			if (settings.getDirection() == Settings.DIRECTION_Y) {
				series.getData().add(new XYChart.Data(p.getTime() / 1000, p.getEntfernungMeterY()));
			} else {
				series.getData().add(new XYChart.Data(p.getTime() / 1000, p.getEntfernungMeterX()));
			}
			System.out.println("Punkt: " + p.toString());
		}
		seriesArray[0] = series;
	}
	
	public void show() {
		txDiagram();
		if (settings.getDirection() == Settings.DIRECTION_Y && stateID == 0) {
			makeDiagram();
			testAcc();
			xyDiagram();
			vDiagram.getData().add(seriesArray[0]);
			diagramBox.getItems().clear();
			diagramBox.getItems().addAll("Zeit-Weg", "Zeit-Geschwindigkeit", "T-Y", "X-Y");
			diagramBox.setValue("Zeit-Weg");
		} else if (stateID == 0){
			makeDiagram();
			testAcc();
			xyDiagram();
			vDiagram.getData().add(seriesArray[0]);
			diagramBox.getItems().clear();
			diagramBox.getItems().addAll("Zeit-Weg", "Zeit-Geschwindigkeit", "T-X", "X-Y");
			diagramBox.setValue("Zeit-Weg");
		} else if (stateID == 1) {
			angleDiagram();
			angleVeloDiagram();
			circVeloDiagram();
			circFreqDiagram();
			vDiagram.getData().add(seriesArray[4]);
			diagramBox.getItems().clear();
			diagramBox.getItems().addAll("Zeit-Winkel", "Zeit-Winkelgeschwindigkeit", "Zeit-Bahngeschwindigkeit", "Zeit-Frequenz", "X-Y");
			diagramBox.setValue("Zeit-Winkel");
		}
		stage.show();
	}
	
	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	@SuppressWarnings("unchecked")
	public void testAcc() {
		velo = pManager.calcV(points);
		@SuppressWarnings("rawtypes")
		XYChart.Series series = new XYChart.Series();
		for (int i = 0; i < velo.size(); i++) {
//			double tmp = (points.get(i + 1).getEntfernungMeterX() - points.get(i + 1).getEntfernungMeterX() / MainController.SCHRITTWEITE);
			series.getData().add(new XYChart.Data(points.get(i).getTime() / 1000, velo.get(i)));
			System.out.println("V: " + velo.get(i));
		}
		seriesArray[1] = series;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void xyDiagram() {
		XYChart.Series series = new XYChart.Series();
		for (Point p : points) {
			series.getData().add(new XYChart.Data(p.getX(), p.getY()));
		}
		seriesArray[3] = series;
	}
	
	private void txDiagram() {
		if (settings.getDirection() == Settings.DIRECTION_Y) {
			XYChart.Series series = new XYChart.Series();
			for (Point p : points) {
				series.getData().add(new XYChart.Data(p.getTime(), p.getY()));
			}
			seriesArray[2] = series;
		} else {
			XYChart.Series series = new XYChart.Series();
			for (Point p : points) {
				series.getData().add(new XYChart.Data(p.getTime(), p.getX()));
			}
			seriesArray[2] = series;
		}
	}
	
	private void angleDiagram() {
		Series<NumberAxis, NumberAxis> series = new XYChart.Series<>();
		this.deltaPhi = pManager.getDeltaPhi();
		for(Vector2 v : deltaPhi) {
			series.getData().add(new XYChart.Data(v.getY(), v.getX()));
		}
		seriesArray[4] = series;
	}
	
	private void angleVeloDiagram() {
		Series<NumberAxis, NumberAxis> series = new XYChart.Series<>();
		ArrayList<Vector2> velo = pManager.getAngleVelo();
		for(int i = 0; i < velo.size(); i++) {
			series.getData().add(new XYChart.Data(velo.get(i).getY(), velo.get(i).getX()));
		}
		seriesArray[5] = series;
	}
	
	private void circVeloDiagram() {
		Series<NumberAxis, NumberAxis> series = new XYChart.Series<>();
		ArrayList<Vector2> velo = pManager.getCircVelo();
		for(int i = 0; i < velo.size(); i++) {
			series.getData().add(new XYChart.Data(velo.get(i).getY(), velo.get(i).getX()));
		}
		seriesArray[6] = series;
	}
	
	private void circFreqDiagram() {
		Series<NumberAxis, NumberAxis> series = new XYChart.Series<>();
		ArrayList<Vector2> freq = pManager.getCircFreq();
		for(int i = 0; i < freq.size(); i++) {
			series.getData().add(new XYChart.Data(freq.get(i).getY(), freq.get(i).getX()));
		}
		seriesArray[7] = series;
	}
	
	public void reset() {
		vDiagram.getData().clear();
		stage.close();
	}
}
