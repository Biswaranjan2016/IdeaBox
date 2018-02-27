package com.example.happy934.tempideabox.cameraFeature2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.example.happy934.tempideabox.R;

import java.io.IOException;

public class Camera extends AppCompatActivity {

    private android.hardware.Camera camera;
    private CameraPreview mCameraPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera3);

        camera = getCameraInstance();

        mCameraPreview = new CameraPreview(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mCameraPreview);
    }

    private boolean checkCameraHardware(Context context){
        return (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA));
    }
    private static android.hardware.Camera getCameraInstance(){
        android.hardware.Camera camera = null;
        try {
            camera = android.hardware.Camera.open();
        }catch (Exception e){

        }
        return camera;
    }

}
class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mHolder;
    private android.hardware.Camera mCamera;

    public CameraPreview(Context context, android.hardware.Camera camera){
        super(context);
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);

        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder){
        //Now tell the camera where to draw the preview
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }catch (IOException ie){
            Log.d("Error setting preview",ie.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder){
        //Empty, release the preview
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int height, int width){
        if (mHolder.getSurface() == null){
            return;
        }

        //Stop preview before making changes
        try{
            mCamera.stopPreview();
        }catch (Exception e){
            //Tried to stop a non existent preview
        }

        //Start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }catch (Exception e){
            //Error starting camera preview
        }
    }
}
