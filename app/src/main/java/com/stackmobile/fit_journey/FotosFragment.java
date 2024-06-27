package com.stackmobile.fit_journey;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FotosFragment extends Fragment {

    private static final String TAG = "FotosFragment";
    private Button buttonTakePhoto;
    private GridView gridViewGallery;
    private TextView textViewNoPhotos;
    private ArrayList<String> imagePathList;
    private ImageAdapter imageAdapter;
    private String currentPhotoPath;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fotos, container, false);

        buttonTakePhoto = view.findViewById(R.id.buttonTakePhoto);
        gridViewGallery = view.findViewById(R.id.gridViewPhotos);
        textViewNoPhotos = view.findViewById(R.id.textFotos);
        imagePathList = new ArrayList<>();
        imageAdapter = new ImageAdapter(getContext(), imagePathList);
        gridViewGallery.setAdapter(imageAdapter);

        initializePermissionLauncher();
        initializeTakePictureLauncher();

        buttonTakePhoto.setOnClickListener(v -> requestPermissionLauncher.launch(new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }));

        loadImagesFromGallery();

        return view;
    }

    private void initializePermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
            boolean allGranted = true;
            for (Boolean granted : permissions.values()) {
                allGranted &= granted;
            }
            if (allGranted) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getContext(), "Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeTakePictureLauncher() {
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                galleryAddPic();
                loadImagesFromGallery();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(), "com.stackmobile.fitjourney.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureLauncher.launch(takePictureIntent);
            }
        } catch (IOException ex) {
            Log.e(TAG, "Error occurred while creating the file", ex);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(currentPhotoPath));
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }

    private void loadImagesFromGallery() {
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] imageFiles = storageDir.listFiles();
        imagePathList.clear();

        if (imageFiles != null && imageFiles.length > 0) {
            textViewNoPhotos.setVisibility(View.GONE);
            gridViewGallery.setVisibility(View.VISIBLE);
            for (File imageFile : imageFiles) {
                imagePathList.add(imageFile.getAbsolutePath());
            }
        } else {
            textViewNoPhotos.setVisibility(View.VISIBLE);
            gridViewGallery.setVisibility(View.GONE);
        }

        imageAdapter.notifyDataSetChanged();
    }
}
