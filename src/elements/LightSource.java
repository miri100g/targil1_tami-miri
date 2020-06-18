package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;
/**
 *a class from which all lighting sources are inherited
 */
public interface LightSource {
	/** 
	 * @param p point value
	 * @return power of the specific lighting at that point
	 */
	public Color getIntensity(Point3D p);
	/**
	 * @param p point value
	 * @return the vector between a given point to the light source 
	 */
	public Vector getL(Point3D p);
	
	double getDistance(Point3D point);
	
	/**
	 * 
	 * @return radius of the light source (for soft shadow)
	 */
	double getRadius();


}
