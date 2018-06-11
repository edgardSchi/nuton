package math;

public class Vector3 {

	private double x;
	private double y;
	private double z;
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vector3(Vector2 v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = 0;
	}
	
	/**
	 * Gibt die Länge des Vektors zur�ck.
	 * @return Die Länge des Vektors
	 */
	public double getLength() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Gibt das Skalarprodukt zweier Vektoren zur�ck.
	 * @param v1 Erster Vektor
	 * @param v2 Zweiter Vektor
	 * @return Das Skalarprodukt beider Vektoren
	 */
	public static double dotProduct(Vector3 v1, Vector3 v2) {
		return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
	}

	/**
	 * Das Kreuzprodukt zweier Vektoren.
	 * @param v1 Erster Vektor
	 * @param v2 Zweiter Vektor
	 * @return Vektor, der zu beiden Vektoren orthogonal ist
	 */
	public static Vector3 crossProduct(Vector3 v1, Vector3 v2) {
		Vector3 newVec;
		double newX = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
		double newY = -(v1.getX() * v2.getZ() - v1.getZ() * v2.getX());
		double newZ = v1.getX() * v2.getY() - v1.getY() * v2.getX();
		newVec = new Vector3(newX, newY, newZ);
		return newVec;
	}
	
	/**
	 * Erzeugt einen neuen Vektor, der zwischen den beiden Vektoren liegt.
	 * V1 - V2
	 * @param v1 Erster Vektor
	 * @param v2 Zweiter Vektor
	 * @return Neuer Vektor zwischen den Vektoren
	 */
	public static Vector3 subtract(Vector3 v1, Vector3 v2) {
		double newX = v1.getX() - v2.getX();
		double newY = v1.getY() - v2.getY();
		double newZ = v1.getZ() - v2.getZ();
		return new Vector3(newX, newY, newZ);
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	@Override
	public String toString() {
		return "Vector3 [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	
	
}
