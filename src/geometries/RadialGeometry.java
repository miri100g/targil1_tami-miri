package geometries;



public abstract class RadialGeometry implements Geometry
{
	double _radius;
	/** 
     * @return _radius value
     */
	public double getR() {
		return _radius;
	}
	
	public RadialGeometry (double r)//ctr
	{
		_radius=r;
	}
	public RadialGeometry (RadialGeometry  r)//coppy ctr
	{
		_radius=r.getR();
	}

	

}
