package myUtils;

/*not finished */

public class Camera {

	Point position, lookAtPoint;
	Vector upVector;
	public double screenDistance, screenWidth,screenHeight;
	// RGB
	// Number of shadow rays
	// Maximum recursion level
	// Super sampling level
	public Vector cameraDirection, x_Axis, fixedUpVector;

	public Camera(Point position, Point lookAtPoint, Vector upVector, double screenDistance, double screenWidth) {
		this.position = position;
		this.lookAtPoint = lookAtPoint;
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
		translateAxises();

	}

	/**
	 * cameraDirection represents the z-axis fixedUpVector represents the y-axis
	 */
	private void translateAxises() {
		cameraDirection = new Vector(position, lookAtPoint);
		cameraDirection.normalise();
		x_Axis = Vector.crossProduct(cameraDirection, upVector);
		fixedUpVector = Vector.crossProduct(x_Axis, cameraDirection);
	}

	public Point findStartPoint(double distance, int width, int height) {
		screenHeight = (height/width)*this.screenWidth;
		Point p0 = Point.findPoint(position, cameraDirection, screenDistance);
		/*change from minus to plus in p0*/
		p0 = new Point(p0.x - screenWidth / 2, p0.y + screenHeight / 2, p0.z);
		return p0;
	}

	public double getDistance() {
		return screenDistance;
	}

	@Override
	public String toString() {
		return "position=" + position + ", lookAtPoint=" + lookAtPoint + ", upVector=" + upVector + ", screenDistance="
				+ screenDistance + ", screenWidth=" + screenWidth;

	}

}
