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
package math;

import java.util.ArrayList;

public class MathFunctions {

	/**
	 * Wandelt Winkel vom Gradmaß ins Bogenmaß.
	 * @param degree Winkel, der umzuwandeln ist
	 * @return Der Winkel im Bogenmaß
	 */
	public static double degreeToRadian(double degree) {
		return (Math.PI / 180) * degree;
	}
	
	/**
	 * Wandelt die Koordinaten eines 2 dimensionalen Vektors von kartesischen zu polaren um.
	 * @param v Vektor, der umzuwandeln ist
	 * @return Der gleiche Vektor in mit polaren Koordinaten
	 */
	public static Vector2 kartToPolar(Vector2 v) {
		double r = Math.sqrt(v.getX() * v.getX() + v.getY() + v.getY());
		double phi = Math.atan2(v.getY(), v.getX());
		return new Vector2(r, phi);
	}
	
	/**
	 * Üperprüft, ob die Vektoren gegen den Uhrzeigersinn gesetzt wurden.
	 * @param vectors Liste der Vektoren
	 * @return 
	 */
	public static boolean isAgainstClock(ArrayList<Vector2> vectors) {
		int pos = 0;
		int neg = 0;
		Vector3 test1 = new Vector3(3, 2, 0);
		Vector3 test2 = new Vector3(2, 3, 0);
		Vector3 testDiff = Vector3.subtract(test2, test1);
		Vector3 testCross = Vector3.crossProduct(testDiff, test1);
		System.out.println("TEST: " + testCross.toString());
		for(int i = 0; i < vectors.size()- 1; i++) {
			System.out.println("Vektor1: " + vectors.get(i).toString());
			System.out.println("Vektor2: " + vectors.get(i + 1).toString());
			Vector3 diff = new Vector3(Vector2.subtract(vectors.get(i + 1), vectors.get(i)));
			System.out.println("Subvektor: " + diff.toString());
			Vector3 v1 = new Vector3(vectors.get(i));
			System.out.println(v1.toString());
			Vector3 cross = Vector3.crossProduct(diff, v1);
			System.out.println(cross.toString());
			if(cross.getZ() >= 0) {
				pos++;
			} else {
				neg++;
			}
		}
		return (pos >= neg);
	}
}
