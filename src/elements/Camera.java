package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
public class Camera 
{
	//Location of viewpoint
	private Point3D p0;
	//3 vectors for Camera orientation
	private Vector vup;
	private Vector vto;
	private Vector vright;
	 /**
     * Point3D value getter
     * @return p0 value
     */
	public Point3D getP0()
	{
		return new Point3D(p0);
	}
	/**
	 * Vector value getter
	 * @return vup value
	 */
	public Vector getVup() {
		return new Vector(vup);
	}
	/**
	 * Vector value getter
	 * @return vto value
	 */
	public Vector getVto() {
		return new Vector(vto);
	}
	/**
	 * Vector value getter
	 * @return vright value
	 */
	public Vector getVright() {
		return new Vector(vright);
	}
	 /**
     * Camera constructor receiving a Point3D value and 2 vectors
     * @param p p0 value
     * @param vt vto value
     * @param vu vup value
     */
	public Camera(Point3D p, Vector vt,Vector vu)
	{
		p0=new Point3D(p);
		if(vt.dotProduct(vu)!=0)
			throw new IllegalArgumentException("the vectors are not orthogonals");
		vright=(vt.crossProduct(vu)).normalized();
		vup=vu.normalized();
		vto=vt.normalized();
	}
	/**
	 * Constructing a ray through a pixel
	 * @param nX number of pixels on x
	 * @param nY number of pixels on y
	 * @param j point of where the pixel start on y
	 * @param i point of where the pixel start on x
	 * @param screenDistance distance from the projection center to the view plane
	 * @param screenWidth width of the view plane
	 * @param screenHeight hight of the view plane
	 * @return a ray that go through a certain pixel
	 */
	public Ray constructRayThroughPixel (int nX, int nY,int j, int i, double screenDistance,double screenWidth, double screenHeight)
	{
		if (isZero(screenDistance))
        {
            throw new IllegalArgumentException("distance cannot be 0");
        }
		//Pc is the point where the vector is sent from the projection center p0 in the direction of Vto is meeting the view plane
        Point3D Pc = p0.add(vto.scale(screenDistance));//pc=p0+d*Vto

        double Ry = screenHeight/nY;//the hight of every single pixel
        double Rx = screenWidth/nX;//the width of every single pixel

        double yi =  ((i - nY/2d)*Ry + Ry/2d);
        double xj=   ((j - nX/2d)*Rx + Rx/2d);

        Point3D Pij = Pc;

        if (! isZero(xj))
        {
            Pij = Pij.add(vright.scale(xj));
        }
        if (! isZero(yi))
        {
            Pij = Pij.subtract(vup.scale(yi)); // Pij.add(_vUp.scale(-yi))
        }

        Vector Vij = Pij.subtract(p0);

        return new Ray(p0,Vij);
	}
	
	 public List<Ray> constructRayBeamThroughPixel(int nX, int nY, int j, int i, double screenDistance, double screenWidth, double screenHeight, double density, int amount) 
	 {
		 Random rnd=new Random();
if (isZero(screenDistance)) {
throw new IllegalArgumentException("distance cannot be 0");
}

List<Ray> rays = new LinkedList<>();

Point3D Pc = p0.add(vto.scale(screenDistance));

double Ry = screenHeight / nY;
double Rx = screenWidth / nX;

double yi = ((i - nY / 2d) * Ry + Ry / 2d);
double xj = ((j - nX / 2d) * Rx + Rx / 2d);

Point3D Pij = Pc;

if (!isZero(xj)) {
Pij = Pij.add(vright.scale(xj));
}
if (!isZero(yi)) {
Pij = Pij.subtract(vup.scale(yi)); // Pij.add(_vUp.scale(-yi))
}

//antialiasing density >= 1
double radius = (Rx + Ry) / 2d * density;


for (int counter = 0; counter < amount; counter++) {
Point3D point = new Point3D(Pij);
double cosTheta = 2 * rnd.nextDouble() - 1;
double sinTheta = Math.sqrt(1d - cosTheta * cosTheta);

double d = radius * (2 * rnd.nextDouble() - 1);
double x = d * cosTheta;
double y = d * sinTheta;

if (!isZero(x)) {
point = point.add(vright.scale(x));
}
if (!isZero(y)) {
point = point.add(vup.scale(y));
}
rays.add(new Ray(p0, point.subtract(p0)));
}
return rays;
}
	
	
	
	
	
	

}
     

