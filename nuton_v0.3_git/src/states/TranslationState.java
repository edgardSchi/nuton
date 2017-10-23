package states;

import java.util.ArrayList;

import application.DiagramsController;
import application.MainController;
import application.PixelManager;
import application.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class TranslationState extends State {
	
	private MainController mainController;
	private PixelManager pManager;
	private GraphicsContext gc;
	private DiagramsController diaController;
	private ArrayList<Point> points;
	private Slider slider;
	private ListView<Integer> listX;
	private ListView<Integer> listY;
	private Button fertigBtn;
	
	private double yFix = 0;
	private double xFix = 0;
	
	public TranslationState(MainController mainController, PixelManager pManager) {
		this.mainController = mainController;
		this.pManager = pManager;
		this.gc = mainController.getGc();
		diaController = new DiagramsController(mainController, pManager);
		points = new ArrayList<Point>();
		this.slider = mainController.getSlider();
		this.listX = mainController.getListX();
		this.listY = mainController.getListY();
		this.fertigBtn = mainController.getFertigBtn();
		fertigBtn.setDisable(false);
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(MouseEvent e) {
		System.out.println("TRANSLATE STATE");
				slider.setDisable(true);
				gc.setFill(Color.rgb(255, 119, 0, 0.80));						
				if (mainController.getSettingsController().yFixed() == true && yFix == 0) {
					//gc.fillRect(e.getX() - 10, e.getY() - 10, 20, 20);
					yFix = e.getY();
					Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue(), points.size());
					points.add(p);
					p.drawPoint(gc);
					listX.getItems().add(p.getX());
					listY.getItems().add(p.getY());
					System.out.println(p.getId());
				} else if (mainController.getSettingsController().yFixed() == true) {
					Point p = new Point((int)e.getX(), (int)yFix, slider.getValue(), points.size());
					points.add(p);
					p.drawPoint(gc);
					listX.getItems().add(p.getX());
					listY.getItems().add(p.getY());
				} else if (mainController.getSettingsController().xFixed() == true && xFix == 0) {
					xFix = e.getX();
					Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue(), points.size());
					points.add(p);
					p.drawPoint(gc);
					listX.getItems().add(p.getX());
					listY.getItems().add(p.getY());
				} else if (mainController.getSettingsController().xFixed() == true) {
					Point p = new Point((int)xFix, (int)e.getY(), slider.getValue(), points.size());
					points.add(p);
					p.drawPoint(gc);
					listX.getItems().add(p.getX());
					listY.getItems().add(p.getY());
				} else {
					Point p = new Point((int)e.getX(), (int)e.getY(), slider.getValue(), points.size());
					points.add(p);
					p.drawPoint(gc);
					listX.getItems().add(p.getX());
					listY.getItems().add(p.getY());
				}
				slider.setValue(slider.getValue() + mainController.getSCHRITTWEITE());				
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}
	
	public void reset() {
		points.clear();
		listX.getItems().clear();
		listY.getItems().clear();
		slider.setValue(0);
		slider.setDisable(false);
		gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
		yFix = 0;
		xFix = 0;
		pManager.reset();
	}
	
	@Override
	public void fertigBtnClick() {
		if (points.size() < 3) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Bitte wählen Sie mindestens zwei Punke aus.");
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					alert.close();
				}
			});
		} else {
			pManager.setPoints(points);
			pManager.calcDistances();
			pManager.calcMeter(points);
			diaController.setPoints(points);
			diaController.makeDiagram();
			diaController.show();
		}
	}

	@Override
	public ArrayList<Point> getPoints() {
		return points;
	}

}
