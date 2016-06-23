package com.projecttango.roomerapp.tango;

import android.util.Log;

import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoAreaDescriptionMetaData;
import com.google.atap.tangoservice.TangoCameraIntrinsics;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoOutOfDateException;
import com.google.atap.tangoservice.TangoPoseData;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.renderer.RoomerSceneFrameCallBack;
import com.projecttango.roomerapp.ui.SetUpUI;

import java.util.ArrayList;

/**
 * Created by Marcus Bätz on 09.06.2016.
 */
public class ConnectRunnable implements Runnable{
    private final RoomerMainActivity main;
    private  String uuid;
    private  Tango tango;
    public ConnectRunnable( final RoomerMainActivity main){
        this.main =main;


    }
    // Pass in a Runnable to be called from UI thread when Tango is ready,
    // this Runnable will be running on a new thread.
    // When Tango is ready, we can call Tango functions safely here only
    // when there is no UI thread changes involved.
    @Override
    public void run() {
        try {
            this.tango=main.mTango;
            connectTango();
            setupRenderer();
        } catch (TangoOutOfDateException outDateEx) {
            if (main.mTangoUx != null) {
                main.mTangoUx.showTangoOutOfDate();
            }
        } catch (TangoErrorException e) {
            Log.e(main.TAG, main.getString(R.string.exception_tango_error), e);
        } catch (SecurityException e) {
            Log.e(main.TAG, main.getString(R.string.permission_motion_tracking), e);
        }
    }

    private  void connectTango() {
        // Create a new Tango Configuration and enable the RoomerMainActivity API
        TangoConfig config;
        config = tango.getConfig(TangoConfig.CONFIG_TYPE_DEFAULT);
        config.putBoolean(TangoConfig.KEY_BOOLEAN_MOTIONTRACKING, true);
        config.putBoolean(TangoConfig.KEY_BOOLEAN_COLORCAMERA, true);
        //config.putBoolean(TangoConfig.KEY_BOOLEAN_LEARNINGMODE, true);

        // Tango service should automatically attempt to recover when it enters an invalid state.
        config.putBoolean(TangoConfig.KEY_BOOLEAN_AUTORECOVERY, true);
        config.putBoolean(TangoConfig.KEY_BOOLEAN_DEPTH, true);
        config.putBoolean(
                TangoConfig.KEY_BOOLEAN_LOWLATENCYIMUINTEGRATION, true);


        //Set adf file
        uuid = tango.listAreaDescriptions().get(0);
        main.adf = new String(tango.loadAreaDescriptionMetaData(uuid).get(TangoAreaDescriptionMetaData.KEY_NAME));
        config.putString(TangoConfig.KEY_STRING_AREADESCRIPTION, uuid);
        tango.connect(config);

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

        tango.connectListener(framePairs, new RoomerTangoUpdateListener(main));
        main.mExtrinsics = new RoomerDeviceExtrinsics(tango);
        main.mIntrinsics = tango.getCameraIntrinsics(TangoCameraIntrinsics.TANGO_CAMERA_COLOR);

        //open Dialog for adf selection
        SetUpUI.getInstance(main).getDestinationDialog().show(main.getFragmentManager(), "Ziele", true);
    }

    /**
     * Connects the view and renderer to the color camara and callbacks.
     */
    private void setupRenderer() {
        // Register a Rajawali Scene Frame Callback to update the scene camera pose whenever a new
        // RGB frame is rendered.
        // (@see https://github.com/Rajawali/Rajawali/wiki/Scene-Frame-Callbacks)
        main.mRenderer.getCurrentScene().registerFrameCallback(new RoomerSceneFrameCallBack(main));
    }
}
