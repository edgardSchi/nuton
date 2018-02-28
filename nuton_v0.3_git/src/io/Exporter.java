package io;

import java.util.ArrayList;

import application.PixelManager;
import application.Point;

public class Exporter {
	
	private TranslationExportHandler translationHandler;
	private CircExportHandler circHandler;
	
	private int handlerID;
	
	public Exporter(ArrayList<Point> points, PixelManager pManager) {
		translationHandler = new TranslationExportHandler(points, pManager);
		circHandler = new CircExportHandler(points, pManager);
		handlerID = pManager.getSettings().getMotion();
	}
	
	public void exportData() {
		if(handlerID == 0) {
			translationHandler.exportData();
		}
		if(handlerID == 1) {
			circHandler.exportData();
		}

	}
}
