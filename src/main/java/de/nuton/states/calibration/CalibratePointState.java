package de.nuton.states.calibration;

import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.draw.VideoPainter;
import de.nuton.math.MathUtils;
import de.nuton.math.UnitsHandler;
import de.nuton.math.Vector2;
import de.nuton.states.State;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Optional;

public class CalibratePointState extends State {

    private Point calibrationPoint;
    private String alertText;

    public CalibratePointState(MainController mainController) {
        this(mainController, "");
    }

    public CalibratePointState(MainController mainController, String alertText) {
        super(mainController);
        this.alertText = alertText;
        this.setStateData("calibrationPoint", () -> calibrationPoint);
    }

    @Override
    public void init() {
        VideoPainter.getInstance().clearScreen();
    }

    @Override
    public void onClick(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            VideoPainter.getInstance().drawCalibrationPoint(e.getX(), e.getY());

            Vector2 normCord = MathUtils.toNormalizedCoordinates(e.getX(), e.getY(), mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
            calibrationPoint = new Point(normCord.getX(), normCord.getY(), -1);

            //TODO: After fixing settings
            TextInputDialog dialog = new TextInputDialog("" /*+ (int) UnitsHandler.convertMetersToOtherUnit(mainController.getSettings().getEichung(), settings.getLengthUnit())*/);
            dialog.setTitle("Bestätigen");
            dialog.setHeaderText(null);
            dialog.setContentText("Punkt speichern?");
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/nutonLogo.png")));


            System.out.println("Calibration Point: " + calibrationPoint);
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                finishState();
            }
            reset();
        }
    }

    @Override
    public void keyPressed(int k) {

    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void fertigBtnClick() {

    }

    @Override
    public void reset() {
        VideoPainter.getInstance().clearScreen();
        calibrationPoint = null;
    }

    @Override
    public void redraw() {

    }

    @Override
    public void onKill() {

    }

    @Override
    public void onUnpause() {

    }

}
