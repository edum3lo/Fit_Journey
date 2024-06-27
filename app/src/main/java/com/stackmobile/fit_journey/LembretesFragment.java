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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class LembretesFragment extends Fragment {

    private static final String TAG = "LembretesFragment";
    private TimePicker timePicker;
    private DatePicker datePicker;
    private EditText editTextDescription;
    private Button buttonSchedule;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lembretes, container, false);

        timePicker = view.findViewById(R.id.timePicker);
        datePicker = view.findViewById(R.id.datePicker);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        buttonSchedule = view.findViewById(R.id.buttonSchedule);

        // Configurar o TimePicker para exibir em formato 24 horas
        timePicker.setIs24HourView(true);

        buttonSchedule.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    private void scheduleReminder() {
        String description = editTextDescription.getText().toString();
        if (description.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, insira uma descrição para o lembrete", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            Toast.makeText(getActivity(), "Selecione uma data e hora futuras", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), ReminderReceiver.class);
        intent.putExtra("description", description);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(getActivity(), "Lembrete agendado!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Lembrete agendado para: " + calendar.getTime().toString());
    }
}
