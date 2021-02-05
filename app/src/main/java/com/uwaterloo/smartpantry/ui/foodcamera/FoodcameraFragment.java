package com.uwaterloo.smartpantry.ui.foodcamera;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.uwaterloo.smartpantry.MainActivity;
import com.uwaterloo.smartpantry.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.uwaterloo.smartpantry.ui.foodcamera.FoodcameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodcameraFragment extends Fragment {
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private ImageCapture imageCapture = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public FoodcameraFragment() {
        // Required empty public constructor
    }
/**
* Use this factory method to create a new instance of
* this fragment using the provided parameters.
*
* @param param1 Parameter 1.
* @param param2 Parameter 2.
* @return A new instance of fragment FoodcameraFragment.
*/
    // TODO: Rename and change types and number of parameters
    public static com.uwaterloo.smartpantry.ui.foodcamera.FoodcameraFragment newInstance(String param1, String param2) {
        com.uwaterloo.smartpantry.ui.foodcamera.FoodcameraFragment fragment = new com.uwaterloo.smartpantry.ui.foodcamera.FoodcameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (permissionsGranted()) {
            startCamera();
        } else {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
        }
    }

    // https://developer.android.com/training/camerax/architecture
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();

                imageCapture =
                        new ImageCapture.Builder()
                                .setTargetRotation(getView().getDisplay().getRotation())
                                .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture);

                PreviewView previewView = getView().findViewById(R.id.viewFinder);
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
            } catch(ExecutionException | InterruptedException e) {
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    public void onClick(View v) {
        if (imageCapture == null) {
            return;
        }

        File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Image_" +System.currentTimeMillis() + ".jpg");

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(file).build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(getContext()), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                Uri savedUri = Uri.fromFile(file);
                String msg = "Photo saved: " + savedUri;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
            // TODO: Error Handling
            @Override
            public void onError(ImageCaptureException error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if(permissionsGranted()) {
                startCamera();
            } else {
                // TODO: Update toast message
                Toast.makeText(getContext(), "Camera permissions not granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Only camera permission
    private boolean permissionsGranted() {
        return ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foodcamera, container, false);
        // Can not initialize in onCreate as view is not yet created
        Button cameraButton = (Button) view.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(this::onClick);
        return view;
    }
}
