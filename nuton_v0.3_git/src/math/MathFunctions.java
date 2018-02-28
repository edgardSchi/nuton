package math;

import java.util.ArrayList;

public class MathFunctions {

	public static double degreeToRadian(double degree) {
		return (Math.PI / 180) * degree;
	}
	
	public static Vector2 kartToPolar(Vector2 v) {
		double r = Math.sqrt(v.getX() * v.getX() + v.getY() + v.getY());
		double phi = Math.atan2(v.getY(), v.getX());
		return new Vector2(r, phi);
	}
	
	public static boolean isAgainstClock(ArrayList<Vector2> vectors) {
		boolean result = true;
		ArrayList<Vector2> polarVectors = new ArrayList<Vector2>();
		for(int i = 0; i < vectors.size(); i++) {
			polarVectors.add(kartToPolar(vectors.get(i)));
		}
		
		for(int i = 0; i < polarVectors.size() - 1; i++) {
			double phi = polarVectors.get(i + 1).getY() - polarVectors.get(i).getY();
			System.out.println("Winkel zwischen Vektoren: " + phi);
		}
		return result;
	}
	
}
