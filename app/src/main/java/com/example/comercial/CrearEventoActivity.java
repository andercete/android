package com.example.comercial;
// CrearEventoActivity.java
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CrearEventoActivity extends AppCompatActivity {

    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(CrearEventoActivity.this,
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


                if (tituloEditText.getText().toString().isEmpty()) {

                    mostrarError("Debes de poner nombre al evento", tituloEditText);
                    return;
                } else if

                (fechaEditText.getText().toString().isEmpty()) {
                    mostrarError("Debes Rellenar la fecha del evento", tituloEditText);
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

    private void mostrarError(String mensaje, final EditText editText) {
        dialog = new AlertDialog.Builder(CrearEventoActivity.this);
        dialog.setTitle("Error");
        dialog.setMessage(mensaje);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogo, int id) {
                dialogo.cancel();
                editText.requestFocus();
            }
        });
        dialog.show();
    }

}