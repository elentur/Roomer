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

package com.example.roberto.myapplication;

import android.widget.*;
import com.example.roberto.myapplication.KDTree.KD3DNode;
import com.example.roberto.myapplication.model.PointsRoutModel;
import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.Tango.OnTangoUpdateListener;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoEvent;
import com.google.atap.tangoservice.TangoOutOfDateException;
import com.google.atap.tangoservice.TangoPoseData;
import com.google.atap.tangoservice.TangoXyzIjData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import org.rajawali3d.math.vector.Vector3;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Main Activity for the Tango Java Quickstart. Demonstrates establishing a
 * connection to the {@link Tango} service and printing the {@link }
 * data to the LogCat. Also demonstrates Tango lifecycle management through
 * {@link TangoConfig}.
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String sTranslationFormat = "Translation: %f, %f, %f";
    private static final String sRotationFormat = "Rotation: %f, %f, %f, %f";

    private static final int SECS_TO_MILLISECS = 1000;
    private static final double UPDATE_INTERVAL_MS = 100.0;

    private double mPreviousTimeStamp;
    private double mTimeToNextUpdate = UPDATE_INTERVAL_MS;

    private TextView mTranslationTextView;
    private TextView mRotationTextView;

    private Tango mTango;
    private TangoConfig mConfig;
    private boolean mIsTangoServiceConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTranslationTextView = (TextView) findViewById(R.id.translation_textview);
        mRotationTextView = (TextView) findViewById(R.id.rotation_textview);

        LinearLayout left = (LinearLayout) findViewById(R.id.left);
        LinearLayout middle = (LinearLayout) findViewById(R.id.middle);
        LinearLayout right = (LinearLayout) findViewById(R.id.right);

        ArrayList<Vector3> list = new ArrayList<Vector3>();

        int numpoints = 10;

        //list = randomList(numpoints);
        list = zChainList(2,2,numpoints);

        TextView text = new TextView(this);

        DecimalFormat df = new DecimalFormat("####0.00");

        text.setText("ungeordnete Werte");

        for (Vector3 vec : list){

            text.setText(text.getText() + "\n" +df.format(vec.x)+","+df.format(vec.y)+","+df.format(vec.z));
        }

        left.addView(text);


        Log.i("List", list.toString());

        PointsRoutModel p = new PointsRoutModel(list);

        ArrayList<Vector3> inOrderList = p.listInOrder();

        text = new TextView(this);
        text.setText("geordnete Werte");

        for(Vector3 vec : inOrderList){
            text.setText(text.getText() + "\n" +df.format(vec.x)+","+df.format(vec.y)+","+df.format(vec.z));
        }

        middle.addView(text);

        Log.i("List", inOrderList.toString());

        Vector3 start = inOrderList.get(4);
        Vector3 end = inOrderList.get(7);

        ArrayList<Vector3> shortesWayList = p.getShortestWay(start,end);

        text = new TextView(this);
        text.setText("kürzester Weg");

        text.setText(text.getText() + "\nStart: " +df.format(start.x)+","+df.format(start.y)+","+df.format(start.z));
        text.setText(text.getText() + "\nEnd: " +df.format(end.x)+","+df.format(end.y)+","+df.format(end.z));

        for(Vector3 vec : shortesWayList){

            text.setText(text.getText() + "\n" +df.format(vec.x)+","+df.format(vec.y)+","+df.format(vec.z));
        }

        right.addView(text);

        Log.i("List", shortesWayList.toString());

    }

    private static ArrayList<Vector3> zChainList(int x, int y, int num) {
        ArrayList<Vector3> list = new ArrayList<Vector3>();

        for (int i = 0; i < num; i++) {

            list.add(new Vector3(x, y, i  - num/2));
        }

        return list;
    }

    private static ArrayList<Vector3> randomList(int num){
        ArrayList<Vector3> list = new ArrayList<Vector3>();

        Random r = new Random();

        for (int i = 0; i < num; i++) {

            list.add(
                    new Vector3(
                            -10.0 + r.nextDouble() * 20.0,
                            -10.0 + r.nextDouble() * 20.0,
                            -10.0 + r.nextDouble() * 20.0
                    )
            );
        }

        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsTangoServiceConnected) {
            // Initialize Tango Service as a normal Android Service, since we call
            // mTango.disconnect() in onPause, this will unbind Tango Service, so
            // everytime when onResume get called, we should create a new Tango object.
            mTango = new Tango(MainActivity.this, new Runnable() {
                // Pass in a Runnable to be called from UI thread when Tango is ready,
                // this Runnable will be running on a new thread.
                // When Tango is ready, we can call Tango functions safely here only
                // when there is no UI thread changes involved.
                @Override
                public void run() {
                    // Set up Tango configuration for motion tracking
                    // If you want to use other APIs, add more appropriate to the config
                    // like: mConfig.putBoolean(TangoConfig.KEY_BOOLEAN_DEPTH, true)
                    mConfig = mTango.getConfig(TangoConfig.CONFIG_TYPE_CURRENT);
                    mConfig.putBoolean(TangoConfig.KEY_BOOLEAN_MOTIONTRACKING, true);
                    try {
                        setTangoListeners();
                    } catch (TangoErrorException e) {
                        Log.e(TAG, "Tango Error! Restart the app!", e);
                    }
                    try {
                        mTango.connect(mConfig);
                        mIsTangoServiceConnected = true;
                    } catch (TangoOutOfDateException e) {
                        Log.e(TAG, "Tango Service out of date!", e);
                    } catch (TangoErrorException e) {
                        Log.e(TAG, "Tango Error! Restart the app!", e);
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // When the app is pushed to the background, unlock the Tango
        // configuration and disconnect
        // from the service so that other apps will behave properly.
        try {
            mTango.disconnect();
            mIsTangoServiceConnected = false;
        } catch (TangoErrorException e) {
            Toast.makeText(getApplicationContext(), "Tango Error!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setTangoListeners() {
        // Select coordinate frame pairs
        ArrayList<TangoCoordinateFramePair> framePairs = new ArrayList<TangoCoordinateFramePair>();
        framePairs.add(new TangoCoordinateFramePair(
                TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE,
                TangoPoseData.COORDINATE_FRAME_DEVICE));

        // Add a listener for Tango pose data
        mTango.connectListener(framePairs, new OnTangoUpdateListener() {

            @Override
            public void onPoseAvailable(TangoPoseData pose) {
                // Format Translation and Rotation data
                final String translationMsg = String.format(sTranslationFormat,
                        pose.translation[0], pose.translation[1],
                        pose.translation[2]);
                final String rotationMsg = String.format(sRotationFormat,
                        pose.rotation[0], pose.rotation[1], pose.rotation[2],
                        pose.rotation[3]);

                // Output to LogCat
                String logMsg = translationMsg + " | " + rotationMsg;
                Log.i(TAG, logMsg);

                final double deltaTime = (pose.timestamp - mPreviousTimeStamp)
                        * SECS_TO_MILLISECS;
                mPreviousTimeStamp = pose.timestamp;
                mTimeToNextUpdate -= deltaTime;

                // Throttle updates to the UI based on UPDATE_INTERVAL_MS.
                if (mTimeToNextUpdate < 0.0) {
                    mTimeToNextUpdate = UPDATE_INTERVAL_MS;

                    // Display data in TextViews. This must be done inside a
                    // runOnUiThread call because
                    // it affects the UI, which will cause an error if performed
                    // from the Tango
                    // service thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*mRotationTextView.setText(rotationMsg);
                            mTranslationTextView.setText(translationMsg);*/
                        }
                    });
                }
            }

            @Override
            public void onXyzIjAvailable(TangoXyzIjData arg0) {
                // Ignoring XyzIj data
            }

            @Override
            public void onTangoEvent(TangoEvent arg0) {
                // Ignoring TangoEvents
            }

            @Override
            public void onFrameAvailable(int arg0) {
                // Ignoring onFrameAvailable Events
            }
        });
    }

}
