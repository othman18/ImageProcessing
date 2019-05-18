package myUtils;

/*not finished */

public class Camera {

	Point position, lookAtPoint;
	Vector upVector;
	double screenDistance, screenWidth;
	int backgroundColor; 
	// RGB
	// Number of shadow rays
	// Maximum recursion level
	// Super sampling level
	Vector cameraDirection,x_Axis,fixedUpVector;

	public Camera(Point position, Point lookAtPoint, Vector upVector,
			double screenDistance, double screenWidth) {
		this.position = position;
		this.lookAtPoint = lookAtPoint;
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
		translateAxises();
		
	}
	
	/**cameraDirection represents the z-axis
	 * fixedUpVector represents the y-axis*/
	private void translateAxises() {
		// TODO Auto-generated method stub
		cameraDirection=new Vector(position,lookAtPoint);
		cameraDirection=Vector.normalized(cameraDirection);
		x_Axis=Vector.crossProduct(cameraDirection,upVector);
		fixedUpVector=Vector.crossProduct(x_Axis, cameraDirection);
		
		
	}
	
	public Point findStartPoint(double distance,int width,int height){
		Point p0=Point.findPoint(position,cameraDirection,distance);
		p0=new Point(p0.x-width/2,p0.y-height/2,p0.z);
		return p0;
	}


	@Override
	public String toString() {
		return "position="+position+", lookAtPoint="+lookAtPoint+", upVector="+upVector+
				", screenDistance="+screenDistance+", screenWidth="+screenWidth;

	}

}
