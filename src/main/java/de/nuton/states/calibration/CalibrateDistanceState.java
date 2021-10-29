package de.nuton.states.calibration;

import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.draw.VideoPainter;
import de.nuton.states.State;
import de.nuton.toolBarEvents.AddPointEvents;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;

public class CalibrateDistanceState extends State {

    private final Point[] calibrationPoints;
    private int clickCounter;

    public CalibrateDistanceState(MainController mainController) {
        super(mainController);
        clickCounter = 0;
        calibrationPoints = new Point[2];

        this.setStateData("calibrationPoints", () -> calibrationPoints);
    }

    @Override
    public void init() {
        VideoPainter.getInstance().clearScreen();
        clickCounter = 0;

        System.out.println("In CalibrateDistanceState!");
    }

    private void addPointByMouse(MouseEvent e) {
        calibrationPoints[clickCounter] = AddPointEvents.createPoint(this, e.getX(), e.getY());
        clickCounter++;
    }

    @Override
    public void onClick(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            VideoPainter.getInstance().drawCalibrationPoint(e.getX(), e.getY(), Color.RED);

            addPointByMouse(e);

            if (clickCounter == 2) {
                VideoPainter.getInstance().drawCalibrationDistance(calibrationPoints[0], calibrationPoints[1], Color.RED);

                TextInputDialog dialog = createDialog("Distanz speichern?",  1, 0);

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    //TODO: After fixing settings

                    resetCalibrate();

                    //Überprüfen, ob ein State pausiert ist
                    if (mainController.getStateManager().statePaused()) {
                        mainController.getStateManager().unpauseState();
                    } else {
                        finishState();
                    }
                } else {
                    resetCalibrate();
                }
            }
        }
    }

    protected void resetCalibrate() {
        VideoPainter.getInstance().clearScreen();
        clickCounter = 0;
    }


    protected TextInputDialog createDialog(String contentText, int lowerBound, int upperBound) {
        //TODO: After fixing settings
        TextInputDialog dialog = new TextInputDialog("" /*+ (int) UnitsHandler.convertMetersToOtherUnit(mainController.getSettings().getEichung(), settings.getLengthUnit())*/);
        dialog.setTitle("Bestätigen");
        dialog.setHeaderText(null);
        dialog.setContentText(contentText);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/nutonLogo.png")));

        dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int i = 1;
                if(!newValue.matches("\\d*")) {
                    dialog.getEditor().setText(newValue.replaceAll("[^\\d]", "1"));
                } else {
                    if (!newValue.isEmpty()) {
                        i = Integer.parseInt(newValue);
                    }
                }

                if(upperBound != 0) {
                    if (i > upperBound) {
                        dialog.getEditor().setText(newValue.replaceAll(newValue, Integer.toString(upperBound)));
                    }
                } else {
                    if (i > Integer.MAX_VALUE) {
                        dialog.getEditor().setText(newValue.replaceAll(newValue, Integer.toString(Integer.MAX_VALUE)));
                    }
                }

                if (i < lowerBound) {
                    dialog.getEditor().setText(newValue.replaceAll(newValue, Integer.toString(lowerBound)));
                }

                if(newValue.isEmpty()) {
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                } else {
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                }
            }

        });
        return dialog;
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
