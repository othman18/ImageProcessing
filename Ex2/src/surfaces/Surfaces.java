package surfaces;

public abstract class Surfaces {
	type myType;
	enum type {triangle,sphere,infinitePlane};
	
	public abstract type getType();
	public abstract  String toString();

}
