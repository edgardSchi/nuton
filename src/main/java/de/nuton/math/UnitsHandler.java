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
package de.nuton.math;

import de.nuton.settings.Settings;

/*
 * Klasse, die für das Umwandeln von verschiedenen physikalischen Einheiten zuständig ist.
 */
public class UnitsHandler {
	
	public enum TimeUnit {MYS, MS, S, M, H};
	public enum LengthUnit {MIM, MM, CM, DM, M, KM};

	public static double convertTimeToMilliseconds(double time, TimeUnit unit) {
		double newTime = 0;
		switch(unit) {
		case MYS:
			newTime = time * Math.pow(10, -6);
			break;
		case MS:
			newTime = time * Math.pow(10, -3);
			break;
		case S:
			newTime = time;
			break;
		case M:
			newTime = time * 60;
			break;
		case H:
			newTime = time * 60 * 60;
			break;
		}
		return newTime * 1000;
	}
	
	public static double convertTimeToMilliseconds(double time, Settings settings) {
		return convertTimeToMilliseconds(time, settings.getTimeUnit());
	}
	
	public static double convertLengthToMeters(double length, LengthUnit unit) {
		double newLength = 0;
		switch(unit) {
		case MIM:
			newLength = length * Math.pow(10, -6);
			break;
		case MM:
			newLength = length * Math.pow(10, -3);
			break;
		case CM:
			newLength = length / 100;
			break;
		case DM:
			newLength = length / 10;
			break;
		case M:
			newLength = length;
			break;
		case KM:
			newLength = length * 1000;
			break;
		}
		return newLength;
	}
	
	public static double convertLengthToMeters(double length, Settings settings) {
		return convertLengthToMeters(length, settings.getLengthUnit());
	}
	
	public static double convertMetersToOtherUnit(double length, LengthUnit unit) {
		double newLength = 0;
		switch(unit) {
		case MIM:
			newLength = length * Math.pow(10, 6);
			break;
		case MM:
			newLength = length * Math.pow(10, 3);
			break;
		case CM:
			newLength = length * 100;
			break;
		case DM:
			newLength = length * 10;
			break;
		case M:
			newLength = length;
			break;
		case KM:
			newLength = length / 1000;
			break;
		}
		return newLength;
	}
	
}
