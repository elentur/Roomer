package com.projecttango.roomerapp.Exceptions;

import android.util.Log;

import com.google.atap.tango.ux.UxExceptionEvent;
import com.google.atap.tango.ux.UxExceptionEventListener;

/**
 * Created by Marcus Bätz on 09.06.2016.
 * UX ExceptionListener with customized Log messages
 */
public class RoomerUxExceptionEventListener implements UxExceptionEventListener {
    private final String TAG;

    public RoomerUxExceptionEventListener(String tag) {
        this.TAG = tag;
    }

    /**
     * This Eventlistener for all Roomer UX Exceptions
     * @param uxExceptionEvent
     */
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
}
