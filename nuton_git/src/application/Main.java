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
	
	private static final String VERSION = "1.0";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			propWriter = new PropertiesWriter();
			stage = primaryStage;
			stage.getProperties().put("hostServices", this.getHostServices());
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Main.fxml"));
			Parent root = (Parent) loader.load();
			MainController controller = (MainController) loader.getController();
			Scene scene = new Scene(root);
			themeLoader = controller.getThemeLoader();
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
			
			
			controller.setMainStage(primaryStage);
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
	
	public static String getVersion() {
		return VERSION;
	}

}
