package com.stackmobile.fit_journey;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stackmobile.fit_journey.database.LocalDatabase;
import com.stackmobile.fit_journey.database.entities.Image;
import com.stackmobile.fit_journey.database.entities.User;
import com.stackmobile.fit_journey.databinding.FragmentFotosBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FotosFragment extends Fragment {

    private FragmentFotosBinding binding;
    private LocalDatabase localDatabase;
    private ImageAdapter imageAdapter;
    private List<Image> imageList = new ArrayList<>();
    private User usuario;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private String currentPhotoPath;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFotosBinding.inflate(inflater, container, false);

        localDatabase = LocalDatabase.getDatabase(requireContext());

        usuario = getArguments().getParcelable("usuario");

        initializePermissionLauncher();
        initializeTakePictureLauncher();

        if (usuario != null)
            imageList = localDatabase.imageModel().getImagesByUserId(usuario.getId());

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> imagePathList = new ArrayList<>();
        for (Image image : imageList) {
            imagePathList.add(image.getImagePath());
        }

        imageAdapter = new ImageAdapter(requireContext(), imagePathList);

        RecyclerView recyclerView = binding.recyclerGridViewPhotos;
        recyclerView.setAdapter(imageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);
        binding.recyclerGridViewPhotos.setLayoutManager(layoutManager);
        binding.recyclerGridViewPhotos.addItemDecoration(new GridSpacingItemDecoration(3, 10, true));

        binding.buttonTakePhoto.setOnClickListener(v -> requestPermissionLauncher.launch(new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }));
    }

    private void initializePermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
            boolean allGranted = true;
            for (Boolean granted : permissions.values()) {
                allGranted &= granted;
            }
            if (allGranted) {
                try {
                    File photoFile = createImageFile();
                    Uri photoURI = FileProvider.getUriForFile(getContext(), "com.stackmobile.fit_journey.fileprovider", photoFile);
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    takePictureLauncher.launch(takePictureIntent);
                } catch (IOException ex) {
                    Toast.makeText(getContext(), "Erro ao criar arquivo de imagem!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Permissões negadas!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeTakePictureLauncher() {
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                saveImagePathToDatabase(currentPhotoPath);
            } else {
                Toast.makeText(getContext(), "Foto não tirada!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void saveImagePathToDatabase(String imagePath) {
        if (usuario != null) {
            Image newImage = new Image();
            newImage.setUserId(usuario.getId());
            newImage.setImagePath(imagePath);

            localDatabase.imageModel().insert(newImage);

            Toast.makeText(getContext(), "Imagem salva com sucesso!", Toast.LENGTH_SHORT).show();

            imageAdapter.addImagePath(imagePath);
            imageAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
