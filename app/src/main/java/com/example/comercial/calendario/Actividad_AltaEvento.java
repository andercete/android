package com.example.comercial.calendario;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comercial.BBDD.DbHelper;
import com.example.comercial.BBDD.Evento;
import com.example.comercial.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Actividad_AltaEvento extends AppCompatActivity {

    private EditText editTextTitle, editTextLocation, editTextDate, editTextTime, editTextDescription;
    private Button buttonGuardar, buttonLimpiar;
    private DbHelper dbHelper;
    private Calendar calendar;
    private SimpleDateFormat timeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altaevento);

        dbHelper = new DbHelper(this);

        editTextTitle = findViewById(R.id.tituloEditText);
        editTextLocation = findViewById(R.id.ubicacionEditText);
        editTextTime = findViewById(R.id.horaEditText2);
        editTextDescription = findViewById(R.id.descripcionEditText);

        editTextDate = findViewById(R.id.editTextDate2);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        buttonGuardar = findViewById(R.id.guardarButton);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEvento();
            }
        });

        buttonLimpiar = findViewById(R.id.button3);
        buttonLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarControles();
            }
        });

        timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

        calendar = Calendar.getInstance();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateEditTextDate();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        updateEditTextTime();
                    }
                },
                hour,
                minute,
                true);

        timePickerDialog.show();
    }

    private void updateEditTextTime() {
        editTextTime.setText(timeFormat.format(calendar.getTime()));
    }

    private void updateEditTextDate() {
        String myFormat = "yyyy-MM-dd"; // Cambia el formato de la fecha según tus necesidades
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextDate.setText(sdf.format(calendar.getTime()));
    }

    private void guardarEvento() {
        String title = editTextTitle.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (!title.isEmpty() && !date.isEmpty() && isValidTime(time) && !description.isEmpty()) {
            Evento evento = new Evento(0, title, location, date, time, description);
            dbHelper.addEvento(evento);

            // Envía un resultado a MainActivity
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Rellene todos los campos correctamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidTime(String time) {
        try {
            timeFormat.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void limpiarControles() {
        editTextTitle.setText("");
        editTextLocation.setText("");
        editTextTime.setText("");
        editTextDate.setText("");
        editTextDescription.setText("");
    }
}
