public abstract class Surfaces {
	type myType;
	public int material_index;

	public enum type {
		triangle, sphere, infinitePlane
	};

	public abstract type getType();

	public abstract double getIntersection(Point p, Vector dir);

	public abstract String toString();


}