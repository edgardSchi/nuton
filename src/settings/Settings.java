/*******************************************************************************
 * Nuton
 * Copyright (C) 2018 Edgard Schiebelbein
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package settings;

import java.io.Serializable;

public class Settings implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7678388284540350947L;
	
	
	private int motion;
	
	public static final int DIRECTION_X = 0;
	public static final int DIRECTION_Y = 1;
	public static final int NULL_X_RIGHT = 0;
	public static final int NULL_X_LEFT = 1;
	public static final int NULL_Y_TOP = 0;
	public static final int NULL_Y_BOTTOM = 1;
	
	public static final int TRANSLATION = 0;
	public static final int CIRCULAR = 1;
	
	private double schrittweite;
	private double eichung;
	
	//Translation
	private int direction;
	private int xNull;
	private int yNull;
	private boolean xFixed;
	private boolean yFixed;
	
	//Kreisbewegung
	public enum CircleDirection {GegenUhrzeiger, Uhrzeiger, Automatic};
	private CircleDirection circleDirection;
	
	public Settings() {

	}


	public double getEichung() {
		return eichung;
	}


	public void setEichung(double eichung) {
		this.eichung = eichung;
	}


	public double getSchrittweite() {
		return schrittweite;
	}


	public int getDirection() {
		return Settings.DIRECTION_X;
	}


	public int getxNull() {
		return xNull;
	}


	public int getyNull() {
		return yNull;
	}


	public void setSchrittweite(double schrittweite) {
		this.schrittweite = schrittweite;
	}

	public void setxNull(int xNull) {
		this.xNull = xNull;
	}


	public void setyNull(int yNull) {
		this.yNull = yNull;
	}


	public boolean isxFixed() {
		return xFixed;
	}


	public void setxFixed(boolean xFixed) {
		this.xFixed = xFixed;
	}


	public boolean isyFixed() {
		return yFixed;
	}


	public void setyFixed(boolean yFixed) {
		this.yFixed = yFixed;
	}
	
	public void setCircleDirection(CircleDirection c) {
		this.circleDirection = c;
	}
	
	public CircleDirection getCircleDirection() {
		return circleDirection;
	}

	
	@Override
	public String toString() {
		return "Settings [motion=" + motion + ", schrittweite=" + schrittweite + ", eichung=" + eichung + ", direction="
				+ direction + ", xNull=" + xNull + ", yNull=" + yNull + ", xFixed=" + xFixed + ", yFixed=" + yFixed
				+ ", circleDirection=" + circleDirection + "]";
	}


	public void setMotion(int motion) {
		this.motion = motion;
	}

	public int getMotion() {
		return motion;
	}
	
	public void overloadSettings(Settings newSettings) {
		this.eichung = newSettings.getEichung();
		this.schrittweite = newSettings.getSchrittweite();
		this.direction = newSettings.getDirection();
		this.xNull = newSettings.getxNull();
		this.yNull = newSettings.getyNull();
		this.xFixed = newSettings.isxFixed();
		this.yFixed = newSettings.isyFixed();
		this.circleDirection = newSettings.getCircleDirection();
		this.motion = newSettings.getMotion();
	}
}
