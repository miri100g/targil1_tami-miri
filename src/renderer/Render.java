package renderer;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import elements.AmbientLight;
import elements.Camera;
import elements.LightSource;
import geometries.Geometry;
import geometries.Intersectable;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import static java.lang.System.out;
import static primitives.Util.*;
public class Render {
	
	private ImageWriter image;
	private Scene scene;
	/**
	 * stop condition  maximum level for the recursion
	 */
	private static final int MAX_CALC_COLOR_LEVEL = 10;
	/**
	 *  stop condition  minimum level for the recursion
	 */
	private static final double MIN_CALC_COLOR_K = 0.001;
	
	/**
	 * number of rayes to the beam from point(soft shadow)
	 */
	private static final int NUM_OF_RAYES=80;
	 private double _supersamplingDensity = 1;
	 private double SOFT_SHADOW=1;
	
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
	
	private int _threads = 1;
	private final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
	private boolean _print = false; // printing progress percentage
	/**
	* Set multithreading <br>
	* - if the parameter is 0 - number of coress less SPARE (2) is taken
	* @param threads number of threads
	* @return the Render object itself
	*/
	public Render setMultithreading(int threads) {
	if (threads < 0) throw new IllegalArgumentException("Multithreading must be 0 or higher");
	if (threads != 0) _threads = threads;
	else {
	int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
	_threads = cores <= 2 ? 1 : cores;
	}
	return this;
	}
	/**
	* Set debug printing on
	* @return the Render object itself
	*/
	public Render setDebugPrint() { _print = true; return this; }
	
	/**
	* Pixel is an internal helper class whose objects are associated with a Render object that
	* they are generated in scope of. It is used for multithreading in the Renderer and for follow up
	* its progress.<br/>
	* There is a main follow up object and several secondary objects - one in each thread.
	*/
	private class Pixel {
	private long _maxRows = 0; // Ny
	private long _maxCols = 0; // Nx
	private long _pixels = 0; // Total number of pixels: Nx*Ny
	public volatile int row = 0; // Last processed row
	public volatile int col = -1; // Last processed column
	private long _counter = 0; // Total number of pixels processed
	private int _percents = 0; // Percent of pixels processed
	private long _nextCounter = 0; // Next amount of processed pixels for percent progress
	/**
	* The constructor for initializing the main follow up Pixel object
	* @param maxRows the amount of pixel rows
	* @param maxCols the amount of pixel columns
	*/
	public Pixel(int maxRows, int maxCols) {
	_maxRows = maxRows;_maxCols = maxCols; _pixels = maxRows * maxCols;
	_nextCounter = _pixels / 100;
	if (Render.this._print) System.out.printf("\r %02d%%", _percents);
	}
	/**
	* Default constructor for secondary Pixel objects
	*/
	public Pixel() {}
	
	/**
	* Public function for getting next pixel number into secondary Pixel object.
	* The function prints also progress percentage in the console window.
	* @param target target secondary Pixel object to copy the row/column of the next pixel
	* @return true if the work still in progress, -1 if it's done
	*/
	public boolean nextPixel(Pixel target) {
	int percents = nextP(target);
	if (_print && percents > 0) System.out.printf("\r %02d%%", percents);
	if (percents >= 0) return true;
	if (_print) System.out.printf("\r %02d%%", 100);
	return false;
	}
	
	/**
	* Internal function for thread-safe manipulating of main follow up Pixel object - this function is
	* critical section for all the threads, and main Pixel object data is the shared data of this critical
	* section.<br/>
	* The function provides next pixel number each call.
	* @param target target secondary Pixel object to copy the row/column of the next pixel
	* @return the progress percentage for follow up: if it is 0 - nothing to print, if it is -1 - the task is
	* finished, any other value - the progress percentage (only when it changes)
	*/
	private synchronized int nextP(Pixel target) {
	++col; ++_counter;
	if (col < _maxCols) {
	target.row = this.row; target.col = this.col;
	if (_print && _counter == _nextCounter) {
	++_percents;_nextCounter = _pixels * (_percents + 1) / 100; return _percents;
	}
	return 0;
	}
	++row;
	if (row < _maxRows) {
	col = 0;
	if (_print && _counter == _nextCounter) {
	++_percents; _nextCounter = _pixels * (_percents + 1) / 100; return _percents;
	}
	return 0;
	}
	return -1;
	}
	}
	
	  private Color calcColor(List<Ray> inRay) {
	        Color bkg = scene.getBackground();
	        Color color = Color.BLACK;
	        for (Ray ray : inRay) {
	            GeoPoint gp = findCLosestIntersection(ray);
	            color = color.add(gp == null ? bkg : calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, 1d));
	        }
	        color = color.add(scene.getAmbientLight().getIntesity());
	        int size = inRay.size();
	        return (size == 1) ? color : color.reduce(size);
	    }
	/**
	 * Implements the image on the screen
	 */
	
	public void renderImage()
	{
		
		 Camera camera = scene.getCamera();
	     java.awt.Color background = scene.getBackground().getColor();
	     double distance = scene.getDistance();
	     
	     int Nx = image.getNx();
	     int Ny = image.getNy();
	     double width = image.getWidth();
	     double height = image.getHeight();
	     final Pixel thePixel = new Pixel(Ny, Nx); // Main pixel management object
	     // Generate threads
			Thread[] threads = new Thread[_threads];
			for (int i = _threads - 1; i >= 0; --i) {
				threads[i] = new Thread(() -> {
					Pixel pixel = new Pixel();
					while (thePixel.nextPixel(pixel)) {
						
						Ray ray = camera.constructRayThroughPixel(Nx, Ny, pixel.col, pixel.row, distance, width, height);
		                GeoPoint closestPoint=findCLosestIntersection(ray);
		              
		                if (closestPoint == null) {
		                	
		                    image.writePixel(pixel.col, pixel.row, background);
		                } else {
		                	
		                    image.writePixel(pixel.col, pixel.row, calcColor(closestPoint,ray).getColor());
		                }
						
					}
				});
			}

			// Start threads
			for (Thread thread : threads) thread.start();

			// Wait for all threads to finish
			for (Thread thread : threads) try { thread.join(); } catch (Exception e) {}
			if (_print) System.out.printf("\r100%%\n");
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
	 * calculate the color in a given point by calling help func calcColor with value 0 in level
	 * @param gp point value on the geometry
	 * @param ray ray for calculation transparency and reflection
	 * @return the intensity that defined in ambient lighting (ambient light+ emission light+diffuse light+ specular light+ transperancy+reflection)
	 */
	private Color calcColor(GeoPoint gp, Ray ray)
	{
		Color color = calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, 1.0);
        color = color.add(scene.getAmbientLight().getIntesity());
        return color;
	}
	/**
	 * calculate the color in a given point
	 * @param geopoint point value
	 * @param inRay ray for calculation transparency and reflection
	 * @param level the level in the recursion call to check if we get to the max calls
	  *@param k  the factor we receive from the recursion 
	 * @return the intensity that defined in ambient lighting (ambient light+ emission light+diffuse light+ specular light+ transperancy+reflection)
	 */
	private Color calcColor(GeoPoint geopoint,Ray  inRay, int level, double k)
	{
        if (level == 0 || k < MIN_CALC_COLOR_K) {
            return Color.BLACK;
        }
        Color result = geopoint.getGeometry().getEmmission();
        Point3D pointGeo = geopoint.getPoint();

        Vector v = pointGeo.subtract(scene.getCamera().getP0()).normalize();
        Vector n = geopoint.getGeometry().getNormal(pointGeo);

        Material material = geopoint.getGeometry().getMaterial();
        int nShininess = material.getShininess();
        double kd = material.getKd();
        double ks = material.getKs();
        double kr = geopoint.getGeometry().getMaterial().getKr();
        double kt = geopoint.getGeometry().getMaterial().getKt();
        double kkr = k * kr;
        double kkt = k * kt;

        List<LightSource> lightSources = scene.getLights();
        if (lightSources != null) {
            for (LightSource lightSource : lightSources) {
                Vector l = lightSource.getL(pointGeo);
                double nl = alignZero(n.dotProduct(l));
                double nv = alignZero(n.dotProduct(v));
                if (nl * nv > 0) {
                 //if (sign(nl) == sign(nv) && nl != 0 && nv != 0) {
                    double ktr = transparency(lightSource, l, n, geopoint);
                   
                    if (ktr * k > MIN_CALC_COLOR_K) {
                     //if (unshaded(lightSource, l, n, geopoint)) {
                        Color ip = lightSource.getIntensity(pointGeo).scale(ktr);
                        result = result.add(
                                calcDiffusive(kd, nl, ip),
                                calcSpecular(ks, l, n, nl, v, nShininess, ip));
                     //}
                    }
                  // }
                }
            }
        }
       

        if (level == 1) {
            return Color.BLACK;
        }

        if (kkr > MIN_CALC_COLOR_K) {
            Ray reflectedRay = constructReflectedRay(pointGeo, inRay, n);
            GeoPoint reflectedPoint = findCLosestIntersection(reflectedRay);
            if (reflectedPoint != null) {
                result = result.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
            }
        }
        if (kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = constructRefractedRay(pointGeo, inRay, n);
            GeoPoint refractedPoint = findCLosestIntersection(refractedRay);
            if (refractedPoint != null) {
                result = result.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
            }
        }
        return result;

    }
	
	private Object sign(double dotProduct) {
		return (dotProduct > 0d);
	}
/**

 * @param kd distance factor
 * @param nl cos(teta) between 2 vectors
 * @param ip intensity in a point
 * @return diffuse intensity
 */
	private Color calcDiffusive(double kd, double nl,Color ip) {
		if (nl < 0) nl = -nl;
        return ip.scale(nl * kd);
	}
/**
 * @param ks distance factor
 * @param l  the light source  direction vector(D)
 * @param n normal to the point (N)
 * @param nl cos(teta) between 2 vectors (DN)
 * @param v vector from the sight view(V)
 * @param nShininess hoe much the object shines(n)
 * @param lightIntensity intensity of the light (l0)
 * @return specular intensity
 */
	private Color calcSpecular(double ks,Vector l,Vector n, double nl, Vector v, int nShininess, Color lightIntensity) {
		
		double p = nShininess;
        Vector R = l.add(n.scale(-2 * nl)); // nl must not be zero! R=D-2(DN)N
        double minusVR = -alignZero(R.dotProduct(v));
        if (minusVR <= 0) {
            return Color.BLACK; // view from direction opposite to r vector
        }
        return lightIntensity.scale(ks * Math.pow(minusVR, p));
	}
	
	/**
	 * calculate reflection ray (R=D-2(D*N)N)
	 * @param pointGeo p value in ray ctr
	 * @param inRay source light direction vector
	 * @param n the mormal to the point
	 * @return ray after moving the start point of the ray on the normal to the geometry in the direction of the new ray	
*/
	    private Ray constructReflectedRay(Point3D pointGeo, Ray inRay, Vector n) 
	    {
	        Vector v = inRay.getV();
	        double vn = v.dotProduct(n);
	        if (vn == 0) {
	            return null;
	        }
	        Vector r = v.subtract(n.scale(2 * vn));
	        return new Ray(pointGeo, r, n);
	    }
	
	/**
	 * @param points list of all the points
	 * @return the closest cutting point with p0
	 */
	private GeoPoint getClosestPoint(List<GeoPoint> points)
	{
		Point3D p0= scene.getCamera().getP0();//p0 point
		double dis;
		double min=Double.MAX_VALUE;// the minimal distance
		GeoPoint p=null;
		for(GeoPoint geopoint : points)//for every point in the list we check if the distance from p0 is less then min
		{
			dis=p0.distance(geopoint.getPoint());
			if(dis<min)
			{
				min=dis;
				p=geopoint;
			}
		}
		return p;
	}
	/**
	 * Non-shading test between point and light source
	 * @param light the current light source
	 * @param l vector from light source
	 * @param n normal to raise the point in £ to fix the floating point problem
	 * @param gp point on the geometry which the vector cut
	 * @return true if the point is not hiding and false if it is
	 */
	private boolean unshaded(LightSource light,Vector l, Vector n, GeoPoint gp)
	{
		Vector lightDirection = l.scale(-1); // from point to light source
		Ray lightRay = new Ray(gp.getPoint(), lightDirection, n);//ray between the point and the light sorce
		Point3D point = gp.getPoint();//the point from which the rays send
		List<GeoPoint> intersections = scene.getGeometries().findIntersections(lightRay);
		if (intersections ==null) 
			return true;
		double lightDistance = light.getDistance(point);
		for (GeoPoint geo : intersections)//only points with _Kt=0 can cause shadowes
		{
		  if (alignZero( geo.point.distance(point)-lightDistance) <= 0 && geo.geometry.getMaterial().getKt() == 0)
		   return false;
		}
		return true;
	}
	
	private Ray constructRefractedRay(Point3D pointGeo, Ray inRay, Vector n) 
	{
        return new Ray(pointGeo, inRay.getV(), n);//baemshech
    }

	/**
	 * calculate the intersection
	 * @param ray the ray to find intersection with
	 * @return the closest intersection with the start point of the ray
	 */
	private GeoPoint findCLosestIntersection(Ray ray) 
	{
		if (ray == null) 
		{
            return null;
        }
        GeoPoint closestPoint = null;
        double closestDistance = Double.MAX_VALUE;
        Point3D ray_p0 = ray.getP();

        List<GeoPoint> intersections = scene.getGeometries().findIntersections(ray);
        if (intersections == null)//if there were no Intersection return null
            return null;

        for (GeoPoint geoPoint : intersections) {
            double distance = ray_p0.distance(geoPoint.getPoint());
            if (distance < closestDistance) {
                closestPoint = geoPoint;
                closestDistance = distance;
            }
        }
        return closestPoint;
		
	}
	/**
	 * @param light light source
	 * @param l vector between  light source and a given point
	 * @param n normal to raise the point in £ to fix the floating point problem
	 * @param geopoint point value on the geometry which the vector cuts
	 * @return value of transparency partial shading in case the object/s that block the light source from the point have transparency at some level or another. 
	 * implementaion of soft shadow: creat a beam of ray from every dark point. calculat the ktr of every ray and return the average of the ktr from all rayes in the beam.
	 * the larger the number of the rayes in beam, the more blurry the shadow's edge.
	 * 
	 */
	
	private double transparency(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
		if(SOFT_SHADOW==0)//doesnt use the soft shadow improvment
		{
			Vector lightDirection = l.scale(-1); // from point to light source
	        Ray lightRay = new Ray(geopoint.getPoint(), lightDirection, n);
	        Point3D pointGeo = geopoint.getPoint();

	        List<GeoPoint> intersections = scene.getGeometries().findIntersections(lightRay);
	        if (intersections == null) {
	            return 1d;
	        }
	        double lightDistance = light.getDistance(pointGeo);
	        double ktr = 1d;
	        for (GeoPoint gp : intersections) {
	            if (alignZero(gp.getPoint().distance(pointGeo) - lightDistance) <= 0) {
	                ktr *= gp.getGeometry().getMaterial().getKt();
	                if (ktr < MIN_CALC_COLOR_K) {
	                    return 0.0;
	                }
	            }
	        }
	        return ktr;
		}
		else 
		{
			double sum_ktr = 0;
        List<Ray> rays = constructRayBeamThroughPoint(light, l, n, geopoint);
        for (Ray ray : rays) {
            List<GeoPoint> intersections = scene.getGeometries().findIntersections(ray);
            if (intersections != null) {
                double lightDistance = light.getDistance(geopoint.point);
                double ktr = 1;
                for (GeoPoint geo : intersections) {
                    if (alignZero(geo.point.distance(geopoint.point) - lightDistance) <= 0) //cheack if the geometry is befor the light source or behind
                    {
                        ktr *= geo.geometry.getMaterial().getKt();
                        if (ktr < MIN_CALC_COLOR_K) {
                            ktr = 0;
                            break;
                        }
                    }
                }
                sum_ktr += ktr;
            } else
                sum_ktr += 1;
          }
        return sum_ktr/rays.size();
		}
       
		 
	    
    }
	
	/**
	 * 
	 * @param light the light source 
	 * @param l vector between  light source and a given point
	 * @param n normal to raise the point in £ to fix the floating point problem
	 * @param geopoint point value on the geometry which the vector cuts
	 * @return beam of rayes from point ,in transparency find the intersection points with all of the rayes in the beam and return the ktr for every ray.
	 * in the end transparency returns the average of ktr from all the rayes
	 */

    private List<Ray> constructRayBeamThroughPoint(LightSource light, Vector l, Vector n, GeoPoint geopoint){
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.point, lightDirection, n);
        List<Ray> beam = new ArrayList<>();
        beam.add(lightRay);
        double r = light.getRadius();
        if(r==0)return beam;//in case the light is direction light so it doesn't have radius
        Point3D p0 = lightRay.getP();
        Vector v = lightRay.getV();
		Vector vx = (new Vector(-v.get().get_y().get(), v.get().get_x().get(),0)).normalized(); 
		Vector vy = (v.crossProduct(vx)).normalized();
		
        Point3D pC = lightRay.getTargetPoint(light.getDistance(p0));
        for (int i=0; i<NUM_OF_RAYES-1; i++)//number of rayes less the direct ray to the light(lightRay)
        {
            // create random polar system coordinates of a point in circle of radius r
            double cosTeta = ThreadLocalRandom.current().nextDouble(-1, 1);
            double sinTeta = Math.sqrt(1 - cosTeta*cosTeta);
            double d = ThreadLocalRandom.current().nextDouble(-r, r);
            // Convert polar coordinates to Cartesian ones
            double x = d*cosTeta;
            double y = d*sinTeta;
            // pC - center of the circle
            // p0 - start of central ray, v - its direction, distance - from p0 to pC
            Point3D point = pC;
            if (!isZero(x)) point = point.add(vx.scale(x));
            if (!isZero(y)) point = point.add(vy.scale(y));
            beam.add(new Ray(p0, point.subtract(p0))); // normalized inside Ray ctor
        }
        return beam;
    } 
	 
	
		
	 


	

}

