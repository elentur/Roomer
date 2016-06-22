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

package com.projecttango.examples.java.potalPointExportToAdf;

import com.google.atap.tango.ux.TangoUx;
import com.google.atap.tango.ux.TangoUxLayout;
import com.google.atap.tango.ux.UxExceptionEvent;
import com.google.atap.tango.ux.UxExceptionEventListener;
import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.Tango.OnTangoUpdateListener;
import com.google.atap.tangoservice.TangoAreaDescriptionMetaData;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoEvent;
import com.google.atap.tangoservice.TangoOutOfDateException;
import com.google.atap.tangoservice.TangoPoseData;
import com.google.atap.tangoservice.TangoXyzIjData;
import com.projecttango.DataStructure.Point;
import com.projecttango.DataStructure.RoomerDB;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.ASceneFrameCallback;
import org.rajawali3d.surface.RajawaliSurfaceView;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is the mainActivity of the Tool which provides saving a vector from the
 * coordinate system of one ADF to the metadata of a nother ADF to the meta data
 * of an
 */
public class AdfPointCoordinateActivity extends Activity implements View.OnTouchListener {

    private static final String TAG = AdfPointCoordinateActivity.class.getSimpleName();
    private static final int SECS_TO_MILLISECS = 1000;
    private static final double UPDATE_INTERVAL_MS = 100.0;

    private double mXyIjPreviousTimeStamp;;
    private double mTimeToNextUpdate = UPDATE_INTERVAL_MS;

    private Tango mTango;

    private TangoUx mTangoUx;
    private TangoConfig mConfig;
    private AdfPointCoordinateRenderer mRenderer;
    private final Object mSharedLock = new Object();
    private boolean mIsRelocalized;

    private AtomicBoolean mIsConnected = new AtomicBoolean(false);

    private LinearLayout lltSavePoint;
    private TextView txtLocalized;
    private Button btnSaveCoordinates;

    private EditText txtName;
    private ListView lstPoints;
    private  ArrayList<String> adfNames;
    private ArrayAdapter<String> adapter;
    private TangoPoseData poseData = new TangoPoseData();
    private String uuid;
    private ArrayList<Point> savePointList = new ArrayList<Point>();
    private RoomerDB db;
    private TangoAreaDescriptionMetaData metaData;
    private Vector3 portal;

    private TextView portalDataTextField;

    private ArrayList<String> fullUuidList;
    private double[] positionInADF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_coordinate);
        Intent i = getIntent();
        uuid = i.getStringExtra("uuid");


        lstPoints = (ListView)findViewById(R.id.lstPoints);

        txtLocalized = (TextView) findViewById(R.id.txtLocalized);
        portalDataTextField = (TextView) findViewById(R.id.portalCoordinates);
        btnSaveCoordinates =  (Button)findViewById(R.id.btnAddDestination);

        mRenderer = setupGLViewAndRenderer();
        mTangoUx = setupTangoUxAndLayout();

        adapter = new ArrayAdapter<String>(AdfPointCoordinateActivity.this,
                android.R.layout.simple_selectable_list_item);

        lstPoints.setAdapter(adapter);

        db = new RoomerDB(this,uuid);



        btnSaveCoordinates.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){

                    adfAPortal();

                }

                return false;
            }
        });

        lstPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    mRenderer.reDraw = true;
                }

        });
    }

    public void adfAPortal(){

        positionInADF = poseData.translation;
        Log.d("DEBUGGER", "position" +Arrays.toString(positionInADF) );
        portalDataTextField.setText(Arrays.toString(positionInADF));

    }
    private void loadSelectedADF() {



    }
    private void saveMetaDataToAdfB(String uuid) {


        metaData = new TangoAreaDescriptionMetaData();
        metaData = mTango.loadAreaDescriptionMetaData(uuid);
        metaData.set("ADFAVector", toByteArray(positionInADF) );

    }

    /**
     * This method converts a given double array to a byte array
     * @param value the given
     * @return
     */
    public static byte[] toByteArray(double[] value) {
        byte[] bytes = new byte[8];
        for (double values : value) {
            ByteBuffer.wrap(bytes).putDouble(values);
        }

        return bytes;
    }

    /**
     * This method converts a given byte array to a double
     * @param bytes
     * @return
     */
    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    private void setUpUUIDlist() {



        fullUuidList = mTango.listAreaDescriptions();
        Collections.reverse(fullUuidList);
        adfNames = new ArrayList<String>();
        ArrayList<Double> adfPortals = new ArrayList<Double>();
        for(String uuid: fullUuidList){
            adfNames.add(new String(mTango.loadAreaDescriptionMetaData(uuid).get("name")));
        }

        Log.d("DEBUGGER","adfNames" + adfNames.toString());



    }

    /**
     * Sets Rajawalisurface view and its renderer. This is ideally called only once in onCreate.
     */
    private AdfPointCoordinateRenderer setupGLViewAndRenderer() {
        // Configure OpenGL renderer
        AdfPointCoordinateRenderer renderer = new AdfPointCoordinateRenderer(this);
        // OpenGL view where all of the graphics are drawn
        RajawaliSurfaceView glView = (RajawaliSurfaceView) findViewById(R.id.gl_surface_view);
        glView.setEGLContextClientVersion(2);
       // glView.setZOrderOnTop(false);
       // glView.setRenderMode(IRajawaliSurface.RENDERMODE_CONTINUOUSLY);
        glView.setSurfaceRenderer(renderer);
        glView.setOnTouchListener(this);
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
            mTango = new Tango(AdfPointCoordinateActivity.this, new Runnable() {
                // Pass in a Runnable to be called from UI thread when Tango is ready,
                // this Runnable will be running on a new thread.
                // When Tango is ready, we can call Tango functions safely here only
                // when there is no UI thread changes involved.
                @Override
                public void run() {
                    try {
                        connectTango();

                        mRenderer.onResume();
                        connectRenderer();
                        setUpUUIDlist();

                    } catch (TangoOutOfDateException outDateEx) {
                        if (mTangoUx != null) {
                            mTangoUx.showTangoOutOfDate();
                        }
                    } catch (TangoErrorException e) {
                        Log.e(TAG, getString(R.string.exception_tango_error), e);
                    } catch (SecurityException e) {
                        Log.e(TAG, getString(R.string.permission_motion_tracking), e);
                    }
                }
            });
        }
    }

    /**
     * Set up the callback listeners for the Tango service, then begin using the Motion
     * Tracking API. This is called in response to the user clicking the 'Start' Button.
     */
    private void connectTango() {
        // Create a new Tango Configuration and enable the AdfPointCoordinateActivity API
        TangoConfig config;
        config = mTango.getConfig(TangoConfig.CONFIG_TYPE_DEFAULT);
        config.putBoolean(TangoConfig.KEY_BOOLEAN_MOTIONTRACKING, true);

        // Tango service should automatically attempt to recover when it enters an invalid state.
        config.putBoolean(TangoConfig.KEY_BOOLEAN_AUTORECOVERY, true);

        config.putBoolean(TangoConfig.KEY_BOOLEAN_DEPTH, true);
           config.putBoolean(
                  TangoConfig.KEY_BOOLEAN_LOWLATENCYIMUINTEGRATION, true);



         //Set adf file
            config.putString(TangoConfig.KEY_STRING_AREADESCRIPTION,uuid);

        mTango.connect(config);

        // Set Tango Listeners for Poses Device wrt Start of Service, Device wrt
        // ADF and Start of Service wrt ADF
        ArrayList<TangoCoordinateFramePair> framePairs = new ArrayList<TangoCoordinateFramePair>();
        framePairs.add(new TangoCoordinateFramePair(
                TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE,
                TangoPoseData.COORDINATE_FRAME_DEVICE));
        framePairs.add(new TangoCoordinateFramePair(
                TangoPoseData.COORDINATE_FRAME_AREA_DESCRIPTION,
                TangoPoseData.COORDINATE_FRAME_DEVICE));
        framePairs.add(new TangoCoordinateFramePair(
                TangoPoseData.COORDINATE_FRAME_AREA_DESCRIPTION,
                TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE));

        mTango.connectListener(framePairs, new OnTangoUpdateListener() {
            @Override
            public void onXyzIjAvailable(TangoXyzIjData xyzij) {
                final double currentTimeStamp = xyzij.timestamp;
                final double pointCloudFrameDelta = (currentTimeStamp - mXyIjPreviousTimeStamp)
                        * SECS_TO_MILLISECS;
                mXyIjPreviousTimeStamp = currentTimeStamp;


                mTimeToNextUpdate -= pointCloudFrameDelta;

                if (mTimeToNextUpdate < 0.0) {
                    mTimeToNextUpdate = UPDATE_INTERVAL_MS;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.addAll(adfNames);

                        Log.d("DEBUGGER", "TTTHHHHREAAADFDFDF");

                         if(mIsRelocalized)txtLocalized.setText( "Localized");


                           //here i can do smthing when successfully localized

                    }
                });
                }
            }

            // Listen to Tango Events
            @Override
            public void onTangoEvent(final TangoEvent event) {
                if (mTangoUx != null) {
                    mTangoUx.updateTangoEvent(event);
                }
            }

            @Override
            public void onPoseAvailable(TangoPoseData pose) {
                if (mTangoUx != null) {
                    mTangoUx.updatePoseStatus(pose.statusCode);
                }
                poseData = pose;
                // Make sure to have atomic access to Tango Data so that
                // UI loop doesn't interfere while Pose call back is updating
                // the data.
                synchronized (mSharedLock) {
                    // Check for Device wrt ADF pose, Device wrt Start of Service pose,
                    // Start of Service wrt ADF pose (This pose determines if the device
                    // is relocalized or not).
                    if (pose.baseFrame == TangoPoseData.COORDINATE_FRAME_AREA_DESCRIPTION
                            && pose.targetFrame == TangoPoseData
                            .COORDINATE_FRAME_START_OF_SERVICE) {
                        mIsRelocalized = pose.statusCode == TangoPoseData.POSE_VALID;
                        mRenderer.isRelocated = mIsRelocalized;

                    }


                }

            }

            @Override
            public void onFrameAvailable(int cameraId) {
                // We are not using onFrameAvailable for this application.
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * Sets up TangoUX layout and sets its listener.
     */
    private TangoUx setupTangoUxAndLayout() {
        TangoUxLayout uxLayout = (TangoUxLayout) findViewById(R.id.layout_tango);
        TangoUx tangoUx = new TangoUx(this);
        tangoUx.setLayout(uxLayout);
        tangoUx.setUxExceptionEventListener(mUxExceptionListener);
        return tangoUx;
    }

    private UxExceptionEventListener mUxExceptionListener = new UxExceptionEventListener() {

        @Override
        public void onUxExceptionEvent(UxExceptionEvent uxExceptionEvent) {
            if (uxExceptionEvent.getType() == UxExceptionEvent.TYPE_LYING_ON_SURFACE) {
                Log.i(TAG, "Device lying on surface ");
            }
            if (uxExceptionEvent.getType() == UxExceptionEvent.TYPE_FEW_DEPTH_POINTS) {
                Log.i(TAG, "Very few depth points in mPoint cloud ");
            }
            if (uxExceptionEvent.getType() == UxExceptionEvent.TYPE_FEW_FEATURES) {
                Log.i(TAG, "Invalid poses in MotionTracking ");
            }
            if (uxExceptionEvent.getType() == UxExceptionEvent.TYPE_INCOMPATIBLE_VM) {
                Log.i(TAG, "Device not running on ART");
            }
            if (uxExceptionEvent.getType() == UxExceptionEvent.TYPE_MOTION_TRACK_INVALID) {
                Log.i(TAG, "Invalid poses in MotionTracking ");
            }
            if (uxExceptionEvent.getType() == UxExceptionEvent.TYPE_MOVING_TOO_FAST) {
                Log.i(TAG, "Invalid poses in MotionTracking ");
            }
            if (uxExceptionEvent.getType() == UxExceptionEvent.TYPE_OVER_EXPOSED) {
                Log.i(TAG, "Camera Over Exposed");
            }
            if (uxExceptionEvent.getType() == UxExceptionEvent.TYPE_TANGO_SERVICE_NOT_RESPONDING) {
                Log.i(TAG, "TangoService is not responding ");
            }
            if (uxExceptionEvent.getType() == UxExceptionEvent.TYPE_UNDER_EXPOSED) {
                Log.i(TAG, "Camera Under Exposed ");
            }

        }
    };

    public void connectRenderer() {
        mRenderer.getCurrentScene().registerFrameCallback(new ASceneFrameCallback() {
            @Override
            public void onPreFrame(long sceneTime, double deltaTime) {
                // NOTE: This will be executed on each cycle before rendering, called from the
                // OpenGL rendering thread

                // NOTE: Sometimes a pre-frame call will already be scheduled by the time the Tango
                // service is disconnected, so we need to check for service connection here just
                // in case. This avoid crashes when pausing the application.
                if (!mIsConnected.get()) {
                    return;
                }


            }

            @Override
            public boolean callPreFrame() {
                return true;
            }

            @Override
            public void onPreDraw(long sceneTime, double deltaTime) {

            }

            @Override
            public void onPostFrame(long sceneTime, double deltaTime) {

            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
