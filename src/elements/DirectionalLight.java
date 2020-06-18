package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class DirectionalLight extends Light implements LightSource{

	private Vector _direction;
	/**
     * Initialize directional light with it's intensity and direction, direction
     * vector will be normalized.
     *
     * @param colorintensity intensity of the light
     * @param direction      direction vector
     */

    public DirectionalLight(Color colorintensity, Vector direction) {
         super(colorintensity);
        _direction = direction.normalized();
    }

    /**
     * @param p the lighted point is not used and is mentioned
     *          only for compatibility with LightSource
     * @return fixed intensity of the directionLight
     */
    @Override
    public Color getIntensity(Point3D p) {
        return super.getIntesity();
        //       return _intensity;
    }

    //instead of getDirection()
    @Override
    public Vector getL(Point3D p) {
        return _direction;
    }
    /**
     * @return the distance between a point and the light
     * direction light is like the sun, in the infinity
     */
	@Override
	public double getDistance(Point3D point) {
		return Double.POSITIVE_INFINITY;
	}
	
   /**
   * @return 0, direction light doesnt have radius
   */
	@Override
	public double getRadius() {
		return 0;
	}
    
    

}
