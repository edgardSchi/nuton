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
import math.Vector2;
import properties.PropertiesWriter;
import userSettings.ThemeLoader;


public class Main extends Application {
	
	@FXML private MediaView mv;
	@FXML private Canvas canvas;
	private ThemeLoader themeLoader;
	private static Stage stage;
	private PropertiesWriter propWriter;
	
	private final String VERSION = "v0.5 - Zweite Testversion für den Unterricht";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			propWriter = new PropertiesWriter();
			stage = primaryStage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Main.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			themeLoader = new ThemeLoader();
			scene.getStylesheets().add(themeLoader.getTheme());
			primaryStage.setTitle("nuton " + VERSION);

			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/nutonLogo.png")));
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent t) {
	                Platform.exit();
	                System.exit(0);
	            }
	        });
			
			MainController controller = (MainController) loader.getController();
			controller.setMainStage(primaryStage);
			
			Vector2 v1 = new Vector2(2, 0);
			Vector2 v2 = new Vector2(0, 2);
			System.out.println("Vektortest DOT: " + Vector2.getAngle(v1, v2));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getStage() {
		return stage;
	}

}
