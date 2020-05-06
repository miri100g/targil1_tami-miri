package renderer;


import java.util.List;

import elements.AmbientLight;
import elements.Camera;
import geometries.Intersectable;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import scene.Scene;

public class Render {
	
	private ImageWriter image;
	private Scene scene;
	/**
	 * ctr
	 * @param i image value
	 * @param s scene value
	 */
	public Render(ImageWriter i,Scene s)
	{
		image=i;
		scene=s;
	}
	/**
	 * Implements the image on the screen
	 */
	public void renderImage()
	{
		 Camera camera = scene.getCamera();
	     Intersectable geometries = scene.getGeometries();
	     java.awt.Color background = scene.getBackground().getColor();
	     AmbientLight ambientLight = scene.getAmbientLight();
	     double distance = scene.getDistance();
	     
	     int Nx = image.getNx();
	     int Ny = image.getNy();
	     double width = image.getWidth();
	     double height = image.getHeight();

	     for (int row = 0; row < Ny; row++) {
	         for (int collumn = 0; collumn < Nx; collumn++)
	         {
	                Ray ray = camera.constructRayThroughPixel(Nx, Ny, collumn, row, distance, width, height);
	                List<Point3D> intersectionPoints = geometries.findIntersections(ray);
	                if (intersectionPoints == null) {
	                    image.writePixel(collumn, row, background);
	                } else {
	                    Point3D closestPoint = getClosestPoint(intersectionPoints);
	                    image.writePixel(collumn, row, calcColor(closestPoint).getColor());
	                }
	         }
	     }
	}
	/**
	 * printing the grid
	 * @param interval interval between one square to an other
	 * @param color the grid color value
	 */
	public void printGrid(int interval, java.awt.Color color)
	{
		 int Nx = image.getNx();
	        int Ny = image.getNy();
	        for (int i = 0; i < Ny; i++) {
	            for (int j = 0; j < Nx; j++) {
	                if (i % interval == 0 || j % interval == 0) {
	                    image.writePixel(j, i, color);
	                }
	            }
	        }
	}
	/**
	 *call to WtiteToImage func in ImageWriter
	 */
	public void writeToImage() 
	{
	     image.writeToImage();
    }
	/**
	 * calculate the color in a given point
	 * @param p point value
	 * @return the intensity that defined in ambient lighting
	 */
	private Color calcColor(Point3D p)
	{
		return scene.getAmbientLight().getIntensity();
	}
	/**
	 * @param points list of all the points
	 * @return the closest cutting point with p0
	 */
	private Point3D getClosestPoint(List<Point3D> points)
	{
		Point3D p0= scene.getCamera().getP0();//p0 point
		double dis;
		double min=Double.MAX_VALUE;// the minimal distance
		Point3D p=null;
		for(Point3D point : points)//for every point in the list we check if the distance from p0 is less then min
		{
			dis=p0.distance(point);
			if(dis<min)
			{
				min=dis;
				p=point;
			}
		}
		return p;
	}
	


}
