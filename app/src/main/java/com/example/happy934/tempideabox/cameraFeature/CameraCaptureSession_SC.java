package com.example.happy934.tempideabox.cameraFeature;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraCaptureSession;
import android.util.Log;

/**
 * Created by happy on 23/2/18.
 */

@TargetApi(23)
public class CameraCaptureSession_SC extends CameraCaptureSession.StateCallback{

    CameraCaptureSession cameraCaptureSession = null;

    @Override
    public void onConfigured(CameraCaptureSession cameraCaptureSession){
        this.cameraCaptureSession = cameraCaptureSession;
        Log.d("Session_msg","Configured");
    }

    @Override
    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession){
        this.cameraCaptureSession = cameraCaptureSession;
        Log.d("Session_msg","Not Configured");
    }
}
