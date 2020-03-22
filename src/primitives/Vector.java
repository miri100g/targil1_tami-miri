package primitives;

public class Vector 
{
	Point3D p;
   
	public Vector(Coordinate a,Coordinate b,Coordinate c)throws IllegalArgumentException 
	{
		if(Point3D.ZERO.equals(new Point3D(a,b,c)))
			throw new IllegalArgumentException("the zero vector");
		p=new Point3D(a,b,c);
		
	}
	public Vector(double a,double b,double c)throws IllegalArgumentException 
	{
		if(Point3D.ZERO.equals(new Point3D(a,b,c)))
			throw new IllegalArgumentException("the zero vector");
		p=new Point3D(a,b,c);
	}
	public Vector(Point3D _p)throws IllegalArgumentException 
	{
		if(_p.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector");
		p= new Point3D(_p);
	}
	public Vector(Vector v)
	{
		p=new Point3D(v.p);
	}
	
    public Point3D get() {
        return p;
    }
    
    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (obj == null) return false;
       if (!(obj instanceof Vector)) return false;
       Vector oth = (Vector)obj;
       return p.equals(oth.p);
    }
    @Override
    public String toString() {
        return "point:"+p.toString();
    }
    

	public Vector subtract(Vector v)
	{
		Vector nv=new Vector(this.p.subtract(v.p));
		return nv;
	}
	public Vector add(Vector v)
	{
		Vector nv=new Vector(this.p.add(v));
		return nv;
	}
	
	public Vector scale(double tmp)
	{
		return new Vector(p.x.get()*tmp,p.y.get()*tmp,p.z.get()*tmp);
	}
	 public double dotProduct(Vector vector) 
	 {
	        return p.get_x().get() * vector.p.get_x().get() +p.get_y().get() * vector.p.get_y().get() + p.get_z().get() * vector.p.get_z().get();
	 }
	 public Vector crossProduct(Vector v)
	 {
		return new Vector(this.p.get_y().get()*v.p.get_z().get()-this.p.get_z().get()*v.p.get_y().get(),
				this.p.get_z().get()*v.p.get_x().get()-this.p.get_x().get()*v.p.get_z().get(),
				this.p.get_x().get()*v.p.get_y().get()-this.p.get_y().get()*v.p.get_x().get());
		 
	 }
	 public double lengthSquared()
	 {
		 return p.distanceSquared(Point3D.ZERO);
	 }
	 public double length()
	 {
		return Math.sqrt(this.lengthSquared()); 
	 }
	 public Vector normalize()
	 {
		 this.p = scale(1/length()).p;
		 return this;
	 }
	 public Vector normalized()
	 {
		 return (new Vector(this).normalize());
	 }
	
}
