package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.Manifest;
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
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.models.CameraSettings;
import com.fadetoproductions.rvkn.clothesconsensus.utils.PhotoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// Guide used http://inducesmile.com/android/android-camera2-api-example-tutorial/

public class CameraActivity extends BaseActivity {

    int RESULT_SELECT_IMAGE_FROM_GALLERY = 1098;
    int LOOK_CONFIRMATION_COMPLETED = 1097;
    int LOOK_POSTED_CODE = 1096;


    @BindView(R.id.ibTakePhoto) ImageButton ibTakePhoto;
    @BindView(R.id.textureViewCamera) TextureView textureView;
    @BindView(R.id.ibFlash) ImageButton ibFlash;
    @BindView(R.id.ibFlipCamera) ImageButton ibFlipCamera;
    @BindView(R.id.ibGallery) ImageButton ibGallery;
    @BindView(R.id.ibBack) ImageButton ibBack;

    final private String TAG = "CameraActivity";
    private String cameraId;
    private String[] cameraIds;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraDevice.StateCallback stateCallback;
    private CameraCaptureSession.CaptureCallback captureCallbackListener;
    private TextureView.SurfaceTextureListener textureListener;
    private CameraSettings cameraSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        setupListenersAndCallbacks();
        cameraSettings = CameraSettings.getCameraSettings(this);
        renderBasedOnCameraSettings();

        // Need to do this after the layout is created. TODO can this be put in a lifecycle?
        textureView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textureView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                textureView.getLayoutParams().height = textureView.getWidth();
            }
        });

        setOnTouchListenerOnImageButton(ibTakePhoto);
        setOnTouchListenerOnImageButton(ibFlash);
        setOnTouchListenerOnImageButton(ibFlipCamera);
        setOnTouchListenerOnImageButton(ibGallery);
        setOnTouchListenerOnImageButton(ibBack);
    }

    public void setupListenersAndCallbacks() {
        textureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                openCamera();
            }
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {}

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        };

        textureView.setSurfaceTextureListener(textureListener);

        stateCallback = new CameraDevice.StateCallback() {

            @Override
            public void onOpened(CameraDevice camera) {
                cameraDevice = camera;
                createCameraPreview();
            }

            @Override
            public void onDisconnected(CameraDevice cameraDevice) {
                cameraDevice.close();
            }

            @Override
            public void onError(CameraDevice cameraDevice, int i) {
                cameraDevice.close();
            }
        };

        captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                createCameraPreview();
            }
        };
    }

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ibTakePhoto)
    protected void takePicture(View view) {
        if(null == cameraDevice) {
            Log.v("CameraActivity", "There is no camera");
            return;
        }

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            int width = 640;
            int height = 480;

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);



            final File file = PhotoUtils.newEmptyFile(this);
            Log.v("CameraActivity", "File created");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }
                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                        Log.v("CameraActivity", "Output is written");

                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    closeCamera();
                    loadLookConfirmation();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraIds = manager.getCameraIdList();
            if (cameraId == null) {
                cameraId = cameraIds[0];
            }
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }
    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.back();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CameraActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private void renderBasedOnCameraSettings() {
        Boolean flashOn = cameraSettings.getFlashOn();
        if(flashOn){
            turnOnFlashLight();
        } else {
            turnOffFlashLight();
        }
        ibFlash.setImageResource(flashOn ? R.drawable.flash_on : R.drawable.flash_off);

    }
    public void turnOffFlashLight()
    {
        try
        {
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void turnOnFlashLight()
    {
        try
        {
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @OnClick(R.id.ibFlash)
    public void switchFlash(View view) {
        cameraSettings.toggleFlash();
        CameraSettings.setCameraSettings(this, cameraSettings);
        renderBasedOnCameraSettings();
    }

    @OnClick(R.id.ibFlipCamera)
    public void flipCamera(View view) {
        cameraSettings.toggleCamera();
        if (cameraIds.length > 1) {
            closeCamera();
            cameraId = cameraSettings.getUseBackCamera() ? cameraIds[0] : cameraIds[1];
            openCamera();
        }
    }

    @OnClick(R.id.ibGallery)
    public void loadGallery(View view) {
        // TODO this doesn't do anything with the gallery item received yet
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
        startActivityForResult(intent, RESULT_SELECT_IMAGE_FROM_GALLERY);
        overridePendingTransition(R.anim.slide_up_2, R.anim.no_change);
    }


    public void loadLookConfirmation() {
        Intent data = new Intent();
        data.putExtra("code", 1034);
        setResult(RESULT_OK, data);
        Intent i = new Intent(CameraActivity.this, LookConfirmationActivity.class);
        Log.v("action", "Starting look confirmation screen");
        startActivityForResult(i, LOOK_CONFIRMATION_COMPLETED);
        overridePendingTransition(R.anim.slide_in_right, R.anim.no_change);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SELECT_IMAGE_FROM_GALLERY) {
            overridePendingTransition(R.anim.no_change, R.anim.slide_down_2);
        }

        if (requestCode == LOOK_CONFIRMATION_COMPLETED) {
            if (resultCode == RESULT_OK) {
                overridePendingTransition(R.anim.no_change, R.anim.slide_down_2);
                setResult(LOOK_POSTED_CODE, data);
                this.finish();
            } else {
                overridePendingTransition(R.anim.no_change, R.anim.slide_out_right);
            }
        }
    }
}
