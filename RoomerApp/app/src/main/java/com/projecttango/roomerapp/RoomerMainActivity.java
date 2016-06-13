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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.atap.tango.ux.TangoUx;
import com.google.atap.tangoservice.*;
import com.google.atap.tangoservice.TangoCameraIntrinsics;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoPoseData;
import com.projecttango.DataStructure.Point;
import com.projecttango.DataStructure.RoomerDB;
import com.projecttango.rajawali.DeviceExtrinsics;
import com.projecttango.roomerapp.tango.ConnectRunnable;
import com.projecttango.roomerapp.ui.SetUpUI;

import org.rajawali3d.surface.RajawaliSurfaceView;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Main Activity class for the Motion Tracking API Sample. Handles the connection to the Tango
 * service and propagation of Tango pose data to OpenGL and Layout views. OpenGL rendering logic is
 * delegated to the {@link RoomerRenderer} class.
 */
public class RoomerMainActivity extends Activity {


    public static final String TAG = RoomerMainActivity.class.getSimpleName();



    public static TangoCoordinateFramePair FRAME_PAIR = new TangoCoordinateFramePair(
            TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE,
            TangoPoseData.COORDINATE_FRAME_DEVICE);


    public Tango mTango;
    public TangoUx mTangoUx;
    public RoomerRenderer mRenderer;



    public AtomicBoolean mIsConnected = new AtomicBoolean(false);

    private String uuid;
    public RoomerDB db;

    public boolean mIsRelocalized;

    public AtomicBoolean mIsFrameAvailableTangoThread = new AtomicBoolean(false);
    public double mRgbTimestampGlThread;

    public RajawaliSurfaceView mSurfaceView;
    public TangoCameraIntrinsics mIntrinsics;
    public DeviceExtrinsics mExtrinsics;
    public double mCameraPoseTimestamp = 0;

    private static final int INVALID_TEXTURE_ID = 0;
    public int mConnectedTextureIdGlThread = INVALID_TEXTURE_ID;

    /**
     * Holds all points loaded from the database.
     */
    public ArrayList<Point> points = new ArrayList<Point>();

    private SetUpUI ui;


    public boolean firstTimeloaded = false;


    /**
     * Checks if the
     */
    private boolean isDebug = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_roomer);
        ui = SetUpUI.getInstance(this);
        final Intent i = getIntent();
        uuid = i.getStringExtra("uuid");
        mRenderer = setupGLViewAndRenderer();
        mTangoUx = ui.setupTangoUxAndLayout();

        db = new RoomerDB(this, uuid);
        try {
            db.importDB(getBaseContext());

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * Sets Rajawalisurface view and its renderer. This is ideally called only once in onCreate.
     */
    private RoomerRenderer setupGLViewAndRenderer() {
        RoomerRenderer renderer = new RoomerRenderer(this);
        mSurfaceView = (RajawaliSurfaceView) findViewById(R.id.gl_surface_view);
        mSurfaceView.setEGLContextClientVersion(2);
        mSurfaceView.setSurfaceRenderer(renderer);
        return renderer;

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mIsConnected.compareAndSet(true, false)) {
            mTangoUx.stop();
            mIsRelocalized = false;
            mRenderer.onPause();
            mRenderer.getCurrentScene().clearFrameCallbacks();
            mTango.disconnectCamera(TangoCameraIntrinsics.TANGO_CAMERA_COLOR);
            // We need to invalidate the connected texture ID so that we cause a re-connection
            // in the OpenGL thread after resume
            mConnectedTextureIdGlThread = INVALID_TEXTURE_ID;
            try {
                mTango.disconnect();
            } catch (TangoErrorException e) {
                Toast.makeText(getApplicationContext(),
                        R.string.exception_tango_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsConnected.compareAndSet(false, true)) {
            // Initialize Tango Service as a normal Android Service, since we call
            // mTango.disconnect() in onPause, this will unbind Tango Service, so
            // everytime when onResume get called, we should create a new Tango object.
            mTangoUx.start(new TangoUx.StartParams());
            mTango = new Tango(RoomerMainActivity.this, new ConnectRunnable(this,uuid));
        }
    }

    public void loadAreaDescription(String uuid){
        if(uuid == null)  mTango.experimentalLoadAreaDescription(this.uuid);
        mTango.experimentalLoadAreaDescription(uuid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }





    public void setDebug(View view){
        isDebug = !isDebug;
        mRenderer.isDebug = isDebug;
        mRenderer.debugRerender = true;
        if(isDebug){
            ui.getTxtFPS().setVisibility(View.VISIBLE);
        }else{
            ui.getTxtFPS().setVisibility(View.VISIBLE);
        }
        Log.d("DEBUGGER","Debugging:" +isDebug);

    }
}
