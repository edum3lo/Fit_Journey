package com.stackmobile.fit_journey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.stackmobile.fit_journey.database.LocalDatabase;
import com.stackmobile.fit_journey.database.entities.ActivityLog;
import com.stackmobile.fit_journey.database.entities.User;
import com.stackmobile.fit_journey.databinding.FragmentRegistroBinding;

public class RegistroFragment extends Fragment {

    private FragmentRegistroBinding binding;
    private LocalDatabase localDatabase;
    private User usuario;

    private String selectedActivity;
    private String selectedWater;
    private String selectedSleep;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentRegistroBinding.inflate(inflater, container, false);

        localDatabase = LocalDatabase.getDatabase(requireContext());

        usuario = getArguments().getParcelable("usuario");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupSpinners();

        binding.buttonSave.setOnClickListener(v -> {
            String meals = binding.editTextMeals.getText().toString();

            if (selectedActivity.isEmpty() || selectedWater.isEmpty() || selectedSleep.isEmpty() || meals.isEmpty()) {
                Toast.makeText(getActivity(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                ActivityLog activityLog = new ActivityLog();
                activityLog.setMinutesOfActivity(selectedActivity);
                activityLog.setWaterConsumption(selectedWater);
                activityLog.setSleepTimeInterval(selectedSleep);
                activityLog.setMealDescription(meals);
                activityLog.setUserId(usuario.getId());

                localDatabase.activityLogModel().insert(activityLog);

                Toast.makeText(getActivity(), "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();

                binding.spinnerActivity.setSelection(0);
                binding.spinnerWater.setSelection(0);
                binding.spinnerSleep.setSelection(0);
                binding.editTextMeals.setText("");
            }
        });
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> adapterActivity = ArrayAdapter.createFromResource(getActivity(),
                R.array.activity_levels, android.R.layout.simple_spinner_item);
        adapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerActivity.setAdapter(adapterActivity);
        binding.spinnerActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedActivity = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedActivity = "";
            }
        });

        ArrayAdapter<CharSequence> adapterWater = ArrayAdapter.createFromResource(getActivity(),
                R.array.water_levels, android.R.layout.simple_spinner_item);
        adapterWater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerWater.setAdapter(adapterWater);
        binding.spinnerWater.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWater = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedWater = "";
            }
        });

        ArrayAdapter<CharSequence> adapterSleep = ArrayAdapter.createFromResource(getActivity(),
                R.array.sleep_levels, android.R.layout.simple_spinner_item);
        adapterSleep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSleep.setAdapter(adapterSleep);
        binding.spinnerSleep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSleep = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSleep = "";
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
