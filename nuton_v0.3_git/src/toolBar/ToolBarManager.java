package toolBar;

import java.util.ArrayList;

import application.MainController;
import application.MainEventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;

public class ToolBarManager {
	
	ArrayList<ToolBarItem> items;
	
	private ToolBar toolBar;
	private MainController mainController;
	private ClassLoader classLoader;
	private MainEventHandler mainEventHandler;
	
	public ToolBarManager(ToolBar toolBar, MainController mainController) {
		this.toolBar = toolBar;
		this.mainController = mainController;
		mainEventHandler = new MainEventHandler(mainController);
		//toolBar.getItems().add
		loadItems();
	}
	
	public void addItem(ToolBarItem item) {
		//items.add(item);
		toolBar.getItems().add(item.getButton());
	}
	
	private void loadItems() {
		loadFirstSegment();
		loadSecondSegment();
	}
	
	private void loadFirstSegment() {
		ToolBarItem openButton = new OpenFileButton(mainEventHandler);
		addItem(openButton);
		addSeparator();
	}
	
	private void loadSecondSegment() {
		ToolBarItem bButton = new BackwardButton(mainEventHandler);
		ToolBarItem fButton = new ForwardButton(mainEventHandler);
		addItem(bButton);
		addItem(fButton);
		addSeparator();
	}
	
	private void addSeparator() {
		Separator s = new Separator();
		s.setOrientation(Orientation.VERTICAL);
		toolBar.getItems().add(s);
	}
}
