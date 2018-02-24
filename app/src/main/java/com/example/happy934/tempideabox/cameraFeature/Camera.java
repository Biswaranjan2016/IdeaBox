package com.example.happy934.tempideabox.cameraFeature;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.happy934.tempideabox.R;

import java.util.ArrayList;
import java.util.List;

@TargetApi(23)
public class Camera extends AppCompatActivity {

    String cameraIdLists[] = null;
    private boolean consent = false;
    final int REQUEST_CODE = 88;
    static SurfaceView surfaceView = null;
    static CameraCharacteristics cameraCharacteristics = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // The default android code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        surfaceView = findViewById(R.id.surfaceView);


        // Obtain the instance of CameraManager to get the Caemera Ids, Camera Characteristics and o-
        // -oen the available camera devices
        CameraManager cameraManager = getApplicationContext().getSystemService(CameraManager.class);


        // Now try and get the camera Ids. The method returns an array of Camera Ids. This may throw
        // CameraAccessException
        try{
            cameraIdLists = cameraManager.getCameraIdList();
        }catch(CameraAccessException cae){
            Log.d("Message","Camera access could not be obtained");
        }


        // If at-all any camera is found then query the number of cameras. For example the device on
        // which it is being tested has 2 cameras (This is experimental)
        if (cameraIdLists.length > 0){
            for (String id : cameraIdLists){
                Log.d("Id : ",id);
            }
        }


        // Now call the method to query the camera characteristics of the specified camera id (This
        // is also experimental)
        this.cameraCharacteristics(cameraIdLists[1],cameraManager);
    }


    /*
    *  Method : cameraCharacteristics()
    *
    *  Parameters : String cameraId
    *               CameraManager cameraManager
    *
    *  ReturnType : void
    *
    *  Access : private
    *
    *  Description : This method is experimental and  presently it is used to obtain the
    *  cameraCharacteristics
    * */


    private void cameraCharacteristics(String cameraId,CameraManager managerInstance){



        // Obtain the CameraCharacteristics instance by the getCameraCharacteristics(). This may throw
        // CameraAccessException
        try{
            cameraCharacteristics = managerInstance.getCameraCharacteristics(cameraId);
        }catch(CameraAccessException cae){
            Log.d("Message","Camera access could not be obtained");
        }

        this.openCameraConnection(managerInstance,cameraId);
        // this.queryAvialableModes(cameraCharacteristics,cameraId);

    }

    /*
    *  Method : queryAvailableModes()
    *
    *  Parameters : CameraCharacteristics cameraCharacteristics
    *               String CameraId
    *
    *  ReturnType : void
    *
    *  Access : private
    *
    *  Description : This method is experimental and can be programmed to get the information about
*                       the supported features of the camera specified by the camera Id.
    * */
    private void queryAvialableModes(CameraCharacteristics cameraCharacteristics, String cameraId){

        // First characteristics to be queried is abberation mode
        int arr[] = cameraCharacteristics.get(CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES);
        Log.d("Length of array Abb",Integer.toString(arr.length));
        for (int x : arr){
            Log.d("Camera Characteristics",Integer.toString(x));
        }

        // Second characteristics to be queried is WhiteBalance Mode
        arr = cameraCharacteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
        Log.d("Length of array in AWB",Integer.toString(arr.length));
        for (int x : arr){
            Log.d("AWB",Integer.toString(x));
        }

        // Third characteristics to be queried is Effects
        arr = cameraCharacteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS);
        Log.d("Length of array in Efct",Integer.toString(arr.length));
        for (int x : arr){
            Log.d("Effect",Integer.toString(x));
        }

        // Likewise you can query the characteristics you want. There are many.
    }

    /*
    *  Method : openCameraConnection()
    *
    *  Parameters : CameraManager cameraManager
    *               String CameraId
    *
    *  ReturnType : void
    *
    *  Access : private
    *
    *  Description : This method is for production and is used to open camera connection
    * */
    private void openCameraConnection(CameraManager cameraManager,String cameraId){

        // Try to open camera connection upon getting the permission. This may get the
        // CameraAccessException
        try{
            if (this.getConsent()){
                cameraManager.openCamera(cameraId,new CameraDevice_SC(),null);
            }
        }catch (CameraAccessException cae){
            Log.d("Exception","Access could not be obtained");
        }

        Capture capture = new Capture();

//        if (CameraDevice_SC.cameraDevice != null){
//            capture.setSurfaceAndStartSession();
//        }else {
//            Log.d("Message","Instance Not Prepared");
//        }
    }

    /*
    *  Method : getConsent()
    *
    *  Parameters : void
    *
    *  ReturnType : boolean
    *
    *  Access : private
    *
    *  Description : This method is for production and is used to get the consent
    * */
    private boolean getConsent(){
        this.checkPermission();
        return this.consent;
    }

    /*
    *  Method : checkPermission()
    *
    *  Parameters : void
    *
    *  ReturnType : void
    *
    *  Access : private
    *
    *  Description : This method checks for the permission if permission is not granted then
    *                   explicit permission is sought.
    * */
    private void checkPermission(){

        //Check first if the permission is obtained or not. If
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            Log.d("Permission status","No permission");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_CODE);
        }
        // else
        else {
            Log.d("Permission status","granted");
            this.consent =  true;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String Permission[],int grantResults[]){
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission Granted
                    this.consent = true;
                }
        }
    }


}
@TargetApi(23)
class Capture{

}