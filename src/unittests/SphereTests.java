/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import geometries.Sphere;
import primitives.Point3D;
import primitives.Vector;

/**
 * testing getNormal() of sphere
 *
 */
public class SphereTests {

	/**
	 * Test method for {@link geometries.Sphere#getNormal(primitives.Point3D)}.
	 */
	@Test
	public void testGetNormal() {
		 // ============ Equivalence Partitions Tests ==============
		 Sphere s = new Sphere(1, new Point3D(1, 0, 0));
	     Vector check=new Vector(s.getNormal(new Point3D(2,0,0)));
	     Vector check2=new Vector(1,0,0);
	     assertEquals("Wrong normal to sphere",check,check2);
	}

}
