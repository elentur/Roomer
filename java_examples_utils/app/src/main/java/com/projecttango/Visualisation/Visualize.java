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

    private ArrayList<Point> points = new ArrayList<Point>();
    private final Material material2 = new Material();
    private final Material material1 = new Material();
    private final Object3D debugObjects = new Object3D();
    private Object3D arrow = null;
    private static Visualize instance;

    private Visualize(RajawaliRenderer renderer) {
        renderer.getCurrentScene().addChild(debugObjects);
        material2.setDiffuseMethod(new DiffuseMethod.Lambert());
        material2.setSpecularMethod(new SpecularMethod.Phong());
        material2.setColor(Color.GREEN);
        material2.enableLighting(true);
        material1.setDiffuseMethod(new DiffuseMethod.Lambert());
        material1.setSpecularMethod(new SpecularMethod.Phong());
        material1.setColor(Color.YELLOW);
        material1.enableLighting(true);

        LoaderOBJ objParser = new LoaderOBJ(renderer, R.raw.arrow1);
        try {
            objParser.parse();
            arrow = objParser.getParsedObject();
            if (arrow !=  null)arrow.setMaterial(material2);

        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets the Points of a calculated NavPath
     *
     * @param points An sorted ArrayList of Points that represents the NavPath. At 0 is start
     *               at point.size()-1 ist destination
     */
    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Point> getPoints() {
        return this.points;

    }

    /**
     * This Methode actualize a given scene with a visualization of the Nav path from user
     * position to destination if the NavPath is set
     *
     * @param scene The Rajawali scene where the visualization has to be added
     */
    public void draw(RajawaliScene scene) {

        //Save the Backscreenquad
        ScreenQuad sq = (ScreenQuad) scene.getChildrenCopy().get(0);
        //Clear all Elements from Scene
        scene.clearChildren();
        //Add the Backscreenquad back again
        scene.addChildAt(sq, 0);
        scene.addChild(debugObjects);
        if (arrow != null) {
            scene.addChild(arrow);
        }




    }

    public void clear(RajawaliScene scene) {
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


    public void debugDraw(ArrayList<Point> allPoints) {

        Log.d("DEBUGGER", "Debug Draw");
        for (Point p : allPoints) {
            Sphere s = new Sphere(0.1f, 20, 20);
            s.setPosition(p.getPosition());
            if (p instanceof DestinationPoint) {
                s.setMaterial(material2);
            } else {
                s.setMaterial(material1);
            }
            debugObjects.addChild(s);
            for (Point n : p.getNeighbours().keySet()) {
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

    public void debugClear() {

        Log.d("DEBUGGER", "Debug clear");
        int x = debugObjects.getNumChildren();
        for (int i = 0; i < x; i++) {
            debugObjects.removeChild(debugObjects.getChildAt(0));
        }

    }


    public static Visualize getInstance(RajawaliRenderer renderer) {
        if (renderer == null) return null;
        if (instance == null) instance = new Visualize(renderer);
        return instance;
    }
}
