package geometries;

import primitives.Point3D;
import primitives.Vector;

public interface Geometry 
{
	/**
	 * func that calculate the normal vector to the object in the given point p
	 * @param p point3D value
	 * @return normal vector 
	 */
	public Vector getNormal(Point3D p);

}
