package unittests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import elements.AmbientLight;
import elements.Camera;
import elements.DirectionalLight;
import elements.PointLight;
import elements.SpotLight;
import geometries.Geometries;
import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import k_means.Cluster;
import k_means.Kmeans;
import k_means.Point;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;
import renderer.ImageWriter;
import renderer.Render;
import scene.Scene;

public class ourImage {

	@Test
	public void ourtest() {
		Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));
      
        
      scene.addGeometries(
                new Sphere(new Color(255,0,0), new Material(0.001, 1.5, 100, 0, 0.7), 20, new Point3D(0, 0, 0)),
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.001, 1.5, 100, 0, 0.7), 20, new Point3D(60, 0, 400)),
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.001, 1.5, 100, 0.2, 0.7), 10, new Point3D(-40, 10, 0)),
                new Sphere(new Color(56,115,8), new Material(0.001, 1.5, 100, 0, 0.7), 20, new Point3D(30,0,200)),
                new Plane(new Color(java.awt.Color.BLACK), new Material(0.5, 0.5, 0, 0.5, 0.7), new Point3D(0, 20, 0), new Point3D(4, 20, 0),new Point3D(4,20, 2)),
                new Sphere(new Color(56,115,8), new Material(0.001, 1.5, 100, 0, 1), 25, new Point3D(-70,-5,80)),
                new Sphere(new Color(java.awt.Color.RED), new Material(0.001, 1.5, 100, 0.2, 0.7), 5, new Point3D(80, 15, 0)),
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.001, 1.5, 100, 0.2, 0.7), 5, new Point3D(90, 15, 0)),
                new Sphere(new Color(56,115,8), new Material(0.001, 1.5, 100, 0.2, 0.7), 5, new Point3D(70, 15, 0)),
              new Sphere(new Color(java.awt.Color.RED), new Material(0.001, 1.5, 100, 0.2, 0.7), 5, new Point3D(-60, 15, 0)));
      
      scene.addLights(new SpotLight(new Color(700, 400, 400),new Vector(-1, 1, 4),new Point3D(40, -40, -115),  1, 4E-4, 2E-5,10));
      
           scene.addLights(
        		   new PointLight(new Color(700, 400, 400), new Point3D(0, -10,0),1, 0.001, 0.0005,10));
        
         scene.addLights(new SpotLight(new Color(700, 400, 400), //
   				new Vector(0, 0, 1), new Point3D(60, -50, 0), 1, 4E-5, 2E-7,10));
           
           
                    
        ImageWriter imageWriter = new ImageWriter("newImage", 200, 200, 500, 500);
        Render render = new Render(imageWriter, scene).setMultithreading(3).setDebugPrint();//60's without multithreading and 45's with.

        render.renderImage();
        render.writeToImage();
	}
	

}
