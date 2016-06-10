package com.projecttango.roomerapp.renderer;

import android.util.Log;

import com.google.atap.tangoservice.TangoCameraIntrinsics;
import com.google.atap.tangoservice.TangoPoseData;
import com.projecttango.roomerapp.RoomerMainActivity;

import org.rajawali3d.scene.ASceneFrameCallback;

/**
 * Created by Marcus Bätz on 09.06.2016.
 */
public class RoomerSceneFrameCallBack extends ASceneFrameCallback {
    private final RoomerMainActivity main;
    public RoomerSceneFrameCallBack(RoomerMainActivity main) {
        this.main = main;
    }

    @Override
        public void onPreFrame(long sceneTime, double deltaTime) {
            // NOTE: This is called from the OpenGL render thread, after all the renderer
            // onRender callbacks had a chance to run and before scene objects are rendered
            // into the scene.

            // Prevent concurrent access to {@code mIsFrameAvailableTangoThread} from the Tango
            // callback thread and service disconnection from an onPause event.
            synchronized (main) {
                // Don't execute any tango API actions if we're not connected to the service
                if (main.mIsConnected.get() == false) {
                    return;
                }


                // Set-up scene camera projection to match RGB camera intrinsics
                if (!main.mRenderer.isSceneCameraConfigured()) {
                    main.mRenderer.setProjectionMatrix(main.mIntrinsics);
                }

                // Connect the camera texture to the OpenGL Texture if necessary
                // NOTE: When the OpenGL context is recycled, Rajawali may re-generate the
                // texture with a different ID.
                if (main.mConnectedTextureIdGlThread != main.mRenderer.getTextureId()) {
                    main.mTango.connectTextureId(TangoCameraIntrinsics.TANGO_CAMERA_COLOR,
                            main.mRenderer.getTextureId());
                    main.mConnectedTextureIdGlThread = main.mRenderer.getTextureId();
                    Log.d(main.TAG, "connected to texture id: " + main.mRenderer.getTextureId());
                }

                // If there is a new RGB camera frame available, update the texture with it
                if (main.mIsFrameAvailableTangoThread.compareAndSet(true, false)) {
                    main.mRgbTimestampGlThread =
                            main.mTango.updateTexture(TangoCameraIntrinsics.TANGO_CAMERA_COLOR);
                }

                // If a new RGB frame has been rendered, update the camera pose to match.
                if (main.mRgbTimestampGlThread > main.mCameraPoseTimestamp) {
                    // Calculate the device pose at the camera frame update time.
                    TangoPoseData lastFramePose = main.mTango.getPoseAtTime(main.mRgbTimestampGlThread,
                            main.FRAME_PAIR);
                    if (lastFramePose.statusCode == TangoPoseData.POSE_VALID) {
                        // Update the camera pose from the renderer
                        main.mRenderer.updateRenderCameraPose(lastFramePose, main.mExtrinsics);
                        main.mCameraPoseTimestamp = lastFramePose.timestamp;
                    } else {
                        Log.w(main.TAG, "Can't get device pose at time: " + main.mRgbTimestampGlThread);
                    }
                }
            }
        }

        @Override
        public void onPreDraw(long sceneTime, double deltaTime) {

        }

        @Override
        public void onPostFrame(long sceneTime, double deltaTime) {

        }

        @Override
        public boolean callPreFrame() {
            return true;
        }

}
