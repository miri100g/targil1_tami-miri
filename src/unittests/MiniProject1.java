package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import elements.AmbientLight;
import elements.Camera;
import elements.DirectionalLight;
import elements.PointLight;
import elements.SpotLight;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;
import renderer.ImageWriter;
import renderer.Render;
import scene.Scene;

public class MiniProject1 {

	@Test
	public void MiniProject1test() {
		Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));
      
        
     scene.addGeometries(
                new Sphere(new Color(255,0,0), new Material(0.001, 1.5, 100, 0.5, 0.7), 40, new Point3D(0, -20, 0)),
                new Plane(new Color(java.awt.Color.BLACK), new Material(0.5, 0.5, 0), new Point3D(0, 20, 0), new Point3D(4, 20, 0),new Point3D(4,20, 2)));
      
      
          
        
          
          scene.addLights(new SpotLight(new Color(700, 400, 400), //
  				new Vector(0, 0, -1), new Point3D(0,-150,500), 1, 4E-5, 2E-7,10));
         /* scene.addLights(new SpotLight(new Color(700, 400, 400), //
    				new Vector(0, 0, -1), new Point3D(0,0,0), 1, 4E-5, 2E-7,10));*/
           
        
        ImageWriter imageWriter = new ImageWriter("miniP1", 200, 200, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
	}

}
