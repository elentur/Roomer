package com.projecttango.roomerapp.tango;

import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoPoseData;
import com.projecttango.rajawali.DeviceExtrinsics;

/**
 * Created by Marcus Bätz on 09.06.2016.
 * The Camera Extrinsics for the different devices and camera for later transformations
 */
public class RoomerDeviceExtrinsics extends DeviceExtrinsics{
    final static  TangoCoordinateFramePair colorFramePair = new TangoCoordinateFramePair(
            TangoPoseData.COORDINATE_FRAME_IMU,TangoPoseData.COORDINATE_FRAME_CAMERA_COLOR);
    final static  TangoCoordinateFramePair deviceFramePair = new TangoCoordinateFramePair(
            TangoPoseData.COORDINATE_FRAME_IMU,TangoPoseData.COORDINATE_FRAME_DEVICE);
    final static  TangoCoordinateFramePair depthFramePair = new TangoCoordinateFramePair(
            TangoPoseData.COORDINATE_FRAME_IMU,TangoPoseData.COORDINATE_FRAME_CAMERA_DEPTH);

    /**
     * Calculates and stores the fixed transformations between the device and
     * the various sensors to be used later for transformations between frames.
     * @param tango from Roomer Device Extrinsic
     */
    public RoomerDeviceExtrinsics(Tango tango){
        super(tango.getPoseAtTime(0.0, deviceFramePair),
                tango.getPoseAtTime(0.0,colorFramePair),
                tango.getPoseAtTime(0.0,depthFramePair));
    }
}
