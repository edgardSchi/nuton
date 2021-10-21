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
package de.nuton.application;
	

import de.nuton.properties.PropertiesWriter;
import de.nuton.userSettings.ThemeLoader;
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


public class MainFX extends Application {
	
	@FXML private MediaView mv;
	@FXML private Canvas canvas;
	private ThemeLoader themeLoader;
	private static Stage stage;
	@SuppressWarnings("unused")
	private PropertiesWriter propWriter;
	
	public static final String VERSION = "2.0";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			propWriter = new PropertiesWriter();
			stage = primaryStage;
			stage.getProperties().put("hostServices", this.getHostServices());
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
			Parent root = (Parent) loader.load();
			MainController controller = (MainController) loader.getController();
			Scene scene = new Scene(root);
			themeLoader = controller.getThemeLoader();
			scene.getStylesheets().add(themeLoader.getTheme());
			primaryStage.setTitle("Nuton " + VERSION);

			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.getIcons().add(new Image(MainFX.class.getResourceAsStream("/nutonLogo.png")));
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
		launch(MainFX.class, args);
	}
	
	//Lädt die OpenCV-Lib, ist rausgenommen
//	private static void loadLibraries() {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//	}
	
	public static Stage getStage() {
		return stage;
	}

}
