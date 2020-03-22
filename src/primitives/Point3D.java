package primitives;
import primitives.Coordinate;
import primitives.Vector;

public class Point3D 
{
	Coordinate x,y,z;
	public static final Point3D ZERO=new Point3D(0.0,0.0,0.0);
	public Point3D(Coordinate a,Coordinate b,Coordinate c)
	{
		x=a;
		y=b;
		z=c;
	};
    public Point3D(double a,double b,double c)
    {
    	Coordinate aa=new Coordinate(a);
    	Coordinate bb=new Coordinate(b);
    	Coordinate cc=new Coordinate(c);
    	x=aa;
    	y=bb;
    	z=cc;
    	
    };
    public Point3D(Point3D point)
    {
    	x=point.x;
    	y=point.y;
    	z=point.z;
    };
    public Coordinate get_x(){
        return x;
    }
    public Coordinate get_y() {
        return y;
    }
    public Coordinate get_z() {
        return z;
    }
    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (obj == null) return false;
       if (!(obj instanceof Point3D)) return false;
       Point3D oth = (Point3D)obj;
       return x.equals(oth.x) && y.equals(oth.y) && z.equals(oth.z);
    }
    @Override
    public String toString() {
        return "x="+x.toString()+"y="+y.toString()+"z="+z.toString();
    }
    
    public Vector subtract(Point3D p)
    {
    	return new Vector(this.get_x().get() - p.get_x().get(), this.get_y().get() - p.get_y().get(), this.get_z().get() - p.get_z().get());
    }
    public Point3D add(Vector v)
    {
    	  return new Point3D(this.get_x().get() + v.p.get_x().get(), this.get_y().get() + v.p.get_y().get(), this.get_z().get() + v.p.get_z().get());
    	
    }
    public double distanceSquared(Point3D _p)
    {
    	return ((this.get_x().get()-_p.get_x().get())*(this.get_x().get()-_p.get_x().get()))+
    			((this.get_y().get()-_p.get_y().get())*(this.get_y().get()-_p.get_y().get()))+
    			((this.get_z().get()-_p.get_z().get())*(this.get_z().get()-_p.get_z().get()));
    }
    public double distance(Point3D _p)
    {
       return Math.sqrt(this.distanceSquared(_p));
    }
}
