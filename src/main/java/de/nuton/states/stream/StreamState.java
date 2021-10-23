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
package de.nuton.states.stream;

import de.nuton.application.MainController;
import de.nuton.application.Point;
import de.nuton.states.PointState;
import de.nuton.application.ScalingManager;
import de.nuton.tracking.TrackingManager;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Klasse zuständig für Livecamera, wurde rausgenommen (fürs erste)
 * @author Edgard
 *
 */
/*
public class StreamState extends PointState {

	private static final long serialVersionUID = 1L;

	//private CameraController camera;
	private Canvas canvas;
	private AnimationTimer timer;
	private GraphicsContext g2dStream;
	private Image frame = null;
	double frameRate = 0;
	private TrackingManager trackingManager;
	private boolean trackingReady = false;
	private boolean readyForNextFrame = false;
	
	//Test
	private static double x = 0;
	private static double y = 0;
	private static double dragX = 0;
	private static double dragY = 0;
	private static double x2 = 0;
	private static double y2 = 0;
	private static boolean leftClicked = false;
	
	private int rectangleWidth = 0;
	private int rectangleHeight = 0;
	
	
	private boolean playing = true;
	
	private static final int FRAMESKIP = 6;
	
	public StreamState(MainController mainController) {
		super(mainController);
		canvas = mainController.getStreamCanvas();
		g2dStream = canvas.getGraphicsContext2D();
		//camera = new CameraController(canvas);

		
		timer = new AnimationTimer() {
			final long[] frameTimes = new long[100];
		    int frameTimeIndex = 0 ;
		    boolean arrayFilled = false ;
		    short counter = 0;

			@Override
			public void handle(long now) {
				long oldFrameTime = frameTimes[frameTimeIndex] ;
                frameTimes[frameTimeIndex] = now ;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
                if (frameTimeIndex == 0) {
                    arrayFilled = true ;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime ;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                    frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;       
                    //System.out.println(String.format("Current frame rate: %.3f", frameRate));
                }
                
                if((counter % FRAMESKIP == 0 && readyForNextFrame == true) || trackingReady == false) {
                	//frame = camera.grabFrame();
                	redraw();
                	readyForNextFrame = false;
                }
				
                if(trackingReady == true && counter % (2 *FRAMESKIP) == 0) {
                	//System.out.println("Track");
                	trackingManager.track(frame);
                	readyForNextFrame = true;
                	counter = 0;
                }
				
				counter++;
			}
			
		};
	}

	@Override
	public void init() {
		//camera = new CameraController(canvas);
		trackingManager = new TrackingManager(mainController, mainController.getThemeLoader(), 5, 3, false);
		initCanvasBounds();
		timer.start();
		playing = true;
	}
	
	private void initCanvasBounds() {
		DoubleProperty canvasSW = canvas.widthProperty();
		DoubleProperty canvasSH = canvas.heightProperty();
		canvasSW.bind(Bindings.selectDouble(mainController.getStackPane().widthProperty()));
		canvasSH.bind(Bindings.selectDouble(mainController.getStackPane().heightProperty()));
		DoubleProperty canvasW = mainController.getCanvas().widthProperty();
		DoubleProperty canvasH = mainController.getCanvas().heightProperty();
		canvasW.bind(Bindings.selectDouble(mainController.getStackPane().widthProperty()));
		canvasH.bind(Bindings.selectDouble(mainController.getStackPane().heightProperty()));
		//mainController.getScalingManager().setMediaDimension(camera.getCameraWidth(), camera.getCameraHeight());
	}

	@Override
	public void onClick(MouseEvent e) {
		
//		if (points.size() == 0 && e.getEventType() == MouseEvent.MOUSE_CLICKED) {	
//			mainController.getToolBarManager().pointButtonEvent(this, e);
//			trackingManager.selectTrackingPoint(frame, points.get(0).getX(), points.get(0).getY());
//			//createRectangle(e);
//			trackingReady = true;
//		}
		
		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {	
			if(playing) {
				timer.stop();
				System.out.println("Stop");
				playing = false;
			} else {
				timer.start();
				System.out.println("Play");
				playing = true;
			}
		}
	}
	
	private void track() {
		
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
		g2dStream.drawImage(frame, 0, 0, canvas.getWidth(), canvas.getHeight());
		g2dStream.setStroke(Color.RED);
		g2dStream.fillText(Integer.toString((int)frameRate), 20, 20);
		gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
		for (Point p : points) {
			p.drawPoint(gc);
		}
	}

	@Override
	public void onKill() {
		timer.stop();
		//camera.stopCamera();
	}
	
	private void createRectangle(MouseEvent e) {
		gc.setStroke(Color.RED);
		gc.setFill(Color.rgb(255, 119, 0, 0.80));		
		
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.isPrimaryButtonDown() && !e.isSecondaryButtonDown()) {
			x = e.getX();
			y = e.getY();
			leftClicked = true;
		}		
		
		if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.isPrimaryButtonDown()) {
			gc.setStroke(Color.RED);
			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			for(Point p : points) {
				p.drawPoint(gc);
			}
			gc.setStroke(Color.RED);
			
			if (e.isSecondaryButtonDown()) {			
				x = x - dragX + e.getX();
				x2 = x2 - dragX + e.getX();
				y = y - dragY + e.getY();
				y2 = y2 - dragY + e.getY();
			} else {
				x2 = e.getX();
				y2 = e.getY();
			}
			
			gc.strokeLine(x, y, x2, y);
			gc.strokeLine(x2, y, x2, y2);
			gc.strokeLine(x, y, x, y2);
			gc.strokeLine(x, y2, x2, y2);
			
			//Kreuz
			gc.strokeLine(((x2 - x) / 2 + x) - 5, ((y2 - y) / 2 + y), ((x2 - x) / 2 + x) + 5, ((y2 - y) / 2 + y));
			gc.strokeLine(((x2 - x) / 2 + x), ((y2 - y) / 2 + y) - 5, ((x2 - x) / 2 + x), ((y2 - y) / 2 + y) + 5);
			
			//Diagonalen
//			gc.strokeLine(e.getX(), e.getY(), me.getX(), me.getY());
//			gc.strokeLine(me.getX(), e.getY(), e.getX(), me.getY());
			dragX = e.getX();
			dragY = e.getY();	
		}
		
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED && !e.isPrimaryButtonDown() && leftClicked) {
			rectangleWidth = (int)(x2 - x);
			rectangleHeight = (int)(y2 - y);
			//int[] cords = ScalingManager.getInstance().getCordRelativeToMedia((int)x, (int)y);
			//trackingManager.calibrateKernel(rectangleWidth, rectangleHeight);
			//trackingManager.selectTrackingPoint(frame, cords[0], cords[1]);
			gc.clearRect(0, 0, mainController.getCanvas().getWidth(), mainController.getCanvas().getHeight());
			for(Point p : points) {
				p.drawPoint(gc);
			}
			leftClicked = false;
		}
	}

	@Override
	public void onUnpause() {

		
	}
	
}
*/
