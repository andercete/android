package com.example.comercial.calendario;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.example.comercial.Metodos;
import com.example.comercial.R;

import java.util.Calendar;

public class Actividad_AltaEvento extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altaevento);

        EditText tituloEditText = findViewById(R.id.tituloEditText);
        EditText fechaEditText = findViewById(R.id.editTextDate2);
        EditText ubicacionEditText = findViewById(R.id.ubicacionEditText);
        EditText descripcionEditText = findViewById(R.id.descripcionEditText);

        Button guardarButton = findViewById(R.id.guardarButton);
        Button limpiarButton = findViewById(R.id.button3);

        // En tu método onCreate
        fechaEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                // Crear un DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Actividad_AltaEvento.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Aquí se establece la fecha seleccionada en el EditText
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                fechaEditText.setText(selectedDate);
                            }
                        }, year, month, day);

                // Mostrar el DatePickerDialog
                datePickerDialog.show();
            }
        });

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = tituloEditText.getText().toString();
                String fecha = fechaEditText.getText().toString();
                String ubicacion = ubicacionEditText.getText().toString();
                String descripcion = descripcionEditText.getText().toString();

                if (!Metodos.isValidCampo(tituloEditText, "Titulo", Metodos.FieldType.STRING,Actividad_AltaEvento.this)) {
                    return;
                }
                if (!Metodos.isValidCampo(ubicacionEditText, "Ubicacion", Metodos.FieldType.STRING,Actividad_AltaEvento.this)) {
                    return;
                }
                if (tituloEditText.getText().toString().isEmpty()) {
                    Metodos.mostrarAlertaValidacion("Error","Debes de poner nombre al evento", tituloEditText, Actividad_AltaEvento.this);
                    return;
                } else if
                (fechaEditText.getText().toString().isEmpty()) {
                    Metodos.mostrarAlertaValidacion("Error","Debes Rellenar la fecha del evento", tituloEditText, Actividad_AltaEvento.this);
                    return;
                }
                // Enviar los datos del nuevo evento de vuelta a la actividad principal
                Intent resultadoIntent = new Intent();
                resultadoIntent.putExtra("titulo", titulo);
                resultadoIntent.putExtra("fecha", fecha);
                resultadoIntent.putExtra("ubicacion", ubicacion);
                resultadoIntent.putExtra("descripcion", descripcion);
                setResult(RESULT_OK, resultadoIntent);

                // Finalizar esta actividad y volver a la actividad principal
                finish();
            }
        });
        limpiarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tituloEditText.setText("");
                descripcionEditText.setText("");
                ubicacionEditText.setText("");
                fechaEditText.setText("");
            }
        });
    }
}