package com.example.happy934.tempideabox.cameraFeature;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by happy on 22/2/18.
 */

@TargetApi(23)
public class CameraDevice_SC extends CameraDevice.StateCallback implements SurfaceHolder.Callback{

    static CameraDevice cameraDevice;
    public void onclosed(){

    }

    public void onDisconnected(CameraDevice cameraDevice){
        Log.d("Message","In disconnected()");
    }

    public void onError(CameraDevice cameraDevice, int error){
        String message = Integer.toString(error);
        Log.d("Message","Error code "+message);
    }

    public void onOpened(CameraDevice cameraDeviceArg){
//        cameraDevice = cameraDeviceArg;
        SurfaceHolder      surfaceHolder = Camera.surfaceView.getHolder();
        List<Surface> lSurface = new ArrayList<>();
        Surface surface = surfaceHolder.getSurface();
        if (surface == null){
            Log.d("Surface","Null");
        }else {
            Log.d("Surface","Not null");
        }
        lSurface.add(surface);


        try{
            cameraDeviceArg.createCaptureSession(lSurface,new CameraCaptureSession_SC(),null);
        }catch (CameraAccessException cae){
            Log.d("Message","Camera could not be accessed");
        }catch (NullPointerException ne){
            Log.d("Message","NPE is caught");
            if (cameraDevice == null){
                Log.d("Message","Camera device instance is null");
            }
        }
        Log.d("Message","In opened()");
//        if (cameraDevice == null){
//            Log.d("Message","Null");
//        }else {
//            Log.d("Message","Not null");
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder){
        Log.d("Message","Surface created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder){
        Log.d("Message","Surface destroyed");
    }

}
