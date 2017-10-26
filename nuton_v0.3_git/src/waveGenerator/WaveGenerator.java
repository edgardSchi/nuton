package waveGenerator;

import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WaveGenerator implements Runnable {

	private GraphicsContext gc;
	private Canvas canvas;
	
	private Double[] yValues;
	private final int size = 10;
	private double angle = 0;
	private double v = 0.01;
	private double amplitude = 100;
	private double dx;
	private double period = 50;
	private double yOffset;
	
	//Thread
	private volatile boolean running;
	private int FPS = 30;
	private long targetTime = 1000 / FPS;
	
	private boolean update;
	private Random random;
	
	public WaveGenerator(GraphicsContext gc, Canvas canvas) {
		random = new Random();
		this.gc = gc;
		this.canvas = canvas;
		yOffset = canvas.getHeight() / 2;
		yValues = new Double[200];
//		for (int i = 0; i < yValues.length; i++) {
//			yValues[i] = yOffset;
//		}
		running = true;
		update = false;
		calcWave();
		drawWave();
	}

	@Override
	public void run() {
		
		long start;
		long elapsed;
		long wait;
		
		while(running) {
			
			start = System.nanoTime();
			
			//System.out.println("Thread running");
			if (update) {
				calcWave();
				drawWave();
			}
						
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void calcWave() {
		dx = (Math.PI * 2 / period);
		angle += v;
		
		double x = angle;
		for (int i = 0; i < yValues.length; i++) {
			yValues[i] = Math.tan(x) * amplitude;	
			x+=dx; 
		}
	}

	public void drawWave() {	
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setFill(Color.WHITE);
		for (int i = 0; i < yValues.length; i++) {
			if (i * size <= canvas.getWidth()) {
				//gc.setFill((Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255))));
				if (yValues[i] == null) {
					//gc.fillOval(i * size, yOffset, size, size);
				} else {
					gc.fillOval(i * size, yValues[i] + yOffset, size, size);
				}
			}
		}
	}
	
	public void setUpdate(boolean r) {
		update = r;
	}
	
	public boolean isUpdating() {
		return update;
	}
	
	public void kill() {
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setVel(double vel) {
		v = vel;
	}
	
	public void setAmplitude(double amp) {
		amplitude = amp;
	}
	
	public void setYOffset(double offset) {
		yOffset = offset;
	}
	
	public void setPeriod(double period) {
		this.period = period;
	}
	
}