package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

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
	 @Override
	 /**
	  * @param ray
      * @return list of Point3D that intersect the given ray with the sphere
	  */
		public List<Point3D> findIntersections (Ray ray) {
			// TODO Auto-generated method stub
			  Point3D p0 = ray.getP();
		        Vector v = ray.getV();
		        Vector u;
		        try {
		            u = _center.subtract(p0);   // p0 == _center
		        } catch (IllegalArgumentException e) {
		            return List.of(ray.getTargetPoint(_radius));
		        }
		        double tm = alignZero(v.dotProduct(u));
		        double dSquared = (tm == 0) ? u.lengthSquared() : u.lengthSquared() - tm * tm;
		        double thSquared = alignZero(_radius * _radius - dSquared);

		        if (thSquared <= 0) return null;

		        double th = alignZero(Math.sqrt(thSquared));
		        if (th == 0) return null;

		        double t1 = alignZero(tm - th);
		        double t2 = alignZero(tm + th);
		        if (t1 <= 0 && t2 <= 0) return null;
		        if (t1 > 0 && t2 > 0) return List.of(ray.getTargetPoint(t1), ray.getTargetPoint(t2)); //P1 , P2
		        if (t1 > 0)
		            return List.of(ray.getTargetPoint(t1));
		        else
		            return List.of(ray.getTargetPoint(t2));
		    }
		}
	    

	


