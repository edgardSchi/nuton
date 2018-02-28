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
	private boolean gegenUZS;
	
	
	public Settings() {
//		this.direction = direction;
//		this.schrittweite = schrittweite;
//		this.eichung = eichung;
//		this.xNull = xNull;
//		this.yNull = yNull;
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
		return direction;
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


	public void setDirection(int direction) {
		this.direction = direction;
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
	
	public void setGegenUhrzeiger(boolean uhrzeiger) {
		this.gegenUZS = uhrzeiger;
	}
	
	public boolean gegenUhrzeiger() {
		return gegenUZS;
	}


	@Override
	public String toString() {
		return "Settings [schrittweite=" + schrittweite + ", eichung=" + eichung + ", direction=" + direction
				+ ", xNull=" + xNull + ", yNull=" + yNull + ", xFixed=" + xFixed + ", yFixed=" + yFixed + "]";
	}
	
	public void setMotion(int motion) {
		this.motion = motion;
	}

	public int getMotion() {
		return motion;
	}
}
