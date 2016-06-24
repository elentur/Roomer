package com.projecttango.Visualisation;

import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

import com.projecttango.DataStructure.DestinationPoint;
import com.projecttango.DataStructure.Point;
import com.projecttango.tangoutils.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera;
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
    private final Material materialTransparent = new Material();
    private final Object3D debugObjects = new Object3D();
    private Vector3 arrowPosition = null;
    private Object3D arrow = null;
    private Object3D futureArrow = null;
    private int nextPoint = 0;
    private static Visualize instance;
    private Vector3 futureArrowPosition = null;

    private Visualize(RajawaliRenderer renderer) {
        renderer.getCurrentScene().addChild(debugObjects);
        material2.setDiffuseMethod(new DiffuseMethod.Lambert());
        material2.setSpecularMethod(new SpecularMethod.Phong());
        material2.setColor(Color.GREEN);
        material2.enableLighting(true);
        materialTransparent.setDiffuseMethod(new DiffuseMethod.Lambert());
        materialTransparent.setSpecularMethod(new SpecularMethod.Phong());
        materialTransparent.setColor(Color.argb(50, 0, 255, 0));
        materialTransparent.enableLighting(true);
        material1.setDiffuseMethod(new DiffuseMethod.Lambert());
        material1.setSpecularMethod(new SpecularMethod.Phong());
        material1.setColor(Color.YELLOW);
        material1.enableLighting(true);

        LoaderOBJ objParser = new LoaderOBJ(renderer, R.raw.arrow1);
        try {
            objParser.parse();
            Object3D ob = objParser.getParsedObject();
            ob.setRotation(0, -90, 0);
            ob.setMaterial(material2);
            ob.getGeometry().getBoundingBox();
            // ob.getGeometry().getBoundingBox().transform(ob.getModelMatrix());
            arrow = new Object3D();
            arrow.addChild(ob);
            objParser.parse();
            ob = objParser.getParsedObject();
            ob.setRotation(0, -90, 0);
            ob.setMaterial(materialTransparent);
            ob.setTransparent(true);
            futureArrow = new Object3D();
            futureArrow.addChild(ob);
            futureArrow.setTransparent(true);
          /*  if (arrow !=  null){
                arrow.setMaterial(material2);
                arrow.setScale(0.5);
                futureArrow.setMaterial(m);
                futureArrow.setScale(0.5);
                futureArrow.setColor(0x5500ff00);
            }*/

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
        nextPoint = 1;

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
    public void draw(RajawaliScene scene, Camera camera) {

        //Save the Backscreenquad
        ScreenQuad sq = (ScreenQuad) scene.getChildrenCopy().get(0);
        //Clear all Elements from Scene
        scene.clearChildren();
        //Add the Backscreenquad back again
        scene.addChildAt(sq, 0);
        scene.addChild(debugObjects);
        if (arrow != null) {
            scene.addChild(arrow);
            //if(arrowPosition == null){
            if (futureArrow != null) {
                scene.addChild(futureArrow);
            }
            double d = 0;
            if (nextPoint < points.size() - 2) {
                Vector3 p = points.get(nextPoint).getPosition();
                arrowPosition = new Vector3(p.x, p.y + 0.5, p.z);
                arrow.enableLookAt();
                Vector3 p2 = points.get(nextPoint + 1).getPosition();
                arrow.setLookAt(p2.x, p2.y + 0.5, p2.z);

                Vector3 dir = Vector3.subtractAndCreate(p, camera.getPosition());
                dir.normalize();
                d = camera.getOrientation().getZAxis().dot(dir);
            }
            if (nextPoint < points.size() - 3) {
                Vector3 p = points.get(nextPoint + 1).getPosition();
                futureArrowPosition = new Vector3(p.x, p.y + 0.5, p.z);
                futureArrow.enableLookAt();
                Vector3 p2 = points.get(nextPoint + 2).getPosition();
                futureArrow.setLookAt(p2.x, p2.y + 0.5, p2.z);
            }


            if (d>0.6){
                Log.d("DEBUGGER", " not in Focus");
            }
            //  Log.d("DEBUGGER", nextPoint +"");

            // }
            arrow.setPosition(arrowPosition);
            futureArrow.setPosition(futureArrowPosition);
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


    public void debugDraw(ArrayList<Point> allPoints) {

        Log.d("DEBUGGER", "Debug Draw");
        Log.d("DEBUGGER", "AllPoints: " + allPoints);
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
