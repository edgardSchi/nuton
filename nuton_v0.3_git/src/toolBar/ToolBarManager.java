package toolBar;

import java.util.ArrayList;

import application.MainController;
import application.MainEventHandler;
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
	
	private ToolBar toolBar;
	private MainController mainController;
	//private ClassLoader classLoader;
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
		//items.add(item);
		toolBar.getItems().add(item.getNode());
	}
	
	private void loadItems() {
		loadFirstSegment();
		loadSecondSegment();
		loadThirdSegment();
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
		addItem(bButton);
		addItem(fButton);
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
}
