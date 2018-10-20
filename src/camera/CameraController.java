package camera;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class CameraController {

	private Canvas canvas;
	
	private VideoCapture videoCapture;
	private GraphicsContext g2d;
	private AnimationTimer timer;
	private int retrieveFreq;
	private Image frame;
	private Mat mat;
	
	private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0 ;
    private boolean arrayFilled = false ;
	
	public CameraController(Canvas canvas) {
		initOpenCV();
		
		this.canvas = canvas;
		
		g2d = canvas.getGraphicsContext2D();
		
		videoCapture = new VideoCapture();
		videoCapture.open(0);
		videoCapture.set(Videoio.CAP_DSHOW, 0);
		int fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G');
	    videoCapture.set(Videoio.CAP_PROP_FOURCC, fourcc);
	    videoCapture.set(Videoio.CAP_PROP_FPS,30);
	    
//	    boolean wset = videoCapture.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, 1280);
//	    boolean hset = videoCapture.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, 720);

		System.out.println(videoCapture.get(Videoio.CV_CAP_PROP_FRAME_WIDTH));
		System.out.println(videoCapture.get(Videoio.CV_CAP_PROP_FRAME_HEIGHT));

		timer = new AnimationTimer() {
			Mat mat = new Mat();
			int counter = 0;
			
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
	                    double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
	                    System.out.println(String.format("Current frame rate: %.3f", frameRate));
	                }

	                if(counter == 5) {
						videoCapture.read(mat);
						
						Image image = mat2Image(mat);
						
						frame = image;
						//g2d.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
						counter = 0;
	                }
	
					
					counter++;
			}
		};
	}
	
	public Image grabFrame() {
		mat = new Mat();
		videoCapture.read(mat);
		//System.out.println("Bildgröße: " + mat.cols() + " x " + mat.rows());
		
		Image image = mat2Image2(mat);
		
		return image;
	}
	
	public void startCamera() {
		videoCapture = new VideoCapture(0);
		videoCapture.open(0);

		
//		canvas.widthProperty().bind(pane.widthProperty());
		
		System.out.println("Camera is open: " + videoCapture.isOpened());
		timer.start();
	}
	
	public void stopCamera() {
		videoCapture.release();
		timer.stop();
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		System.out.println("Camera is closed: " + !videoCapture.isOpened());
	}
	
	private void initOpenCV() {
		
//		setLibraryPath();
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		
//		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//			public void handle(WindowEvent we) {
//				timer.stop();
//				videoCapture.release();
//				
//				System.out.println("Camera released");
//			}
//		});
	}
	
	public static Image mat2Image(Mat mat) {
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".jpg", mat, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
	
	public static Image mat2Image2(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  
        return SwingFXUtils.toFXImage(image, null);
	}
	
	public void prepareCanvas(AnchorPane pane) {

	}
	
	public boolean isRunning() {
		if(videoCapture == null) {
			return false;
		}
		return videoCapture.isOpened();
	}
	
	public Image getFrame() {
		return frame;
	}
	
	public void setRetrieveFreq(int f) {
		
	}
	
	public int getCameraWidth() {
		return (int) videoCapture.get(Videoio.CV_CAP_PROP_FRAME_WIDTH);
	}
	
	public int getCameraHeight() {
		return (int) videoCapture.get(Videoio.CV_CAP_PROP_FRAME_HEIGHT);
	}
}
