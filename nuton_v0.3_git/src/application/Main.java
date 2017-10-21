package application;
	

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import userSettings.ThemeLoader;


public class Main extends Application {
	
	@FXML private MediaView mv;
	@FXML private Canvas canvas;
	private ThemeLoader themeLoader;
	
	private final String VERSION = "v0.3.1";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
			Scene scene = new Scene(root);
			themeLoader = new ThemeLoader();
			scene.getStylesheets().add(themeLoader.getTheme());
			primaryStage.setTitle("nuton " + VERSION);

			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("Nuton_logo.png")));
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent t) {
	                Platform.exit();
	                System.exit(0);
	            }
	        });
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

}
