package com.uwaterloo.smartpantry.ui.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;

import com.android.volley.error.VolleyError;
import com.couchbase.lite.internal.utils.StringUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.datalink.DataLinkREST;
import com.uwaterloo.smartpantry.datalink.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment implements VolleyResponseListener {
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private ImageCapture imageCapture = null;
    private Button btnBack;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
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
                // Scale the target resolution for smaller image size when doing the upload
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder()
                        .setTargetResolution(new Size(768, 1024))
                        .build();

                imageCapture =
                        new ImageCapture.Builder()
                                .setTargetRotation(getView().getDisplay().getRotation())
                                .setTargetResolution(new Size(768, 1024))
                                .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                cameraProvider.unbindAll();
                Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture);

                PreviewView previewView = (PreviewView) getView().findViewById(R.id.viewFinder);
                ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        float zoomRatio = camera.getCameraInfo().getZoomState().getValue().getZoomRatio();
                        float scale = zoomRatio * detector.getScaleFactor();
                        camera.getCameraControl().setZoomRatio(scale);
                        return true;
                    }
                });
                previewView.setOnTouchListener((a, event) -> {
                    scaleGestureDetector.onTouchEvent(event);
                    return true;
                });

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
                DataLinkREST.GetPrediction(file.getAbsolutePath(), CameraFragment.this);
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
        View v = inflater.inflate(R.layout.fragment_camera, container, false);

        Button cameraButton = (Button) v.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(this::onClick);

        btnBack = v.findViewById(R.id.back_btn);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
            }
        });

        return v;
    }

    @Override
    public void onSuccess(JSONArray response, String type) {
        System.out.println(response);
    }

    @Override
    public void onSuccess(JSONObject response, String type) {
        System.out.println(response);
        switch (type) {
            case "Predict":
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray prediction = result.getJSONArray("prediction");
                    ArrayList<String> ingredients = new ArrayList<String>();
                    for (int i = 0; i < prediction.length(); i++) {
                        if (!StringUtils.isEmpty(prediction.getString(i)) && !prediction.getString(i).equals("can not recognize item")) {
                            ingredients.add(prediction.getString(i));
                        }
                    }
                    ingredients.add("No Matches");
                    Bundle bundle = new Bundle();

                    bundle.putStringArrayList("Ingredients", ingredients);
                    getParentFragmentManager().setFragmentResult("requestKey", bundle);
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        Log.v("Error", error.getMessage());
    }
}