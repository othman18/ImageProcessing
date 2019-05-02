package surfaces;

public abstract class Surfaces {
	type myType;
	enum type {triangle,sphere,infinitePlane};
	
	public void setType(type t){ myType = t; }
	public type getType(){ return myType; }
	
}

