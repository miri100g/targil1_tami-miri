package geometries;

import primitives.Point3D;
import primitives.Vector;

public interface Geometry 
{
	/**
	 * @param p
	 * @return normal vector to the object in the given point p
	 */
	public Vector getNormal(Point3D p);

}
