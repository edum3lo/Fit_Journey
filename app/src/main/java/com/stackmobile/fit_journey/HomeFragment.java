package com.stackmobile.fit_journey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.stackmobile.fit_journey.database.entities.User;
import com.stackmobile.fit_journey.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private User usuario;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        usuario = getArguments().getParcelable("usuario");

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = new Bundle();
        bundle.putParcelable("usuario", usuario);

        binding.textRegistro.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_to_registro, bundle)
        );
        binding.imageRegistro.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_to_registro, bundle)
        );
        binding.textLembrete.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_to_lembretes, bundle)
        );
        binding.imageLembrete.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_to_lembretes, bundle)
        );
        binding.textFotos.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_to_fotos, bundle)
        );
        binding.imageFotos.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_to_fotos, bundle)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
