package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

public class Tube extends RadialGeometry
{
	Ray axisRay;
	
	public Tube(double r,Ray ray) 
	{
		super(r);
		// TODO Auto-generated constructor stub
		axisRay=new Ray(ray);
	}
	
	public Ray getRay()
	{
		return axisRay;
	}

	public Vector getNormal(Point3D _point) 
	{
	       return null;
	}
	@Override
	public String toString() 
    {
		 return "ray:"+axisRay;
	} 
	

}
