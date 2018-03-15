package com.example.happy934.tempideabox.camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.happy934.tempideabox.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@TargetApi(23)
public class Cam extends AppCompatActivity{

    private static final String Tag = "xxxxxxxxxxxxxxxxxxx";
    int fileCounter = 1;
    static List<File> photoList = null;
    private Button capture;
    private TextureView textureView;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }

    //Stores the camera id returned from CameraManager instance
    private String cameraId;

    //An instance to a camera device.
    protected CameraDevice cameraDevice;

    //Configured session for a cameraDevice
    protected CameraCaptureSession cameraCaptureSession;

    //settings and outputs to capture a single image
    protected CaptureRequest captureRequest;

    //A builder for capture requests
    protected CaptureRequest.Builder captureRequestBuilder;


    private android.util.Size imageDimension;
    android.util.Size[] jpegSizes;


    private ImageReader imageReader;
    private File file;

    private static final int REQUEST_CMERA_PERMISSION = 200;
    private boolean flashSupported;
    private Handler backgroundHandler;
    private HandlerThread mBackgroundThread;

    RecyclerView recyclerView;
    ImageAdapter imageAdapter;

    /*
    * This is the activity lifecycle method.
    *
    * This is first called.
    * All the initializations should be done here
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        //Get the TextureView from the xml
        textureView = (TextureView)findViewById(R.id.textureView);
        assert textureView != null;

        //Set the listener to TextureView
        textureView.setSurfaceTextureListener(surfaceTextureListener);

        //Get the capture button from the xml
        capture = (Button) findViewById(R.id.btn_capture);
        assert capture != null;

        //Associate a listener to the button
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
//                Intent intent = new Intent(getApplicationContext(),ImageSelector.class);
//                startActivity(intent);
            }
        });

        photoList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imageAdapter = new ImageAdapter(photoList);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(imageAdapter);
    }

    /*
    * Create a SurfaceTextureListener
    * */
    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {

        /*
        * Open the camera whenever the SurfaceTexture is available
        * */
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    /*
    * This is a callback related to state of the CameraDevice
    * */
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.e(Tag,"onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice =null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {

        /*
        * Enable the preview again whenever the completion of the Capture is detected
        * */
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(Cam.this,"Saved: "+file,Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }

    };

    protected void startBackgroundThread(){
        mBackgroundThread = new HandlerThread("Background Thread");
        mBackgroundThread.start();
        backgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread(){
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            backgroundHandler = null;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
    *   Initiates the procedure to take a picture
    *
    *
    * */
    protected void takePicture(){

        /*
        *
        * Checks whether the camera device is null or not
        *
        * */
        if (cameraDevice == null){
            Log.d(Tag,"Camera device is null");
            return;
        }

        /*
        * Obtain a camera manager instance to query the number of cameras, characteristics of the available cameras etc
        * */
        CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);

        try{

            /*
            * Based on the obtained CameraManager instance obtain the instance of CameraCharacteristics
            * */
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraDevice.getId());
            jpegSizes = null;

            /*
            * Checks whether the obtained cameraCharacteristics instance is null or not
            * */
            if (cameraCharacteristics != null){
                //Retrieve the jpeg size
                jpegSizes = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getInputSizes(ImageFormat.JPEG);
            }

            //Set a default width and height
            int width = 640;
            int height = 480;

            /*
            * Obtained Jpeg size should not be null and the array should contain at least one entry
            * */
            if (jpegSizes != null && jpegSizes.length > 1){
                //Retrieve the width from the jpegSizes array
                width = jpegSizes[0].getWidth();
                //Retrieve the height from the jpegSizes array
                height = jpegSizes[0].getHeight();
            }

            /*
            * @Class ImageReader: allows direct access to image data rendered to a surface
            *
            * @Description obtain the instance of ImageReader by providing width, height, format of the image and maximum images
            * */
            ImageReader imageReader = ImageReader.newInstance(width,height,ImageFormat.JPEG,1);

            //Create a list of output surfaces
            List<Surface> outputSurfaces = new ArrayList<>(2);


            outputSurfaces.add(imageReader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation));

            //Create a file to store the obtained image
            // final File file = new File(Environment.getExternalStorageDirectory()+"/pic.jpeg");

            /*
            * Set the listener for ImageReader to get the image based on the availability
            * */
            ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    file = createFile();
                    try{
                        image = imageReader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte bytes[] = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    }catch (Exception e){
                        Log.d(Tag,e.getMessage());
                    }finally {
                        if (image != null){
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException{
                    OutputStream output = null;
                    try{
                        output = new FileOutputStream(file);
                        output.write(bytes);
                        photoList.add(file);
                        imageAdapter.notifyDataSetChanged();
                    }finally {
                        if (output != null){
                            output.close();
                        }
                    }
                }
            };

            imageReader.setOnImageAvailableListener(onImageAvailableListener,backgroundHandler);
            final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(Cam.this,"Saved "+file,Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try{
                        cameraCaptureSession.capture(captureBuilder.build(),captureCallback,backgroundHandler);
                    }catch (CameraAccessException cae){
                        cae.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }

            },backgroundHandler);
        }catch (CameraAccessException cae){
            cae.printStackTrace();
        }
    }

    private File createFile(){
        fileCounter++;
        //Create a file to store the obtained image
        file = new File(Environment.getExternalStorageDirectory()+"/pic"+fileCounter+".jpeg");
        return file;
    }


    /*
    * Use this method to create the cameraPreview
    *
    *       obtain the SurfaceTexture through textureView
    *       Set the default buffer size
    *       Obtain the surface from SurfaceTexture instance
    *
    *       Set the CaptureRequestBuilder
    *
    * */
    protected void createCameraPreview(){


        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        assert surfaceTexture != null;

        surfaceTexture.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
        Surface surface = new Surface(surfaceTexture);

        try{

            // Set the captureRequestBuilder
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            // Add a target to the captureRequestBuilder by passing the surface
            captureRequestBuilder.addTarget(surface);

            //Create a captureSession on the cameraDevice Instance
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {

                // Callback is called when the capture session is configured
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession2) {

                    /*
                    * Return from method if the CameraDevice instance is null
                    * */
                    if (cameraDevice == null){
                        return;
                    }
                    /*
                    * Assign the cameraCaptureSession obtained, when the cameraCaptureSession is
                    * successfully configured
                    * */
                    cameraCaptureSession = cameraCaptureSession2;

                    //Now update the preview
                    updatePreview();
                }

                // Callback is called when the configuration is failed
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Log.d(Tag,"Configuration failed");
                }
            },null);
        }catch (CameraAccessException cae){
            Log.d(Tag, "CAE in crtPrv");
        }
    }


    /*
    * Use this method to
    *       Get the instance of CameraManager
    *       Get a cameraId out of available cameras
    *       Get CameraCharacteristics by passing the cameraId
    *       Get the imageDimension through the StreamConfigurationMap instance
    *       Check the permission
     *      Open the camera connection
    * */
    private void openCamera() {

        // Get the instance of CameraManager
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(Tag, "in camera open");

        try{
            // Get a cameraId out of available cameras
            cameraId = cameraManager.getCameraIdList()[0];
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(
                    cameraId
            );

            // Get the imageDimension through the StreamConfigurationMap instance
            StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
            );
            assert streamConfigurationMap != null;
            imageDimension = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];

            // Check the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA )
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ){

                ActivityCompat.requestPermissions(Cam.this,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CMERA_PERMISSION);
                return;
            }

            // Open the camera connection
            cameraManager.openCamera(cameraId,stateCallback,null);
        }catch (CameraAccessException cae){
            cae.printStackTrace();
        }
        Log.d(Tag,"Open camera x");
    }


    protected void updatePreview(){
        if (cameraDevice == null){
            Log.e(Tag,"Update preview err");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CameraMetadata.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),null,backgroundHandler);
        }catch (CameraAccessException cae){
            cae.printStackTrace();
        }
    }

    private void closeCamera() {
        if (cameraDevice != null){
            cameraDevice.close();
            cameraDevice = null;
        }if (imageReader != null){
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] Permission, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CMERA_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Sorry the fature is necessary", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /*
    * Activity lifecycle method basically comes at number 3 . After,
    *       onCreate
    *       onStart
    * */
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(Tag,"in onResume()");

        //Start the backGround Thread
        startBackgroundThread();

        //Open Camera if TextureView is available
        if (textureView.isAvailable()){
            openCamera();
        }else{
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    protected void onPause(){
        Log.d(Tag,"in onPause()");
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    public void redirectForConfirmation(){

    }

    public void viewAndAction(View view){

        if (photoList.size() > 0){
            Intent intent = new Intent(this, ImageSelector.class);
            startActivity(intent);
        }

    }


}

