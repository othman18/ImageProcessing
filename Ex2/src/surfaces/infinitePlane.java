package surfaces;

public class InfinitePlane extends Surfaces{
	
	public InfinitePlane() {
		System.out.println("should define the normal and offset");
	}

	public type getType() {
		return type.infinitePlane;
	}
	
	@Override
	public String toString() {
		return "IP.: N=?, P=?";
	}


}
