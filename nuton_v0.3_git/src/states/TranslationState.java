package states;

import java.util.ArrayList;

import application.DiagramsController;
import application.MainController;
import application.PixelManager;
import application.Point;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

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
	private boolean pointSelected = false;
	private Point selectedPoint = null;
	
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
		mainController.getSlider().setSnapToTicks(true);
		mainController.getSlider().setValue(0);
		fertigBtn.setDisable(false);
		checkSlider(mainController.getSlider());
	}

	@Override
	public void onClick(MouseEvent e) {
		if (pointSelected) {
			toolBarEvents.MovePointEvents.dragPoint(mainController, e, selectedPoint);
		} else {
			mainController.getToolBarManager().pointButtonEvent(this, e);	
		}
//		for (Point p : points) {
//			if (p.getTime() == slider.getValue()) {
//				p.highlightPoint();
//				mainController.redraw();
//			}
//		}	
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
		slider.setSnapToTicks(true);
		gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
		pManager.reset();
	}
	
	@Override
	public void fertigBtnClick() {
		if (points.size() < 2) {
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
	
	public MainController getMainController() {
		return mainController;
	}

	@Override
	public ArrayList<Point> setPoints(ArrayList<Point> points) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void checkSlider(Slider slider) {
		slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				selectedPoint = null;
				if (points != null) {
					for (Point p : points) {
						if (p.getTime() == newValue.doubleValue()) {
							selectedPoint = p;
						} else {
							p.highlightPoint(false);
							pointSelected = false;
						}
					}
					if (selectedPoint != null) {
						selectedPoint.highlightPoint(true);
						pointSelected = true;
						mainController.getToolBarManager().setSelectedPoint(selectedPoint);
					}
					mainController.redraw();
				}				
			}
			
		});
	}

}
