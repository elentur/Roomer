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

package com.projecttango.roomerapp;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.google.atap.tangoservice.TangoCameraIntrinsics;
import com.google.atap.tangoservice.TangoPoseData;
import com.projecttango.DataStructure.ADF;
import com.projecttango.DataStructure.Point;
import com.projecttango.Dijkstra.VectorGraph;
import com.projecttango.Visualisation.Visualize;
import com.projecttango.rajawali.DeviceExtrinsics;
import com.projecttango.rajawali.Pose;
import com.projecttango.rajawali.ScenePoseCalculator;
import com.projecttango.roomerapp.ui.SetUpUI;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.ScreenQuad;
import org.rajawali3d.renderer.RajawaliRenderer;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * This class implements the rendering logic for the Motion Tracking application using Rajawali.
 */
public class RoomerRenderer extends RajawaliRenderer {

    private static final String TAG = RoomerRenderer.class.getSimpleName();


    private static final float CAMERA_NEAR = 0.01f;
    private static final float CAMERA_FAR = 200f;

    // The current screen rotation index. The index value follow the Android surface rotation enum:
    // http://developer.android.com/reference/android/view/Surface.html#ROTATION_0


    public ArrayList<Point> points = new ArrayList<Point>();
    private boolean reDraw = false;
    public boolean isDebug = false;
    protected ArrayList<Point> allPoints = new ArrayList<Point>();
    public boolean debugRerender = false;
    public boolean clear = false;

    private long timeStamp = 0;
    private long fps = 0;
    public int globalFPS = 0;

    public Visualize vis;
    public boolean isRelocated = false;
    public boolean destArrive = false;
    private PointLight light3;
    public ADF adf;
    private boolean waitForCalcPath = false;
    private Point destPoint;
    public int nextPoint=0;

    public RoomerRenderer(Context context) {
        super(context);
    }

    // Rajawali texture used to render the Tango color camera
    private ATexture mTangoCameraTexture;

    // Keeps track of whether the scene camera has been configured
    private boolean mSceneCameraConfigured;

    @Override
    protected void initScene() {

        // Create a quad covering the whole background and assign a texture to it where the
        // Tango color camera contents will be rendered.
        ScreenQuad backgroundQuad = new ScreenQuad();
        Material tangoCameraMaterial = new Material();
        tangoCameraMaterial.setColorInfluence(0);
        // We need to use Rajawali's {@code StreamingTexture} since it sets up the texture
        // for GL_TEXTURE_EXTERNAL_OES rendering
        mTangoCameraTexture =
                new StreamingTexture("camera", (StreamingTexture.ISurfaceListener) null);
        try {
            tangoCameraMaterial.addTexture(mTangoCameraTexture);
            backgroundQuad.setMaterial(tangoCameraMaterial);
        } catch (ATexture.TextureException e) {
            Log.e(TAG, "Exception creating texture for RGB camera contents", e);
        }
        getCurrentScene().addChildAt(backgroundQuad, 0);

        light3 = new PointLight();

        light3.setColor(1, 1, 1);
        light3.setPower(1.0f);
        light3.setPosition(0, 2, 0);
        getCurrentScene().addLight(light3);

        getCurrentCamera().setNearPlane(CAMERA_NEAR);
        getCurrentCamera().setFarPlane(CAMERA_FAR);

        vis = Visualize.getInstance(this);

        //vis.setPoints(null);
        timeStamp = System.currentTimeMillis();
    }

    /**
     * OnREnder Method
     * @param ellapsedRealtime
     * @param deltaTime
     */
    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {

        //For debug representation of framerate
        fps++;
        globalFPS = (int) (fps / ((System.currentTimeMillis() - timeStamp) / 1000.0));
        if (fps > 1000) {
            fps = 0;
            timeStamp = System.currentTimeMillis();
        }
        nextPoint=vis.saveNextPoint;
        try {
            //Clear Scene
            if (clear) {
                clear = false;
                reDraw = false;
                vis.clear(getCurrentScene());


            }
            if(isRelocated && waitForCalcPath)calcPath();

            //Redraw Scene
            if (reDraw && isRelocated) {

                if (light3 != null) light3.setPosition(getCurrentCamera().getPosition());
                if(vis.needsNextPoint&& adf.equals(vis.points.get(nextPoint).getAdf()))vis.setNextPoint(nextPoint);
                destArrive = vis.draw(getCurrentScene(), getCurrentCamera());
                reDraw = !destArrive;
            }

            if (isDebug && debugRerender) {

                debugRerender = false;
                vis.isDebug=true;

               //if(adf !=null) vis.debugDraw(allPoints );
                reDraw = true;
            } else if (!isDebug && debugRerender) {
                debugRerender = false;
                vis.isDebug=false;
            }

        } catch (Exception e) {
            Log.e("DEBUGGER", "in onRender: " + e.getMessage());
        }

        // Perform the actual OpenGL rendering of the updated objects
        super.onRender(ellapsedRealtime, deltaTime);
    }

    @Override
    public void onOffsetsChanged(float v, float v1, float v2, float v3, int i, int i1) {
        // Unused, but needs to be declared to adhere to the IRajawaliSurfaceRenderer interface.
    }

    /**
     * We need to override this method to mark the camera for re-configuration (set proper
     * projection matrix) since it will be reset by Rajawali on surface changes.
     */
    @Override
    public void onRenderSurfaceSizeChanged(GL10 gl, int width, int height) {
        super.onRenderSurfaceSizeChanged(gl, width, height);
        mSceneCameraConfigured = false;
    }

    public boolean isSceneCameraConfigured() {
        return mSceneCameraConfigured;
    }

    /**
     * Sets the projection matrix for the scen camera to match the parameters of the color camera,
     * provided by the {@code TangoCameraIntrinsics}.
     */
    public void setProjectionMatrix(TangoCameraIntrinsics intrinsics) {
        Matrix4 projectionMatrix = ScenePoseCalculator.calculateProjectionMatrix(
                intrinsics.width, intrinsics.height,
                intrinsics.fx, intrinsics.fy, intrinsics.cx, intrinsics.cy);
        getCurrentCamera().setProjectionMatrix(projectionMatrix);

    }

    @Override
    public void onTouchEvent(MotionEvent motionEvent) {
        // Unused, but needs to be declared to adhere to the IRajawaliSurfaceRenderer interface.

    }

    /**
     * Update the scene camera based on the provided pose in Tango start of service frame.
     * The device pose should match the pose of the device at the time the last rendered RGB
     * frame, which can be retrieved with this.getTimestamp();
     * NOTE: This must be called from the OpenGL render thread - it is not thread safe.
     */
    public void updateRenderCameraPose(TangoPoseData devicePose, DeviceExtrinsics extrinsics) {
        Pose cameraPose = ScenePoseCalculator.toOpenGlCameraPose(devicePose, extrinsics);
        getCurrentCamera().setRotation(cameraPose.getOrientation());
        getCurrentCamera().setPosition(cameraPose.getPosition());

        if(isRelocated){
            vis.setCompassArrow(cameraPose);
        }else{
            vis.setCompassArrow(null);
        }


    }


    /**
     * It returns the ID currently assigned to the texture where the Tango color camera contents
     * should be rendered.
     * NOTE: This must be called from the OpenGL render thread - it is not thread safe.
     */
    public int getTextureId() {
        return mTangoCameraTexture == null ? -1 : mTangoCameraTexture.getTextureId();
    }

    /**
     * Sets the Points for Calculation and the Destination Points
     * @param points
     * @param destpoint
     */
    public void setPoints(ArrayList<Point> points, Point destpoint) {
        this.destPoint = destpoint;
        this.points = points;
        this.waitForCalcPath = true;

    }

    /**
     * Calculates the Points for  the route an gives it in the
     * right direction
     */
    private void calcPath(){
    Vector3 pos = new Vector3(getCurrentCamera().getPosition().x,
            getCurrentCamera().getPosition().y - 1,
            getCurrentCamera().getPosition().z);
    waitForCalcPath=false;
    vis.setPoints(VectorGraph.getPath(pos,destPoint,points),getContext());
    vis.adf =adf;
    reDraw = true;
    Log.d("DEBUGGER", "calcPath");
}

    /**
     * Set All Points for this Building
     * @param points All points
     */
    public void setAllPoints(ArrayList<Point> points)
    {
        Log.d("DEBUGGER", "setAllPoints");
        vis.adf =adf;
        allPoints = points;
        vis.allPoints = allPoints;
    }
}
