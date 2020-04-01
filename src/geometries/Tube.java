package geometries;

import primitives.Point3D;
import static primitives.Util.*;
import primitives.Ray;
import primitives.Vector;

public class Tube extends RadialGeometry
{
    Ray axisRay;
	
	public Tube(double r,Ray ray) //ctr
	{
		super(r);
		// TODO Auto-generated constructor stub
		axisRay=new Ray(ray);
	}
	//get ray axisRay
	public Ray getRay()
	{
		return axisRay;
	}

	public Vector getNormal(Point3D _point) 
	{
	   Point3D o=axisRay.getP();
	   Vector v=axisRay.getV();
	   double t=_point.subtract(o).dotProduct(v);
	   if(!isZero(t))
	   {
		   o=o.add(v.scale(t));
	   }
	   return _point.subtract(o).normalize();
	   
	}
	@Override
	public String toString() 
    {
		 return "ray:"+axisRay;
	} 
	

}
