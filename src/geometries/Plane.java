package geometries;


import primitives.Point3D;
import primitives.Vector;

public class Plane implements Geometry
{
	Point3D p1;
	Vector normal;
	/**
	 * Plane  constructor receiving 3 Point3D value
	 * 
	 * @param x  point p1
	 * @params y,z vector normal
	 * 
	 */
	public Plane(Point3D x,Point3D y, Point3D z)
	{
		this.p1=x;//relation point of the plane
        this.normal=null;
	}
	/**
	 * Plane  constructor receiving Point3D value and Vector
	 * @param _p point p1
	 * @param v Vector normal
	 */	
	public Plane(Point3D _p,Vector v)
	{
		p1=new Point3D(_p);
		normal=new Vector(v);
	}
	
    public Point3D getP()//get point p1
    {
    	return p1;
    }
    
    public Vector getV()//get vector normal
    {
    	return normal;
    }
    
    @Override
    public String toString() 
	{
	        return "point:"+p1.toString()+"normal vector:"+normal.toString();
	}

	@Override
	public Vector getNormal(Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

}
