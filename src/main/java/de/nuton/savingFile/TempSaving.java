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
package de.nuton.savingFile;

public class TempSaving {

	private static boolean ffmpeg = false;
	private static String videoURL;
	private static boolean alreadySaved = false;
	private static String savingPath;
	private static boolean showPoints = true;
	private static boolean showDistance = false;
	
	public static void withFfmpeg(boolean used) {
		ffmpeg = used;
	}
	
	public static void setURL(String path) {
		videoURL = path;
	}

	public static boolean isFfmpeg() {
		return ffmpeg;
	}

	public static String getVideoURL() {
		return videoURL;
	}

	public static boolean isAlreadySaved() {
		return alreadySaved;
	}

	public static void setAlreadySaved(boolean alreadySaved) {
		TempSaving.alreadySaved = alreadySaved;
	}

	public static String getSavingPath() {
		return savingPath;
	}

	public static void setSavingPath(String savingPath) {
		TempSaving.savingPath = savingPath;
	}

	public static boolean isShowPoints() {
		return showPoints;
	}

	public static void setShowPoints(boolean showPoints) {
		TempSaving.showPoints = showPoints;
	}

	public static boolean isShowDistance() {
		return showDistance;
	}

	public static void setShowDistance(boolean showDistance) {
		TempSaving.showDistance = showDistance;
	}
	
	
	
}
