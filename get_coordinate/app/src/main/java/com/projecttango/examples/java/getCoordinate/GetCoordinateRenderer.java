/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.projecttango.examples.java.getCoordinate;

import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoPoseData;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.projecttango.DataStructure.*;
import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.util.OnObjectPickedListener;

import com.projecttango.rajawali.Pose;
import com.projecttango.rajawali.renderables.Grid;

import com.projecttango.tangosupport.TangoSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import static com.projecttango.DataStructure.PointProperties.*;

/**
 * This class implements the rendering logic for the Motion Tracking application using Rajawali.
 */
public class GetCoordinateRenderer extends RajawaliRenderer implements OnObjectPickedListener {
    private static final String TAG = GetCoordinateRenderer.class.getSimpleName();

    private static final float CAMERA_NEAR = 0.01f;
    private static final float CAMERA_FAR = 200f;

    // Latest available device pose;
    private Pose mDevicePose = new Pose(Vector3.ZERO, Quaternion.getIdentity());
    private boolean mPoseUpdated = false;
  
    // The current screen rotation index. The index value follow the Android surface rotation enum:
    // http://developer.android.com/reference/android/view/Surface.html#ROTATION_0
    private int mCurrentScreenRotation = 0;

    public boolean addNavPoint =true;
    public boolean addDestPoint =true;
    private int countDestPoints = 0;
    private int countNavPoints =0;

    public boolean reloadList = false;

    private LinkedHashMap<Object3D,Point> points = new LinkedHashMap<Object3D, Point>();
    public boolean reDraw = false;

    public boolean isRelocated = false;
    private ObjectColorPicker mPicker;
    private Point selectetPoint;


    private Material mSphereMaterial = new Material();
    private boolean pointsClear =  false;
    public RoomerDB db;
    public ADF adf;

    { mSphereMaterial.enableLighting(true);
        mSphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        mSphereMaterial.setSpecularMethod(new SpecularMethod.Phong());}
    private Material mSphereMaterialGreen = new Material();
    { mSphereMaterial.enableLighting(true);
        mSphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        mSphereMaterial.setSpecularMethod(new SpecularMethod.Phong());
    mSphereMaterialGreen.setColor(Color.GREEN);}

    private ArrayList<Line3D> lines = new ArrayList<Line3D>();


    public GetCoordinateRenderer(Context context) {
        super(context);
    }




    public void setCurrentScreenRotation(int currentRotation) {
        mCurrentScreenRotation = currentRotation;
        //Zeigt an ob Tablet in Hoch oder querformat und wie herum
    }
  
    @Override
    protected void initScene() {
        Grid grid = new Grid(100, 1, 1, 0xFFCCCCCC);
        grid.setPosition(0, -1.3f, 0);
        getCurrentScene().addChild(grid);

        DirectionalLight light = new DirectionalLight(1, 0.2, -1);
        light.setColor(1, 1, 1);
        light.setPower(0.8f);
        light.setPosition(3, 2, 4);
        getCurrentScene().addLight(light);

        DirectionalLight light2 = new DirectionalLight(-1, 0.2, -1);
        light.setColor(1, 4, 4);
        light.setPower(0.8f);
        light.setPosition(3, 3, 3);
        getCurrentScene().addLight(light2);

        mPicker = new ObjectColorPicker(this);
        mPicker.setOnObjectPickedListener(this);



        getCurrentScene().setBackgroundColor(Color.WHITE);


        getCurrentCamera().setNearPlane(CAMERA_NEAR);
        getCurrentCamera().setFarPlane(CAMERA_FAR);
    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        // Update the scene objects with the latest device position and orientation information.
        // Synchronize to avoid concurrent access from the Tango callback thread below.
        try {
            if(!addNavPoint){
                addNavPoint();

            }
            if(!addDestPoint){
                addDestPoint();
            }

            if (pointsClear){
                pointsClear=false;

                getCurrentScene().clearChildren();
                Grid grid = new Grid(100, 1, 1, 0xFFCCCCCC);
                grid.setPosition(0, -1.3f, 0);
                getCurrentScene().addChild(grid);

            }

            if(reDraw){
                reDraw=false;
                for(Line3D line : lines){
                    getCurrentScene().removeChild(line);
                }

               for(Object3D key: points.keySet()) {
                    Point p = points.get(key);
                   for (Point n : p.getNeighbours().keySet()) {
                       Stack<Vector3> stack = new Stack<Vector3>();
                       stack.add(key.getPosition());
                       if(n.getAdf().equals(adf)){
                           stack.add(n.getPosition());
                       }else{
                           Vector3 offset = Vector3.subtractAndCreate(n.getAdf().getPosition(),adf.getPosition());
                           stack.add(Vector3.addAndCreate(n.getPosition(),offset));

                       }

                       Line3D line = new Line3D(stack, 50, Color.RED);
                       Material m = new Material();
                       m.setColor(Color.RED);
                       line.setMaterial(m);
                       lines.add(line);
                       getCurrentScene().addChild(line);
                   }
               }
            }


            TangoPoseData pose =null;
            try {
               if(isRelocated){
                   pose =
                           TangoSupport.getPoseAtTime(0.0, TangoPoseData.COORDINATE_FRAME_AREA_DESCRIPTION,
                                   TangoPoseData.COORDINATE_FRAME_DEVICE,
                                   TangoSupport.TANGO_SUPPORT_ENGINE_OPENGL,
                                   mCurrentScreenRotation);
               }else{
                   pose =
                           TangoSupport.getPoseAtTime(0.0, TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE,
                                   TangoPoseData.COORDINATE_FRAME_DEVICE,
                                   TangoSupport.TANGO_SUPPORT_ENGINE_OPENGL,
                                   mCurrentScreenRotation);
               }
            }catch (Exception e){
                Log.e("Render_Pose_error", "Keine Pose Daten");
            }
            if(pose !=null) {
                if (pose.statusCode == TangoPoseData.POSE_VALID) {
                    getCurrentCamera().setPosition((float) pose.translation[0],
                            (float) pose.translation[1],
                            (float) pose.translation[2]);


                    Quaternion invOrientation = new Quaternion((float) pose.rotation[3],
                            (float) pose.rotation[0],
                            (float) pose.rotation[1],
                            (float) pose.rotation[2]);

                    // For some reason, rajawalli's orientation is inversed.
                    Quaternion orientation = invOrientation.inverse();
                    getCurrentCamera().setOrientation(orientation);
                }
            }
        } catch (TangoErrorException e) {
            Log.e(TAG, "TangoSupport.getPoseAtTime error", e);
        }

        // Perform the actual OpenGL rendering of the updated objects
        super.onRender(ellapsedRealtime, deltaTime);
    }

    @Override
    public void onOffsetsChanged(float v, float v1, float v2, float v3, int i, int i1) {
      // Unused, but needs to be declared to adhere to the IRajawaliSurfaceRenderer interface.
    }

    @Override
    public void onTouchEvent(MotionEvent motionEvent) {
      // Unused, but needs to be declared to adhere to the IRajawaliSurfaceRenderer interface.
    }

    public  void addNavPoint() {
        addNavPoint = true;
        Vector3 p = new Vector3(getCurrentCamera().getPosition().x,getCurrentCamera().getPosition().y-1,getCurrentCamera().getPosition().z);
        Sphere s = new Sphere(0.1f, 20, 20);
        s.setMaterial(mSphereMaterial);
        getCurrentScene().addChild(s);
        s.setPosition(p);

        mPicker.registerObject(s);

        HashMap<PointProperties,PointProperties> properties = new HashMap<PointProperties, PointProperties>();

        properties.put(type,navigation);

        Point point = db.createPoint(p,properties,"NavPoint" +countNavPoints,adf);
        Log.d("DEBUGGER", "addNavPoint: " +  point);

        countNavPoints++;
        points.put(s,point);
        selectetPoint = point;
        reloadList=true;
    }
    public  void addDestPoint() {
        addDestPoint = true;
        Vector3 p = new Vector3(getCurrentCamera().getPosition().x,getCurrentCamera().getPosition().y-1,getCurrentCamera().getPosition().z);
        Sphere s = new Sphere(0.1f, 20, 20);
        s.setMaterial(mSphereMaterialGreen);
        getCurrentScene().addChild(s);
        s.setPosition(p);

        mPicker.registerObject(s);

        HashMap<PointProperties,PointProperties> properties = new HashMap<PointProperties, PointProperties>();

        properties.put(type,destination);

        Point point = db.createPoint(p,properties,"DestPoint" +countDestPoints,adf);

        countDestPoints++;
        points.put(s,point);
        selectetPoint = point;
        reloadList=true;
    }

    @Override
    public void onObjectPicked(Object3D object) {
        selectetPoint = points.get(object);
        reloadList=true;
    }

    public void getObjectAt(float x, float y) {
        mPicker.getObjectAt(x, y);
    }

    public ArrayList<Point> getPoints(){
        ArrayList<Point> p = new ArrayList<Point>( points.values());
        return p;
    }

    public void clearPoints(){
        pointsClear = true;
        db.deletePoints(points.values());
        points.clear();
        reDraw =true;
        countDestPoints=0;
        countNavPoints=0;
        selectetPoint=null;
        reloadList=true;
    }

    public Point getSelectetPoint() {
        return selectetPoint;
    }

    public void removePoint() {
        for(Object3D key : points.keySet()){
            if(points.get(key).equals(selectetPoint)){

                getCurrentScene().removeChild(key);
                Point p = points.remove(key);
                for(Object3D key2 : points.keySet()){
                    points.get(key2).getNeighbours().remove(p);

                }
                for (Point n : p.getNeighbours().keySet()){
                    db.deleteEdge(p,n);
                    db.deleteEdge(n,p);
                }
                db.deletePoint(p);
                selectetPoint =null;
                reDraw =true;
                return;
            }
        }
    }

    public void setPoints(ArrayList<Point> points) {
        Log.d("DEBUGGER","PointSize: " + points.size());
        for (Point p : points) {
            Sphere s = new Sphere(0.1f, 20, 20);
            getCurrentScene().addChild(s);
           if(p.getAdf().equals(adf)){
               s.setPosition(p.getPosition());
           }else{
               Vector3 offset = Vector3.subtractAndCreate(p.getAdf().getPosition(),adf.getPosition());
               s.setPosition(Vector3.addAndCreate(p.getPosition(),offset));

           }
            mPicker.registerObject(s);
            this.points.put(s, p);
            if (p.getProperties().get(PointProperties.type).equals(PointProperties.destination)) {
                s.setMaterial(mSphereMaterialGreen);
                countDestPoints++;
            } else {
                s.setMaterial(mSphereMaterial);
                countNavPoints++;
            }
        }
        reDraw=true;

    }
}
