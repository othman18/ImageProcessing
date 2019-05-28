
public class Camera {
	Point position, lookAtPoint;
	public double screenDistance, screenWidth, screenHeight;
	double imageWidth, imageHeight;
	public Vector cameraDirection, upVector;
	public Vector xAxis, yAxis;// this is the the screen's normalized axes

	public Camera(Point position, Point lookAtPoint, Vector upVector, double screenDistance, double screenWidth,
			int width, int height) {
		this.position = position;
		this.lookAtPoint = lookAtPoint;
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
		this.imageWidth = (double) width;
		this.imageHeight = (double) height;
		this.screenHeight = screenWidth * (imageHeight / imageWidth);
		translateAxises();
	}

	/**
	 * calculate the camera's position and translate it using the newly defined x
	 * and y axes
	 */
	private void translateAxises() {
		cameraDirection = new Vector(position, lookAtPoint);
		cameraDirection.normalise();
		xAxis = Vector.crossProduct(upVector, cameraDirection);
		yAxis = Vector.crossProduct(cameraDirection, xAxis);
	}

	/** finding the screen's origin */
	public Point findLeftLowerPoint() {
		Point p0 = Point.findPoint(position, cameraDirection, screenDistance);
		p0 = Point.findPoint(p0, yAxis, -screenHeight / 2);
		p0 = Point.findPoint(p0, xAxis, -screenWidth / 2);
		return p0;
	}

}
