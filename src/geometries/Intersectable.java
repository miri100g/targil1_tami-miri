package geometries;

import primitives.Ray;

import java.util.List;

import primitives.Point3D;


public interface Intersectable 
{
	/**
	 * @param ray
	 * @return a point3D list of itersections with a given ray
	 */
	public List<Point3D> findIntersections (Ray ray);

}
