package com.projecttango.Visualisation;

import android.graphics.Color;
import android.util.Log;

import com.projecttango.DataStructure.DestinationPoint;
import com.projecttango.DataStructure.Point;
import com.projecttango.tangoutils.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;
import org.rajawali3d.primitives.ScreenQuad;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.scene.RajawaliScene;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Marcus Bätz on 26.05.2016.
 * This Class Visualize the navigation path
 */
public class Visualize {

    private static ArrayList<Point> points = new ArrayList<Point>();
    private final static Material material2 = new Material();
    private final static Material material1 = new Material();
    private final static Object3D debugObjects = new Object3D();
    private static Object3D Object;

    static {
        material2.setDiffuseMethod(new DiffuseMethod.Lambert());
        material2.setColor(Color.GREEN);
        material2.enableLighting(true);


        material1.setDiffuseMethod(new DiffuseMethod.Lambert());
        material1.setSpecularMethod(new SpecularMethod.Phong());
        material1.setColor(Color.YELLOW);
        material1.enableLighting(true);

    }

    private static Material mSphereMaterial = new Material();
    static{ mSphereMaterial.enableLighting(true);
        mSphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        mSphereMaterial.setSpecularMethod(new SpecularMethod.Phong());}
    private static Material mSphereMaterialGreen = new Material();
    static{ mSphereMaterial.enableLighting(true);
        mSphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        mSphereMaterial.setSpecularMethod(new SpecularMethod.Phong());
        mSphereMaterialGreen.setColor(Color.GREEN);}

    /**
     * Sets the Points of a calculated NavPath
     *
     * @param points An sorted ArrayList of Points that represents the NavPath. At 0 is start
     *               at point.size()-1 ist destination
     */
    public static void setPoints(ArrayList<Point> points,RajawaliRenderer renderer) {
       /* Visualize.points = points;
        Log.d("DEBUGGER","LoadPoints");
        LoaderOBJ objParser = new LoaderOBJ(renderer, R.raw.arrow);
        Log.d("DEBUGGER", "Zeichne Pfeil");
        try {

            objParser.parse();
            Object = objParser.getParsedObject();
          if(Object !=null)  Object.setMaterial(mSphereMaterialGreen);


        } catch (ParsingException e) {
            e.printStackTrace();
        }*/

    }

    public static ArrayList<Point> getPoints() {
        return points;

    }

    /**
     * This Methode actualize a given scene with a visualization of the Nav path from user
     * position to destination if the NavPath is set
     *
     * @param scene The Rajawali scene where the visualization has to be added
     */
    public static void draw(RajawaliScene scene,RajawaliRenderer renderer ) {

        //Save the Backscreenquad
        ScreenQuad sq = (ScreenQuad) scene.getChildrenCopy().get(0);
        //Clear all Elements from Scene
        scene.clearChildren();
        //Add the Backscreenquad back again
        scene.addChildAt(sq, 0);
        scene.addChild(debugObjects);
       // if(Object != null) scene.addChild(Object);

        //generate a new Point for the actual position of the user
      /*  Vector3 cp = new Vector3(
                scene.getCamera().getPosition().x,
                scene.getCamera().getPosition().y - 1,
                scene.getCamera().getPosition().z);

        //remove waypoints that the camera has crossed
        if (cp.distanceTo(points.get(0).getPosition()) < 0.5) {
            points.remove(0);
        }

        if (!points.isEmpty()) {
            //Generate an approximated curve over all points an the users position
            //First an last have to be doubled
            CatmullRomCurve3D n = new CatmullRomCurve3D();
            n.addPoint(cp);
            n.addPoint(cp);
            for (Point p : points) {
                n.addPoint(p.getPosition());
            }
            n.addPoint(points.get(points.size() - 1).getPosition());

            //equalize the elements for the same distance between each cube
            n.reparametrizeForUniformDistribution(n.getNumPoints() * 4);
            //Generate list ov NavigationPoints
            ArrayList<Vector3> vP = new ArrayList<Vector3>();
            int count = ((int) n.getLength(10)) * 4;
            for (int i = 0; i < count; i++) {
                Vector3 result = new Vector3();
                double t = (i * 1.0) / (count * 1.0);
                n.calculatePoint(result, t);
                vP.add(result);
            }
            // generate for each navigation point a small square except the last,
            // this one gets a big square
            for (int i = 0; i < vP.size(); i++) {
                Cube s;
                if (i == vP.size() - 1) {
                    s = new Cube(0.2f);
                    s.setMaterial(material1);
                } else {
                    s = new Cube(0.05f);
                    s.setMaterial(material2);
                }
                s.setPosition(vP.get(i));
                scene.addChild(s);
            }

        }*/

    }

    public static void clear(RajawaliScene scene) {
        points.clear();
        //Save the Backscreenquad
        ScreenQuad sq = (ScreenQuad) scene.getChildrenCopy().get(0);
        //Clear all Elements from Scene
        scene.clearChildren();
        //Add the Backscreenquad back again
        scene.addChildAt(sq, 0);
        scene.addChild(debugObjects);
    }

    public static void main(String[] args) {
        //Test Method
       //setPoints(new ArrayList<Point>());
        //draw(null);
    }


    public static void debugDraw(ArrayList<Point> allPoints) {

        Log.d("DEBUGGER","Debug Draw");
        for(Point p : allPoints){
            Sphere s = new Sphere(0.1f, 20, 20);
            s.setPosition(p.getPosition());
            if(p instanceof DestinationPoint){
                s.setMaterial(mSphereMaterialGreen);
            }else{
                s.setMaterial(mSphereMaterial);
            }
            debugObjects.addChild(s);
            for(Point n : p.getNeighbours().keySet()){
                Stack<Vector3> stack = new Stack<Vector3>();
                stack.add(p.getPosition());
                stack.add(n.getPosition());
                Line3D line = new Line3D(stack, 50, Color.RED);
                Material m = new Material();
                m.setColor(Color.RED);
                line.setMaterial(m);
                debugObjects.addChild(line);
            }
        }

    }
    public static void debugClear() {

        Log.d("DEBUGGER","Debug clear");
        int x = debugObjects.getNumChildren();
        for(int i = 0; i <x ; i++){
            debugObjects.removeChild(debugObjects.getChildAt(0));
        }

    }

    public static void init(RajawaliScene currentScene) {
        currentScene.addChild(debugObjects);
        Log.d("DEBUGGER","Visualize initialisiert");
    }
}
