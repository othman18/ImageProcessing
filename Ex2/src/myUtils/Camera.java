package myUtils;

public class Camera {

	Point position, lookAtPoint;
	Vector upVector;
	double screenDistance, screenWidth;
	int backgroundColor; // RGB
	// Number of shadow rays
	// Maximum recursion level
	// Super sampling level

	public Camera(Point position, Point lookAtPoint, Vector upVector,
			double screenDistance, double screenWidth) {
		this.position = position;
		this.lookAtPoint = lookAtPoint;
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
		
	}

	@Override
	public String toString() {
		return "position="+position+", lookAtPoint="+lookAtPoint+", upVector="+upVector+
				", screenDistance="+screenDistance+", screenWidth="+screenWidth;

	}

}
