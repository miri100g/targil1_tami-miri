package geometries;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import primitives.*;
import static primitives.Util.*;

public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected List<Point3D> _vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane _plane;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     * 
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex</li>
     *                                  </ul>
     */ 
     public Polygon(Color emmission,Material material,Point3D... vertices) {
    	super(emmission,material,null);
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        _vertices = List.of(vertices);
        Box b=createBox();
        this.set_box(b);
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        _plane = new Plane( vertices[0], vertices[1], vertices[2]);
        if (vertices.length == 3) return; // no need for more tests for a Triangle

        Vector n = _plane.getNormal( vertices[0]);

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }
/**
 * ctr with default emission value
 * @param vertices list of _vertices values
 */
    public Polygon(Point3D... vertices) {
        this( Color.BLACK, new Material(0,0,0), vertices);
    }
    
  /**
   * ctr  
   * @param emmission _emission value
   * @param vertices  list of _vertices values
   */
    public Polygon(Color emmission,Point3D... vertices) {
    	this(emmission,new Material(0,0,0), vertices);
    }
    
   

    @Override
    public Vector getNormal(Point3D point) {
        return _plane.getNormal(point);
    }

    /**
     * @param vi keeps all the vectors from pi-p0
     * @param si keeps the numbers from crossProduct and dotProduct with v
     * 
     */
	@Override
	public List<GeoPoint> findIntersections (Ray ray) {
		
		if(!IsIntersectionBox(ray))//the ray doesnt intersect the box
    	{
    		return null;
    	}
		List<GeoPoint> intersections = _plane.findIntersections(ray);
        if (intersections == null) return null;

        Point3D p0 = ray.getP();
        Vector v = ray.getV();
        Vector vi[]=new Vector[_vertices.size()];
        List<Double> si=new ArrayList<Double>();
        for(int i=0;i<vi.length;i++)
        {
        	vi[i]=_vertices.get(i).subtract(p0);
        }
        for(int i=0;i<vi.length-1;i++)
        {
        	double s= v.dotProduct(vi[i].crossProduct(vi[i+1]));
        	    if (isZero(s)) return null;
        	    si.add(s);
        }
        double s= v.dotProduct(vi[vi.length-1].crossProduct(vi[0]));
	    if (isZero(s)) return null;
	    si.add(s);
	    
	    List<GeoPoint> result = new LinkedList<>();
        if(si.get(0)>0) //cheack if all numbers are +
        {
        	for(int i=1; i<si.size(); i++)
           {
        	  if(si.get(i)<0)
        		return null;
           }
	            for (GeoPoint geo : intersections) {
	                result.add(new GeoPoint(this, geo.getPoint()));
	            }
	            return result;
	       
        }
        else //if all numbers arr -
        {
        	for(int i=1; i<si.size(); i++)
            {
         	  if(si.get(i)>0)
         		return null;
            }
        	for (GeoPoint geo : intersections) {
                result.add(new GeoPoint(this, geo.getPoint()));
            }
            return result;
        	
        }
        
	}
	  private Box createBox() {
		   	 
	        double x1=Double.NEGATIVE_INFINITY;
			double x0=Double.POSITIVE_INFINITY;
			double y1=Double.NEGATIVE_INFINITY;
			double y0=Double.POSITIVE_INFINITY;
			double z1=Double.NEGATIVE_INFINITY;
			double z0=Double.POSITIVE_INFINITY;
			
	        for(Point3D v: _vertices) {
	        	if(v.get_x().get()<x0) x0=v.get_x().get();
	        	if(v.get_x().get()>x1) x1=v.get_x().get();
	        	if(v.get_y().get()<y0) y0=v.get_y().get();
	        	if(v.get_y().get()>y1) y1=v.get_y().get();
	        	if(v.get_z().get()<z0) z0=v.get_z().get();
	        	if(v.get_z().get()>z1) z1=v.get_z().get();
	        }
	        return new Box(x0,x1,y0,y1,z0,z1);
			
	    }
	@Override
	public boolean IsIntersectionBox(Ray ray) {
		return this._box.IntersectionBox(ray);
	}
	@Override
	public Point3D getPositionPoint() {
		return _vertices.get(0);
	}
}
