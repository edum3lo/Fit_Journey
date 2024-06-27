package com.stackmobile.fit_journey;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stackmobile.fit_journey.database.LocalDatabase;
import com.stackmobile.fit_journey.database.entities.Reminder;
import com.stackmobile.fit_journey.database.entities.User;
import com.stackmobile.fit_journey.databinding.FragmentLembretesBinding;

import java.util.Calendar;

public class LembretesFragment extends Fragment {

    private FragmentLembretesBinding binding;
    private LocalDatabase localDatabase;
    private User usuario;

    private static final String TAG = "LembretesFragment";

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentLembretesBinding.inflate(inflater, container, false);
        localDatabase = LocalDatabase.getDatabase(requireContext());
        usuario = getArguments().getParcelable("usuario");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.timePicker.setIs24HourView(true);

        binding.buttonSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    scheduleReminder();
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao agendar lembrete", e);
                    Toast.makeText(getActivity(), "Erro ao agendar lembrete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void scheduleReminder() {
        String description = binding.editTextDescription.getText().toString();
        if (description.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, insira uma descrição para o lembrete", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = binding.timePicker.getHour();
        int minute = binding.timePicker.getMinute();
        int day = binding.datePicker.getDayOfMonth();
        int month = binding.datePicker.getMonth();
        int year = binding.datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            Toast.makeText(getActivity(), "Selecione uma data e hora futuras", Toast.LENGTH_SHORT).show();
            return;
        }

        // Salvar o lembrete no banco de dados
        Reminder reminder = new Reminder();
        reminder.setUserId(usuario.getId());
        reminder.setDate(calendar.getTime());
        reminder.setHour(hour);
        reminder.setMinute(minute);

        localDatabase.reminderModel().insert(reminder);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), ReminderReceiver.class);
        intent.putExtra("description", description);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(getActivity(), "Lembrete agendado!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Lembrete agendado para: " + calendar.getTime());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
