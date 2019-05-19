import java.util.List;

import myUtils.*;
import surfaces.*;

public class Assistant {
	/** finding the first shape the ray hits */
	public static Surfaces findIntersection(Point p0, Vector direction, List<Surfaces> primitives) {
		double min_t = Double.POSITIVE_INFINITY;
		Surfaces min_primitive = null;
		for (Surfaces shape : primitives) {
			double t = shape.getIntersection(p0, direction);
			if (t == -1)
				continue;
			if (t < min_t) {
				min_t = t;
				min_primitive = shape;
			}

		}

		return min_primitive;
	}
	public static Point getRelevantPoint(int i, int j, Point p0,Camera cam) {
		// TODO Auto-generated method stub
		Point p=new Point(p0.x,p0.y,p0.z);
		p.addByVector(cam.fixedUpVector,i);
		p.addByVector(cam.x_Axis, j);
		return p;
	}

}
