package states.translation;

import application.DrawHandler;
import application.FertigDialogController;
import application.MainController;
import application.Point;
import diagrams.DiagramsController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import savingFile.TempSaving;
import settings.Settings;
import states.Motion;
import states.PointState;

public class TranslationState extends PointState {
	
	
	public TranslationState(MainController mainController) {
		super(mainController);
	}

	@Override
	public void init() {
		this.defaultInit();
	}

	@Override
	public void onClick(MouseEvent e) {
		this.defaultOnClick(e);
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
		this.defaultReset();
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
			pManager.getSettings().setMotion(Settings.TRANSLATION);
			pManager.calcPixelLength();
			pManager.calcMeter(points);
			FertigDialogController fController = new FertigDialogController(mainController, pManager, points, Motion.TRANSLATION);
			fController.showDialog();
		}
	}

	@Override
	public void redraw() {
		gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
		if(TempSaving.isShowPoints()) {
			for(Point p : points) {
				p.drawPoint(gc);
			}
		}
		if(TempSaving.isShowDistance()) {
			DrawHandler.drawDistance(gc, getCalibratePoints()[0], getCalibratePoints()[1], Double.toString(settings.getEichung()).concat(" cm"));
		}
	}
	

}
