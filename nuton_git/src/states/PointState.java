package states;

import application.MainController;
import application.Point;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public abstract class PointState extends State{

	protected GraphicsContext gc;
	protected Slider slider;
	protected Button fertigBtn;
	protected boolean pointSelected = false;
	protected Point selectedPoint = null;
	protected ChangeListener<Number> sliderListener;
	
	public PointState(MainController mainController) {
		super(mainController);
		this.gc = mainController.getGc();
		this.slider = mainController.getSlider();
		this.fertigBtn = mainController.getFertigBtn();
		fertigBtn.setDisable(false);	
	}
	
	public void defaultInit() {
		mainController.getSlider().setSnapToTicks(true);
		mainController.getSlider().setValue(0);
		fertigBtn.setDisable(false);
		checkSlider(mainController.getSlider());
	}
	
	public void defaultOnClick(MouseEvent e) {
		updateSlider(mainController.getSlider());
		if (pointSelected) {
			toolBarEvents.MovePointEvents.dragPoint(mainController, e, selectedPoint);
		} else {
			mainController.getToolBarManager().pointButtonEvent(this, e);	
		}
	}
	
	public void defaultReset() {
		pointSelected = false;
		points.clear();
		slider.setValue(0);
		slider.setDisable(false);
		slider.setSnapToTicks(true);
		gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getWidth());
		pManager.reset();
	}
	
	
	private void checkSlider(Slider slider) {
		
		sliderListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
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
		};
			
		slider.valueProperty().addListener(sliderListener);
	}
	
	private void updateSlider(Slider slider) {
		sliderListener.changed(slider.valueProperty(), slider.getValue(), slider.getValue());
	}

	

}
