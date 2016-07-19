package com.projecttango.roomerapp.tango;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.Toast;
import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoCameraIntrinsics;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoEvent;
import com.google.atap.tangoservice.TangoPoseData;
import com.google.atap.tangoservice.TangoXyzIjData;
import com.projecttango.DataStructure.ADF;
import com.projecttango.DataStructure.Point;
import com.projecttango.DataStructure.RoomerDB;
import com.projecttango.Visualisation.ToastHandler;
import com.projecttango.Visualisation.Visualize;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.RoomerUIThread;
import com.projecttango.roomerapp.ui.SetUpUI;

import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;

/**
 * Created by Marcus Bätz on 09.06.2016.
 *
 */
public class RoomerTangoUpdateListener implements Tango.OnTangoUpdateListener {
    private static final double UPDATE_INTERVAL_MS = 100.0;
    private static final int SECS_TO_MILLISECS = 1000;
    private double mXyIjPreviousTimeStamp;
    private double mTimeToNextUpdate = UPDATE_INTERVAL_MS;
    private final Object mSharedLock = new Object();
    private  RoomerDB db;



    private final RoomerMainActivity main;

    public RoomerTangoUpdateListener(RoomerMainActivity main) {
        this.main = main;
        db = new RoomerDB(main.getBaseContext());
    }

    @Override
    public void onXyzIjAvailable(TangoXyzIjData xyzij) {
        final SetUpUI init = SetUpUI.getInstance(main);
        final double currentTimeStamp = xyzij.timestamp;
        final double pointCloudFrameDelta = (currentTimeStamp - mXyIjPreviousTimeStamp)
                * SECS_TO_MILLISECS;
        mXyIjPreviousTimeStamp = currentTimeStamp;


        mTimeToNextUpdate -= pointCloudFrameDelta;

        if (mTimeToNextUpdate < 0.0) {
            mTimeToNextUpdate = UPDATE_INTERVAL_MS;

            main.runOnUiThread(new RoomerUIThread(main));
        }
    }

    @Override
    public void onTangoEvent(final TangoEvent event) {
        if (main.mTangoUx != null) {
            main.mTangoUx.updateTangoEvent(event);
        }
    }

    @Override
    public void onPoseAvailable(TangoPoseData pose) {
        if (main.mTangoUx != null) {
            main.mTangoUx.updatePoseStatus(pose.statusCode);
        }

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
                main.mIsRelocalized = pose.statusCode == TangoPoseData.POSE_VALID;
                main.mRenderer.isRelocated = main.mIsRelocalized;
                if (main.mIsRelocalized) {

                    main.FRAME_PAIR = new TangoCoordinateFramePair(
                            TangoPoseData.COORDINATE_FRAME_AREA_DESCRIPTION,
                            TangoPoseData.COORDINATE_FRAME_DEVICE);



                }


            }

        }
    }

    @Override
    public void onFrameAvailable(int cameraId) {
        // Check if the frame available is for the camera we want and update its frame
        // on the view.
        if (cameraId == TangoCameraIntrinsics.TANGO_CAMERA_COLOR) {
            // Mark a camera frame is available for rendering in the OpenGL thread
            main.mIsFrameAvailableTangoThread.set(true);
            // Trigger an Rajawali render to update the scene with the new RGB data.
            main.mSurfaceView.requestRender();
        }
    }

}

