package states;

import java.util.ArrayList;

import application.MainController;
import application.PixelManager;
import application.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CalibrateState extends State { 

	private MainController mainController;
	private PixelManager pManager;
	private GraphicsContext gc;
	private int clickCounter = 0;
	private double x1;
	private double x2;
	private double y1;
	private double y2;
	
	public CalibrateState(MainController mainController, PixelManager pManager) {
		this.mainController = mainController;
		this.pManager = pManager;
		this.gc = mainController.getGc();
		mainController.getFertigBtn().setDisable(true);
	}
	
	@Override
	public void init() {
		//gc = mainController.getCanvas().getGraphicsContext2D();
	}

	@Override
	public void onClick(MouseEvent e) {
		System.out.println("CALIBRATE STATE");
		//gc.setFill(Color.rgb(255, 119, 0, 0.80));
		gc.fillRect(e.getX() - 10, e.getY() - 10, 20, 20);
		
		if (clickCounter == 0) {
			x1 = e.getX();
			y1 = e.getY();
		}
		
		if (clickCounter == 1) {
			x2 = e.getX();
			y2 = e.getY();
		}
		
		System.out.println("X1: " + x1);
		System.out.println("Y1: " + y1);
		System.out.println("X2: " + x2);
		System.out.println("Y2: " + y2);
		
		clickCounter++;
		
		if (clickCounter == 2) {
			gc.setStroke(Color.RED);
			gc.strokeLine(x1, y1, x2, y2);
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Bestätigen");
			alert.setHeaderText("Diese Distanz speichern?");
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					//LAENGEPIXEL = pManager.getDistance(x1, y1, x2, y2);
					pManager.setLaengePixel(pManager.getDistance(x1, y1, x2, y2));
					//System.out.println(LAENGEPIXEL);			
					pManager.setEichung(mainController.getEICHUNG());
					gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
					clickCounter = 0;
					mainController.getStateManager().setState(StateManager.TRANSLATION);
				} 
				if (rs == ButtonType.CANCEL) {
					gc.clearRect(0, 0,mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
					x1 = 0;
					y1 = 0;
					x2 = 0;
					y2 = 0;
					clickCounter = 0;
				}
			});
		}
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fertigBtnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Point> getPoints() {
		return null;
	}

	
}
