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
package plugins;

import application.MainController;
import application.PixelManager;
import application.ScalingManager;
import application.settingsPane.SettingsPaneController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import states.StateManager;

public abstract class PMInterface {

	private MainController mainController;
	
	public PMInterface(MainController mainController) {
		this.mainController = mainController;
	}
	
	public Canvas getCanvas() {
		return mainController.getCanvas();
	}
	
	public Button getFinishButton() {
		return mainController.getFertigBtn();
	}
	
	public GraphicsContext getGraphicsContext() {
		return mainController.getGc();
	}
	
	public Media getMedia() {
		return mainController.getMedia();
	}
	
	public double getMediaLength() {
		return mainController.getMediaLength();
	}
	
	public MediaView getMediaView() {
		return mainController.getMv();
	}
	
	public MediaPlayer getMediaPlayer() {
		return mainController.getPlayer();
	}
	
	public PixelManager getPixelManager() {
		return mainController.getPManager();
	}
	
	public Button getRestartButton() {
		return mainController.getRestartBtn();
	}
	
	public ScalingManager getScalingManager() {
		return mainController.getScalingManager();
	}
	
	public StateManager getStateManager() {
		return mainController.getStateManager();
	}
	
	public void addMotionSettingsPane(SettingsPaneController spc) {
		
	}
 }
