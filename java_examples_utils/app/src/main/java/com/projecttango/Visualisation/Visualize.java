package com.projecttango.Visualisation;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;
import android.widget.Toast;

import com.google.atap.tangoservice.experimental.TangoPositionData;
import com.projecttango.DataStructure.ADF;
import com.projecttango.DataStructure.Point;
import com.projecttango.DataStructure.PointProperties;
import com.projecttango.DataStructure.PosCalculator;
import com.projecttango.DataStructure.RoomerDB;
import com.projecttango.rajawali.Pose;
import com.projecttango.tangoutils.R;
import com.projecttango.tangoutils.TangoPoseUtilities;

import org.rajawali3d.BufferInfo;
import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.loader.fbx.LoaderFBX;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
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

    public ArrayList<Point> points = new ArrayList<Point>();
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
    public int nextPoint = 0;
    private static Visualize instance;
    private Vector3 futureArrowPosition = null;
    private Vector3 destinationPosition = null;
    public boolean changeADF;
    public ADF adf;
    public ArrayList<Point> allPoints = new ArrayList<Point>();
    public boolean isDebug;
    public int saveNextPoint= 0;
    public boolean needsNextPoint= false;

    /**
     * Build an ne Visualisation class and loads an build all 3d Objects
     * @param renderer
     */
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

        materialDestination.setColor(Color.rgb(231, 76, 60));
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
            ob.setDepthMaskEnabled(false);
            compassArrow = new Object3D();
            // ob.setMaterial(materialCompassArrow);
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
            ob = new NPrism(40, 0.3, 0.05);
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
        Log.d("DEBUGGER", "Path: " + points);
        nextPoint = 0;
        if(!points.isEmpty()) adf = points.get(0).getAdf();
        for(Point p : points){
            if(p.getAdf()!=null){
                Log.d("DEBUGGER", p.getAdf().getName());
            }
        }
    }

    /**
     * Distanz from User Position to destination in m
     * @param camera user position camera
     * @return distanz in m
     */
    public int getDistance(Camera camera) {
        int distance = 0;

        ArrayList<Point> points = this.points;
        for (int i = nextPoint; i < points.size()-1; i++) {
            distance += Vector3.distanceTo(
                    points.get(i).getPosition(),
                    points.get(i+1).getPosition());
        }
        Vector3 cp = new Vector3(
                camera.getPosition().x,
                camera.getPosition().y - 1,
                camera.getPosition().z);
        if (!points.isEmpty()) distance += Vector3.distanceTo(
                points.get(nextPoint).getPosition(),
                cp);
        return distance;
    }

    /**
     * This Methode actualize a given scene with a visualization of the Nav path from user
     * position to destination if the NavPath is set
     *
     * @param scene The Rajawali scene where the visualization has to be added
     */
    public boolean draw(RajawaliScene scene, Camera camera) {
        if (isDebug && !scene.getChildrenCopy().contains(debugObjects)) {
            setAllPoints();
            scene.addChild(debugObjects);
            scene.removeChild(compassArrow);
        } else if (!isDebug) {
            scene.removeChild(debugObjects);
        }
        if (!points.isEmpty()) {
            if (arrow != null && futureArrow != null) {
                if (!scene.getChildrenCopy().contains(destination)) scene.addChild(destination);
                if (!scene.getChildrenCopy().contains(arrow)) scene.addChild(arrow);
                if (!scene.getChildrenCopy().contains(futureArrow)) scene.addChild(futureArrow);
                if (!scene.getChildrenCopy().contains(compassArrow)) scene.addChild(compassArrow);
                if(!compassArrow.isVisible()) compassArrow.setVisible(true);
                double dist = Double.MAX_VALUE;
                for (int i = nextPoint; i < points.size(); i++) {
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
                        scene.removeChild(arrow);
                        scene.removeChild(futureArrow);
                        scene.removeChild(compassArrow);
                        scene.removeChild(destination);
                        return true;
                    }
                }
                if (nextPoint < points.size() - 1) {
                    if (nextPoint-1 >-1 &&adf.getId() != points.get(nextPoint-1).getAdf().getId()){
                        adf = points.get(nextPoint-1).getAdf();
                        changeADF = true;
                        needsNextPoint=true;
                        saveNextPoint = nextPoint;
                        Log.d("DEBUGGER","ADF-Changed: " + points.get(nextPoint-1).getTag()
                                + "  "+ points.get(nextPoint-1).getAdf().toString());
                    }

                    if (!arrow.isVisible())arrow.setVisible(true);
                    Vector3 p = points.get(nextPoint).getPosition();
                    arrowPosition = new Vector3(p.x, p.y + 0.5, p.z);
                    arrow.enableLookAt();
                    Vector3 p2 = points.get(nextPoint + 1).getPosition();
                    arrow.setLookAt(p2.x, p2.y + 0.5, p2.z);

                }else{
                    if(arrow.isVisible())arrow.setVisible(false);
                }
                if (nextPoint < points.size() - 2) {
                    if (!futureArrow.isVisible())futureArrow.setVisible(true);
                    destination.setVisible(false);
                    Vector3 p = points.get(nextPoint + 1).getPosition();
                    futureArrowPosition = new Vector3(p.x, p.y + 0.5, p.z);
                    futureArrow.enableLookAt();
                    Vector3 p2 = points.get(nextPoint + 2).getPosition();
                    futureArrow.setLookAt(p2.x, p2.y + 0.5, p2.z);
                } else if (destination != null) {
                    if (!destination.isVisible())destination.setVisible(true);
                    if(futureArrow.isVisible())futureArrow.setVisible(false);
                    Vector3 p = points.get(points.size() - 1).getPosition();
                    destinationPosition = new Vector3(p.x, p.y + 0.5, p.z);
                }
                if (arrowPosition != null) {
                    arrow.setPosition(arrowPosition);
                }
                Object3D ob = compassArrow.getChildAt(0);
                if (ob.getMaterial() == null) {
                    Material m = new Material();
                    m.setColor(Color.RED);
                    m.enableLighting(true);
                    m.setDiffuseMethod(new DiffuseMethod.Lambert());
                    m.setSpecularMethod(new SpecularMethod.Phong(Color.WHITE, 60));
                    ob.setMaterial(m);
                }
                if (!scene.getChildrenCopy().contains(compassArrow)) scene.addChild(compassArrow);
                if (futureArrowPosition != null) futureArrow.setPosition(futureArrowPosition);
                if (destinationPosition != null) destination.setPosition(destinationPosition);
            }
        }
        //  Log.d("DEBUGGER", "DebugSize: " + debugObjects.getNumChildren());
        return false;
    }

    /**
     * Clears the given scene
     * @param scene the scene that have to be cleaned
     */
    public void clear(RajawaliScene scene) {
        points.clear();
        arrow.setVisible(false);
        futureArrow.setVisible(false);
        destination.setVisible(false);
        compassArrow.setVisible(false);
        //Save the Backscreenquad
        ScreenQuad sq = (ScreenQuad) scene.getChildrenCopy().get(0);
        //Clear all Elements from Scene
        scene.clearChildren();
        //Add the Backscreenquad back again
        scene.addChildAt(sq, 0);
        //scene.addChild(debugObjects);
    }


    /**
     or this Building Sets all Points f
     */
    public void setAllPoints() {
        debugClear();
        Log.d("DEBUGGER", "DebugSize: " + allPoints.size());
        for (Point p : allPoints) {
            Cube s = new Cube(0.1f);

            s.setPosition(PosCalculator.newPos(adf, p));

            if (p.getProperties().containsValue(PointProperties.destination)) {
                s.setMaterial(material2);
            } else {
                s.setMaterial(material1);
            }
            debugObjects.addChild(s);
            for (Point n : p.getNeighbours().keySet()) {
                Stack<Vector3> stack = new Stack<Vector3>();
                stack.add(PosCalculator.newPos(adf, p));
                stack.add(PosCalculator.newPos(adf, n));

                Line3D line = new Line3D(stack, 50, Color.RED);
                Material m = new Material();
                m.setColor(Color.RED);
                line.setMaterial(m);
                debugObjects.addChild(line);
            }
        }
        Log.d("DEBUGGER", "DebugSize: " + debugObjects.getNumChildren());
    }


    /**
     * Clears the Debug Object3D
     */
    public void debugClear() {

        Log.d("DEBUGGER", "Debug clear");
        int x = debugObjects.getNumChildren();
        for (int i = 0; i < x; i++) {
            debugObjects.removeChild(debugObjects.getChildAt(0));
        }

    }

    /**
     * Returns an instanz of this class
     * @param renderer the renerer where visualize works
     * @return
     */
    public static Visualize getInstance(RajawaliRenderer renderer) {
        if (renderer == null) return null;
        if (instance == null) instance = new Visualize(renderer);
        return instance;
    }

    /**
     * actualize the compass arrow synchrony to the camera
     * @param pose The Posedata of the camera
     */
    public void setCompassArrow(Pose pose) {
        if(pose !=null){
            compassArrow.setVisible(false);
        }else {

            if (!points.isEmpty() && nextPoint < points.size()) {
                compassArrow.setVisible(true);
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

    /**
     * Sets the nextPoint variable where the navigation has to run after
     * relocalisation of the system
     * @param nextPoint
     */
    public void setNextPoint(int nextPoint) {
        this.nextPoint= nextPoint;
        this.needsNextPoint =false;
    }
}
