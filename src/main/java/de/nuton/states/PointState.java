/*******************************************************************************
 * Nuton
 *   Copyright (C) 2018-2019 Edgard Schiebelbein
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.nuton.states;

import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.draw.VideoPainter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class PointState extends State{

	//TODO: Button und Slider haben hier eigentlich nix verloren
	protected Button fertigBtn;
	protected boolean pointSelected = false;
	protected Point selectedPoint = null;
	private final ArrayList<Point> points;
	protected ChangeListener<Number> sliderListener;
	
	public PointState(MainController mainController) {
		super(mainController);
		points = new ArrayList<>();
		this.fertigBtn = mainController.getFertigBtn();
		fertigBtn.setDisable(false);

		setStateData("points", () -> points);
	}
	
	public void defaultInit() {
		mainController.getSlider().setSnapToTicks(true);
		mainController.getSlider().setValue(0);
		fertigBtn.setDisable(false);
		checkSlider(mainController.getSlider());
	}

	//TODO: Dont always redraw on every click
	public void defaultOnClick(MouseEvent e) {
		if (pointSelected) {
			de.nuton.toolBarEvents.MovePointEvents.dragPoint(mainController, e, selectedPoint);
		} else {
			mainController.getToolBarManager().pointButtonEvent(this, e);
		}
		mainController.redraw();
		mainController.updateLists(getPoints());
	}
	
	public void defaultReset() {
		pointSelected = false;
		points.clear();
		mainController.resetSlider();
		VideoPainter.getInstance().clearScreen();
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
	
	public void updateSlider(Slider slider) {
		sliderListener.changed(slider.valueProperty(), slider.getValue(), slider.getValue());
	}

	public void addPoint(Point p) {
		points.add(p);
	}

	public void removePoint(Point p) {
		points.remove(p);
	}

	public List<Point> getPoints() {
		return points;
	}

}
