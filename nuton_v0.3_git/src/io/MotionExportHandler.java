package io;

import java.util.ArrayList;

import application.PixelManager;
import application.Point;
import javafx.stage.FileChooser;

public abstract class MotionExportHandler {

	protected FileChooser fileChooser;
	protected ArrayList<Point> points;
	protected PixelManager pManager;
	
	public MotionExportHandler(ArrayList<Point> points, PixelManager pManager) {
		this.points = points;
		this.pManager = pManager;
	}
	
	public abstract void exportData();
	
	
}
