package waveGenerator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import userSettings.ThemeLoader;

public class WaveGeneratorController {
	
	@FXML
	private Button startBtn;
	@FXML
	private Canvas canvas;
	@FXML private AnchorPane canvasPane;
	@FXML private Slider velSlider;
	@FXML private Slider ampSlider;
	@FXML private Slider periodSlider;
	private GraphicsContext gc;
	private WaveGenerator waveGen;
	private Stage stage;
	private Thread t;
	

	public WaveGeneratorController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WaveGeneratorFrame.fxml"));
		loader.setController(this);
		try {
			//loader.load();
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			ThemeLoader themeLoader = new ThemeLoader();
			scene.getStylesheets().add(themeLoader.getTheme());
			gc = canvas.getGraphicsContext2D();
			waveGen = new WaveGenerator(gc, canvas);
			t = (new Thread(waveGen));
			t.start();
			stage = new Stage();
			stage.setResizable(true);
			stage.setTitle("Welle");
			stage.setScene(scene);
			//stage.getIcons().add(new Image(WaveGeneratorController.class.getResourceAsStream("Nuton_logo.png")));

			stage.show();
			canvasPane.widthProperty().addListener(observable -> updateWidth());
			canvasPane.heightProperty().addListener(observable -> updateHeight());
			velSlider.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	            	waveGen.setVel(velSlider.getValue()/100);
	            }
	        });
			
			ampSlider.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	            	waveGen.setAmplitude(ampSlider.getValue()*10);
	            }
	        });
			
			periodSlider.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	            	waveGen.setPeriod(periodSlider.getValue()*10);
	            }
	        });
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				startBtnPress();
			}
			
		});
		
		stage.setOnCloseRequest(new EventHandler <WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				if (t != null) {
					waveGen.setUpdate(false);
					try {
						waveGen.kill();
						t.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
	}
	
	
	
	
	
	private void startBtnPress() {
		if (waveGen.isUpdating()) {
			waveGen.setUpdate(false);
			startBtn.setText("Start");
		} else {
			waveGen.setUpdate(true);
			startBtn.setText("Stop");
		}

	}
	
	private void resizeCanvas() {
//		DoubleProperty canvasW = canvas.widthProperty();
//		DoubleProperty canvasH = canvas.heightProperty();
//		canvasW.bind(Bindings.selectDouble(canvasPane.parentProperty(), "width"));
//		canvasH.bind(Bindings.selectDouble(canvasPane.parentProperty(), "height"));
		canvas.setWidth(canvasPane.getWidth());
	}
	
	private void updateWidth() {
		canvas.setWidth(canvasPane.getWidth());
	}
	
	private void updateHeight() {
		canvas.setHeight(canvasPane.getHeight());
		waveGen.setYOffset(canvas.getHeight()/2);
	}
	
}
