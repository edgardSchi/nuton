package states.stream;

import application.MainController;
import application.Point;
import camera.CameraController;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import states.PointState;
import tracking.TrackingManager;

public class StreamState extends PointState {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CameraController camera;
	private Canvas canvas;
	private AnimationTimer timer;
	private GraphicsContext g2dStream;
	private Image frame = null;
	double frameRate = 0;
	private TrackingManager trackingManager;
	private boolean trackingReady = false;
	
	
	private static final int FRAMESKIP = 6;
	
	public StreamState(MainController mainController) {
		super(mainController);
		canvas = mainController.getStreamCanvas();
		g2dStream = canvas.getGraphicsContext2D();
		camera = new CameraController(canvas);
		trackingManager = new TrackingManager(mainController, mainController.getThemeLoader(), 5, 3, 1);
		
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
                
                if(counter % FRAMESKIP == 0) {
                	frame = camera.grabFrame();
                	redraw();
                }
				
                if(trackingReady == true && counter % (2 *FRAMESKIP) == 0) {
                	//System.out.println("Track");
                	trackingManager.track(frame);
                	counter = 0;
                }
				
				counter++;
			}
			
		};
	}

	@Override
	public void init() {
		camera = new CameraController(canvas);
//		trackingManager = new TrackingManager(mainController, mainController.getThemeLoader(), 7, 2, 1);
		initCanvasBounds();
		timer.start();
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
		mainController.getScalingManager().setMediaDimension(camera.getCameraWidth(), camera.getCameraHeight());
	}

	@Override
	public void onClick(MouseEvent e) {
		mainController.getToolBarManager().pointButtonEvent(this, e);
		if(points.size() != 0) {
			trackingManager.selectTrackingPoint(frame, points.get(0).getDrawX(), points.get(0).getDrawY());
			trackingReady = true;
		}

	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fertigBtnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
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
		camera.stopCamera();
	}
	
}
