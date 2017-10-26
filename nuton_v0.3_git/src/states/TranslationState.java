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
		mainController.getToolBarManager().pointButtonEvent(this, e);	
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
	
	public MainController getMainController() {
		return mainController;
	}

}
