/*******************************************************************************
 * Nuton
 * Copyright (C) 2018 Edgard Schiebelbein
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package toolBar;

import java.util.ArrayList;

import application.MainController;
import application.MainEventHandler;
import application.Point;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import states.State;

public class ToolBarManager {
	
	ArrayList<ToolBarItem> items;
	ArrayList<ToolBarToggleButton> pointItems;
	private Point selectedPoint = null;
	
	private ToolBar toolBar;
	private MainController mainController;
	private MainEventHandler mainEventHandler;
	
	public ToolBarManager(ToolBar toolBar, MainController mainController) {
		this.toolBar = toolBar;
		this.mainController = mainController;
		mainEventHandler = new MainEventHandler(mainController);
		pointItems = new ArrayList<ToolBarToggleButton>();
		loadItems();
		initGroup();
	}
	
	public void addItem(ToolBarItem item) {
		toolBar.getItems().add(item.getNode());
	}
	
	private void loadItems() {
		loadFirstSegment();
		loadSecondSegment();
		loadThirdSegment();
		loadFourthSegment();
		loadFifthSegment();
	}
	
	private void loadFirstSegment() {
		ToolBarItem openButton = new OpenFileButton(mainEventHandler);
		addItem(openButton);
		ToolBarItem saveButton = new SaveButton(mainController);
		addItem(saveButton);
		addSeparator();
	}
	
	private void loadSecondSegment() {
		ToolBarItem bButton = new BackwardButton(this);
		ToolBarItem fButton = new ForwardButton(mainEventHandler);
		ToolBarItem firstPointButton = new FirstPointButton(this);
		ToolBarItem lastPointButton = new LastPointButton(this);
		addItem(firstPointButton);
		addItem(bButton);
		addItem(fButton);
		addItem(lastPointButton);
		addSeparator();
	}
	
	private void loadThirdSegment() {
		ToolBarToggleButton pButton = new PointButton(this);
		pButton.getToggleButton().setSelected(true);
		addItem(pButton);
		pointItems.add(pButton);
		ToolBarToggleButton rButton = new RectangleButton(this);
		addItem(rButton);
		pointItems.add(rButton);
		ToolBarToggleButton cButton = new EllipseButton();
		addItem(cButton);
		pointItems.add(cButton);
		addSeparator();
	}
	
	private void loadFourthSegment() {
		ToolBarButton calibration = new CalibrateDistanceButton(this);
		addItem(calibration);
		ToolBarButton removePoint = new RemovePointButton(this);
		addItem(removePoint);
		addSeparator();
	}
	
	private void loadFifthSegment() {
		ToolBarItem showDistance = new ShowDistanceButton(this);
		ToolBarItem showPoints = new ShowPointsButton(this);
		addItem(showDistance);
		addItem(showPoints);
	}
	
	private void initGroup() {
		ToggleGroup group = new ToggleGroup();
		for (ToolBarToggleButton p : pointItems) {
			p.getToggleButton().setToggleGroup(group);
		}
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		            Toggle toggle, Toggle new_toggle) {
		                if (new_toggle == null) {
		                	pointItems.get(0).getToggleButton().setSelected(true);
		                }
		    		}
		    });
	}
	
	private void addSeparator() {
		Separator s = new Separator();
		s.setOrientation(Orientation.VERTICAL);
		toolBar.getItems().add(s);
	}
	
	public MainEventHandler getEventHandler() {
		return mainEventHandler;
	}
	
	public MainController getMainController() {
		return mainController;
	}
	
	public void pointButtonEvent(State state, MouseEvent e) {
		for (ToolBarToggleButton p : pointItems) {
			if (p.getToggleButton().isSelected()) {
				p.addPoint(state, e);
				break;
			}
		}		
	}
	
	public void setSelectedPoint(Point p) {
		selectedPoint = p;
	}
	
	public Point getSelectedPoint() {
		return selectedPoint;
	}
}
