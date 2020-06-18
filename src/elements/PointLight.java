package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class PointLight extends Light implements LightSource {
	
	protected Point3D _position;
	double _kC;// Constant attenuation
    double _kL; // Linear attenuation
    double _kQ; // Quadratic attenuation
    double _r;
/**
 * ctr that calls Light ctr with color value
 * @param color _intensity value
 * @param p _position value
 * @param kc _kC value 
 * @param kl _kL value
 * @param kq _kQ value
 *
 */
	public PointLight(Color color,Point3D p,double kc,double kl,double kq) {
		super(color);
		_position=new Point3D(p);
		_kC=kc;
		_kL=kl;
		_kQ=kq;
		
	}
	
	/**
	 * ctr that calls Light ctr with color value
	 * @param color _intensity value
	 * @param p _position value
	 * @param kc _kC value 
	 * @param kl _kL value
	 * @param kq _kQ value
	 * @param r light source radius
	 */
	public PointLight(Color color,Point3D p,double kc,double kl,double kq,double r) {
		
		this(color,p,kc,kl,kq);
		_r=r;
		
	}
	
	

	@Override
	public Color getIntensity(Point3D p) {
		double dsquared = p.distanceSquared(_position);
        double d = p.distance(_position);

        return (_intensity.reduce(_kC + _kL * d + _kQ * dsquared));
	}

	@Override
	public Vector getL(Point3D p) {
		if (p.equals(_position)) {
            return null;
        }
        return p.subtract(_position).normalize();
	}

	@Override
	public double getDistance(Point3D point) {
		return point.distance(_position);

	}

	@Override
	public double getRadius() {
		return _r;
	}

}
