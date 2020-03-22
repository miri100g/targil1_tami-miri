package geometries;

import primitives.Point3D;
import primitives.Vector;

public class Triangle extends Polygon
{
	Point3D x,y,z;
	public Triangle(Point3D a,Point3D b,Point3D c)
	{
		super(a,b,c);
	}
	 public Point3D get_x(){
	        return x;
	    }
	    public Point3D get_y() {
	        return y;
	    }
	    public Point3D get_z() {
	        return z;
	    }
	    
	    @Override
	    public String toString() 
		{
		        return "point1:"+x.toString()+"point2:"+y.toString()+"point3:"+z.toString();
		} 
	    
	    @Override
	    public Vector getNormal(Point3D _point) {
	        return null;
	    }
	

}
