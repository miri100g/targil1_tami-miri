package geometries;


import primitives.Point3D;
import primitives.Vector;

public class Plane implements Geometry
{
	Point3D p1;
	Vector normal;
	
	public Plane(Point3D x,Point3D y, Point3D z)
	{
		this.p1=x;//relation point of the plane
        this.normal=null;
	}
	
	public Plane(Point3D _p,Vector v)
	{
		p1=new Point3D(_p);
		normal=new Vector(v);
	}
	
    public Point3D getP()
    {
    	return p1;
    }
    
    public Vector getV()
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
