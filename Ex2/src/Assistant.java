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

}
