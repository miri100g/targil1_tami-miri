package primitives;


public class Ray
{
	Point3D p;
	Vector dir;
	
	public Ray(Point3D _p, Vector v)throws IllegalArgumentException 
	{
		if(v.normalized()!=v)
			throw new IllegalArgumentException ("the vector is not normalized");
		p=new Point3D(_p);
		dir=new Vector(v);	
	}
	public Ray(Ray r)
	{
		p=new Point3D(r.p);
		dir=new Vector(r.dir);	
	}
	public Point3D getP()
	{
		return p;
	}
	public Vector getV()
	{
		return dir;
	}
	public boolean equals(Object obj) {
	      if (this == obj) return true;
	      if (obj == null) return false;
	      if (!(obj instanceof Ray)) return false;
	      Ray oth = (Ray)obj;
	      return p.equals(oth.p) && dir.equals(oth.dir);
	   }
	@Override
	public String toString() 
	{
	        return "point:"+p.toString()+"diraction vector:"+dir.toString();
	}
	
	


}
