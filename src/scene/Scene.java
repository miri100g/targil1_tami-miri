package scene;



import elements.AmbientLight;
import elements.Camera;
import geometries.Geometries;
import geometries.Intersectable;
import primitives.Color;

public class Scene 
{
	private String _name;
	private Color _background;
	private AmbientLight _ambientLight;
	private Geometries _geometries;
	private Camera _camera;
	private double _distance;
	
	/**
	 * ctr that create an empty collection of elements in 3D model. 
	 * @param name scene name
	 */
	public Scene(String name)
	{
		_name=name;
		_geometries=new Geometries();
	}
	/** 
	 * @return scene name
	 */
	public String getName() {
		 return _name;
	}
	/**
	 * @return background color
	 */
	public Color getBackground()
	{
		return _background;
	}
	/**
	 * @return ambient light
	 */
	public AmbientLight getAmbientLight()
	{
		return _ambientLight;
	}
	/**
	 * @return camera
	 */
	public Camera getCamera()
	{
		return _camera;
	}
	/**
	 * @return distance from the camera to the screen
	 */
	public double getDistance()
	{
		return _distance;
	}
	/**
	 * @return collection of elements in 3D model.
	 */
	public Geometries getGeometries()
	{
		return _geometries;
	}
	/**
	 * @param c camera value
	 */
	public void setCamera(Camera c)
	{
		_camera=c;
	}
	/**
	 * @param d distance value
	 */
	public void setDistance(double d)
	{
		_distance=d;
	}
	/**
	 * @param b background color value
	 */
	public void setBackground(Color b)
	{
		_background=b;
	}
	/**
	 * @param a ambient light value
	 */
	public void setAmbientLight(AmbientLight a)
	{
		_ambientLight=a;
	}
	/**
	 * func that add geometries to the 3D model
	 * @param geometries geometries to add the collection 
	 */
	public void addGeometries(Intersectable...  geometries) 
	{
		for(Intersectable geo :geometries)
		{
			_geometries.add(geo);
		}
	}
	

    


}
