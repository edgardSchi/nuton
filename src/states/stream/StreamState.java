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
import states.State;
import toolBarEvents.AddPointEvents;

public class StreamState extends State {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CameraController camera;
	private Canvas canvas;
	private Image frame;
	private AnimationTimer timer;
	private GraphicsContext g2d;
	double frameRate = 0;
	
	public StreamState(MainController mainController) {
		super(mainController);
		canvas = mainController.getCanvas();
		g2d = canvas.getGraphicsContext2D();
		camera = new CameraController(canvas);
		
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
                
                if(counter == 6) {
                	redraw();
                	counter = 0;
                }
				
				
				counter++;
			}
			
		};
	}

	@Override
	public void init() {
		DoubleProperty canvasW = canvas.widthProperty();
		DoubleProperty canvasH = canvas.heightProperty();
		canvasW.bind(Bindings.selectDouble(mainController.getStackPane().widthProperty()));
		canvasH.bind(Bindings.selectDouble(mainController.getStackPane().heightProperty()));
		timer.start();
	}

	@Override
	public void onClick(MouseEvent e) {
		AddPointEvents.addRectangle(this, e);
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
		g2d.drawImage(camera.grabFrame(), 0, 0, canvas.getWidth(), canvas.getHeight());
		g2d.fillText(Integer.toString((int)frameRate), 20, 20);
		for(Point p : this.getPoints()) {
			p.drawPoint(g2d);
		}
	}
	
}
