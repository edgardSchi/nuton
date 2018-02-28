package states.circl;

import application.DiagramsController;
import application.FertigDialogController;
import application.MainController;
import application.Point;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import settings.Settings;
import states.PointState;

public class CircState extends PointState{

	private Point origin;
	
	public CircState(MainController mainController) {
		super(mainController);
	}

	@Override
	public void init() {
		defaultInit();
		this.origin = pManager.getOrigin();
		redraw();
	}

	@Override
	public void onClick(MouseEvent e) {
		defaultOnClick(e);
		this.redraw();
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
		if (points.size() < 2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Bitte wählen Sie mindestens zwei Punke aus.");
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					alert.close();
				}
			});
		} else {		
			diaController = new DiagramsController(mainController, pManager, DiagramsController.CIRCULAR);
			pManager.setPoints(points);
			pManager.getSettings().setMotion(Settings.CIRCULAR);
			pManager.calcMeter(points);
			diaController.setPoints(points);
			pManager.createVectors(points);
			pManager.checkVectorDirection();
			pManager.calcDeltaPhi(true);
			pManager.calcAngleVelo();
			pManager.calcCircVelo();
			pManager.calcCircFreq();
			FertigDialogController fController = new FertigDialogController(diaController, pManager, points);
			fController.showDialog();
		}
	}

	@Override
	public void reset() {
		defaultReset();
	}
	
	private void drawVector(Point p) {
		gc.strokeLine(origin.getX(), origin.getY(), p.getX(), p.getY());
	}

	@Override
	public void redraw() {
		gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
		origin.drawPoint(gc);
		for(Point p : points) {
			p.drawPoint(gc);
			drawVector(p);
		}
	}
	
	public void setOrigin(Point origin) {
		this.origin = origin;
	}

}
