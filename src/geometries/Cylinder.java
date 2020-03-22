package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube
{
	double _height;

	public Cylinder(double r, Ray ray,double height) {
		super(r, ray);
		// TODO Auto-generated constructor stub
		_height=height;
	}
	public Vector getNormal(Point3D _point) 
	{
	        return null;
	}
	
	public double getH()
	{
		return _height;
	}
	@Override
	public String toString() 
	{
		 return "height:"+_height;
	} 
	    

	
	

}
