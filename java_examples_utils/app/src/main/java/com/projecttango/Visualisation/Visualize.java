package com.projecttango.Visualisation;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;
import android.widget.Toast;

import com.projecttango.DataStructure.DestinationPoint;
import com.projecttango.DataStructure.Point;
import com.projecttango.rajawali.Pose;
import com.projecttango.tangoutils.R;

import org.rajawali3d.BufferInfo;
import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;
import org.rajawali3d.primitives.NPrism;
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
    private final Material materialCompassArrow = new Material();
    private final Material materialTransparent = new Material();
    private final Material materialDestination = new Material();
    private final Object3D debugObjects = new Object3D();
    private Vector3 arrowPosition = null;
    private Object3D arrow = null;
    private Object3D futureArrow = null;
    private Object3D compassArrow = null;
    private Object3D destination = null;
    private int nextPoint = 0;
    private static Visualize instance;
    private Vector3 futureArrowPosition = null;
    private Vector3 destinationPosition = null;

    private Visualize(RajawaliRenderer renderer) {
        material2.setColor(Color.rgb(52, 152, 219));
        material2.enableLighting(true);
        material2.setDiffuseMethod(new DiffuseMethod.Lambert());
        material2.setSpecularMethod(new SpecularMethod.Phong(Color.WHITE, 60));


        materialTransparent.setColor(Color.argb(50, 52, 152, 219));
        materialTransparent.enableLighting(true);
        materialTransparent.setDiffuseMethod(new DiffuseMethod.Lambert());
        materialTransparent.setSpecularMethod(new SpecularMethod.Phong(Color.WHITE, 60));


        material1.setColor(Color.YELLOW);
        material1.enableLighting(true);
        material1.setDiffuseMethod(new DiffuseMethod.Lambert());
        material1.setSpecularMethod(new SpecularMethod.Phong(Color.WHITE, 60));

        materialDestination.setColor(Color.rgb(231,76,60));
        materialDestination.enableLighting(true);
        materialDestination.setDiffuseMethod(new DiffuseMethod.Lambert());
        materialDestination.setSpecularMethod(new SpecularMethod.Phong(Color.WHITE, 60));

        LoaderOBJ objParser = new LoaderOBJ(renderer, R.raw.arrow2);
        try {
            objParser.parse();
            Object3D ob = objParser.getParsedObject();
            ob.setRotation(0, -90, 0);
            ob.setMaterial(material2);
            ob.getGeometry().getBoundingBox();
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
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        objParser = new LoaderOBJ(renderer, R.raw.arrow1);
        try {
            objParser.parse();
            Object3D ob = objParser.getParsedObject();
            ob.setRotation(0, -90, 0);
            ob.setScale(0.2);
            ob.setName("arrow");
            ob.setDepthTestEnabled(false);
            compassArrow = new Object3D();
            ob.setMaterial(materialCompassArrow);
            ob.getGeometry().getBoundingBox();
            compassArrow.addChild(ob);

        } catch (ParsingException e) {
            e.printStackTrace();
        }
        objParser = new LoaderOBJ(renderer, R.raw.dest);
        try {
            objParser.parse();
            Object3D ob = objParser.getParsedObject();
            ob.setRotation(0, 0, 180);
            ob.setScaleY(1.5);
            ob.setMaterial(materialDestination);
            ob.getGeometry().getBoundingBox();
            destination = new Object3D();
            destination.addChild(ob);
            ob = new NPrism(40,0.3,0.05);
            ob.setMaterial(materialDestination);
            destination.addChild(ob);
            ob.moveUp(-1);
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
    public boolean draw(RajawaliScene scene, Camera camera, Context context) {

        //Save the Backscreenquad
        ScreenQuad sq = (ScreenQuad) scene.getChildrenCopy().get(0);
        //Clear all Elements from Scene
        scene.clearChildren();
        //Add the Backscreenquad back again
        scene.addChildAt(sq, 0);
        if(debugObjects.getNumChildren()>0)scene.addChild(debugObjects);

        if(!points.isEmpty()) {
            if (arrow != null && futureArrow != null) {
                double dist = Double.MAX_VALUE;
                for (int i = 0; i < points.size(); i++) {
                    double distTemp = points.get(i).getPosition().distanceTo(camera.getPosition());

                    if (distTemp > dist) {
                        if (i > 0) {
                            Vector3 p1 = points.get(i - 1).getPosition();
                            Vector3 p2 = points.get(i).getPosition();
                            if (p1.distanceTo(p2) < camera.getPosition().distanceTo(p2)) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    dist = distTemp;
                    nextPoint = i;
                }
                if (dist < 2.0) {
                    nextPoint++;
                    if (nextPoint > points.size() - 1) {
                        points.clear();
                        return true;
                    }
                }
                if (nextPoint < points.size() - 1) {
                    scene.addChild(arrow);
                    Vector3 p = points.get(nextPoint).getPosition();
                    arrowPosition = new Vector3(p.x, p.y + 0.5, p.z);
                    arrow.enableLookAt();
                    Vector3 p2 = points.get(nextPoint + 1).getPosition();
                    arrow.setLookAt(p2.x, p2.y + 0.5, p2.z);

                }
                if (nextPoint < points.size() - 2) {
                    scene.addChild(futureArrow);
                    Vector3 p = points.get(nextPoint + 1).getPosition();
                    futureArrowPosition = new Vector3(p.x, p.y + 0.5, p.z);
                    futureArrow.enableLookAt();
                    Vector3 p2 = points.get(nextPoint + 2).getPosition();
                    futureArrow.setLookAt(p2.x, p2.y + 0.5, p2.z);
                } else if (destination != null) {
                    scene.addChild(destination);
                    Vector3 p = points.get(points.size() - 1).getPosition();
                    destinationPosition = new Vector3(p.x, p.y + 0.5, p.z);
                }
                if (arrowPosition != null) {
                    arrow.setPosition(arrowPosition);
                }
                Material m = new Material();
                m.setColor(Color.RED);
                m.enableLighting(true);
                m.setDiffuseMethod(new DiffuseMethod.Lambert());
                m.setSpecularMethod(new SpecularMethod.Phong(Color.WHITE, 60));
                compassArrow.getChildAt(0).setMaterial(m);
                scene.addChild(compassArrow);
                if (futureArrowPosition != null) futureArrow.setPosition(futureArrowPosition);
                if (destinationPosition != null) destination.setPosition(destinationPosition);
            }
        }

        return false;
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

    public void setCompassArrow(Pose pose) {
        if(!points.isEmpty() && nextPoint< points.size()) {
            compassArrow.setPosition(pose.getPosition());
            compassArrow.setOrientation(pose.getOrientation());
            compassArrow.moveForward(-1);
            compassArrow.moveUp(-.2);
            Object3D ob = compassArrow.getChildByName("arrow");
            Vector3 p = points.get(nextPoint).getPosition();
            Vector3 position = new Vector3(p.x, p.y + 0.5, p.z);
            if (ob != null && position != null) {
                compassArrow.enableLookAt();
                compassArrow.setLookAt(position);
            }
        }
    }
}
