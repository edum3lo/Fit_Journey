package com.stackmobile.fit_journey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegistroFragment extends Fragment {

    private Spinner spinnerActivity, spinnerWater, spinnerSleep;
    private EditText editTextMeals;
    private Button buttonSave;
    private String selectedActivity;
    private String selectedWater;
    private String selectedSleep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        spinnerActivity = view.findViewById(R.id.spinnerActivity);
        spinnerWater = view.findViewById(R.id.spinnerWater);
        spinnerSleep = view.findViewById(R.id.spinnerSleep);
        editTextMeals = view.findViewById(R.id.editTextMeals);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Configurar os spinners
        setupSpinners();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String meals = editTextMeals.getText().toString();

                if (selectedActivity.isEmpty() || selectedWater.isEmpty() || selectedSleep.isEmpty() || meals.isEmpty()) {
                    Toast.makeText(getActivity(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Salvar os dados no banco de dados ou enviar para o servidor
                    Toast.makeText(getActivity(), "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void setupSpinners() {
        // Atividade Física
        ArrayAdapter<CharSequence> adapterActivity = ArrayAdapter.createFromResource(getActivity(),
                R.array.activity_levels, android.R.layout.simple_spinner_item);
        adapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(adapterActivity);
        spinnerActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedActivity = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedActivity = "";
            }
        });

        // Consumo de Água
        ArrayAdapter<CharSequence> adapterWater = ArrayAdapter.createFromResource(getActivity(),
                R.array.water_levels, android.R.layout.simple_spinner_item);
        adapterWater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWater.setAdapter(adapterWater);
        spinnerWater.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWater = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedWater = "";
            }
        });

        // Horas de Sono
        ArrayAdapter<CharSequence> adapterSleep = ArrayAdapter.createFromResource(getActivity(),
                R.array.sleep_levels, android.R.layout.simple_spinner_item);
        adapterSleep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSleep.setAdapter(adapterSleep);
        spinnerSleep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
}
