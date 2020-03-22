package geometries;

public abstract class RadialGeometry 
{
	double _radius;
	
	public double getR() {
		return _radius;
	}
	
	public RadialGeometry (double r)
	{
		_radius=r;
	}
	public RadialGeometry (RadialGeometry  r)
	{
		_radius=r.getR();
	}

}
