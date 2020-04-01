/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import geometries.Plane;
import primitives.Point3D;
import primitives.Vector;

/**
 * testing getNormal() of plane
 *
 */
public class PlaneTests {

	/**
	 * Test method for {@link geometries.Plane#getNormal(primitives.Point3D)}.
	 */
	@Test
	public void testGetNormal() {
		 // ============ Equivalence Partitions Tests ==============
		 Plane pl = new Plane(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0));
	     double sqrt3 = Math.sqrt(1d / 3);
	     assertEquals("Bad normal to plane", new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point3D(0, 0, 1)));
	}

}
