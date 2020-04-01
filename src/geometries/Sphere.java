package geometries;

import primitives.Point3D;
import primitives.Vector;

public class Sphere extends RadialGeometry
{
    Point3D _center;
	
	public Sphere(double r,Point3D _p)//ctr
	{
		super(r);
		// TODO Auto-generated constructor stub
		_center=new Point3D(_p);
    }

	public Vector getNormal(Point3D _point) 
	{
	    Vector normal=_point.subtract(_center);
	    return normal.normalize();
	}
	//get point _center
	public Point3D getC()
	{
		return _center;
	}
	 @Override
	    public String toString() 
		{
		        return "center:"+_center;
		} 
	    

	

}
