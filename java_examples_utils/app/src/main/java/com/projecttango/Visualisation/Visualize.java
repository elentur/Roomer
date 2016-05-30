package com.projecttango.Visualisation;

import android.graphics.Color;
import android.util.Log;

import com.projecttango.DataStructure.NavigationPoint;
import com.projecttango.DataStructure.Point;

import org.rajawali3d.curves.CatmullRomCurve3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.ScreenQuad;
import org.rajawali3d.scene.RajawaliScene;

import java.util.ArrayList;

/**
 * Created by marcu_000 on 26.05.2016.
 */
public class Visualize {

    private static ArrayList<Point> points;
    private static Material material2 = new Material();
    private static Material material1 = new Material();

    static {
        material2.setDiffuseMethod(new DiffuseMethod.Lambert());
        material2.setColor(Color.GREEN);
        material2.enableLighting(true);

        material1.setDiffuseMethod(new DiffuseMethod.Lambert());
        material1.setSpecularMethod(new SpecularMethod.Phong());
        material1.setColor(Color.YELLOW);
        material1.enableLighting(true);
    }

    private static  int start=0;

    public static void setPoints(ArrayList<Point> points) {
        Visualize.points = points;

//TEST
        /*Vector3 v1 = new Vector3(0, -1, 0);
        Vector3 v2 = new Vector3(0, -1, -1);
        Vector3 v3 = new Vector3(1, -1, -1);
        Vector3 v4 = new Vector3(2, -1, -2);
        Vector3 v5 = new Vector3(0, -1, -5);
        Visualize.points.add(new NavigationPoint(v1, null, ""));
        Visualize.points.add(new NavigationPoint(v2, null, ""));
        Visualize.points.add(new NavigationPoint(v3, null, ""));
        Visualize.points.add(new NavigationPoint(v4, null, ""));
        Visualize.points.add(new NavigationPoint(v5, null, ""));*/

    }


    public static void draw(RajawaliScene scene) {

        ScreenQuad sq = (ScreenQuad) scene.getChildrenCopy().get(0);
        scene.clearChildren();
        scene.addChildAt(sq,0);

        Vector3 cp= new Vector3(
                scene.getCamera().getPosition().x,
                scene.getCamera().getPosition().y-1,
                scene.getCamera().getPosition().z);

        /*if(points.size()>3 &&
                cp.distanceTo(points.get(3).getPosition())
                        < points.get(2).getPosition().distanceTo(points.get(3).getPosition())){
            Point p = points.get(2);
            points.remove(0);
            points.remove(1);
            points.add(0,p);
        }*/

       // Log.d("DEBUGGER",points.toString());
        if (!points.isEmpty()) {
            CatmullRomCurve3D n = new CatmullRomCurve3D();
            n.addPoint(cp);
            n.addPoint(cp);
            for (Point p : points) {
                n.addPoint(p.getPosition());
            }
            n.addPoint(points.get(points.size() - 1).getPosition());
            n.reparametrizeForUniformDistribution(n.getNumPoints() * 4);
            ArrayList<Vector3> vP = new ArrayList<Vector3>();
            int count = ((int) n.getLength(10)) * 4;
            for (int i = 0; i < count; i++) {
                Vector3 result = new Vector3();
                double t = (i * 1.0) / (count * 1.0);
                n.calculatePoint(result, t);
                vP.add(result);
            }

            for (int i = 0; i < vP.size(); i++) {
                if(i == vP.size()-1){
                   // Sphere s = new Sphere(0.2f, 10, 10);
                    Cube s = new Cube(0.2f);
                    s.setPosition(vP.get(i));
                    s.setMaterial(material1);
                    scene.addChild(s);
                }else {
                    //Sphere s = new Sphere(0.05f, 10, 10);
                    Cube s = new Cube(0.05f);
                    s.setPosition(vP.get(i));
                    s.setMaterial(material2);
                    scene.addChild(s);
                }
            }

        }

    }


    public static void main(String[] args) {
        setPoints(new ArrayList<Point>());
        draw(null);
    }


}
