package com.example.comercial;
// CrearEventoActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class CrearEventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        EditText tituloEditText = findViewById(R.id.tituloEditText);
        EditText fechaEditText = findViewById(R.id.editTextDate2);
        EditText ubicacionEditText = findViewById(R.id.ubicacionEditText);
        EditText descripcionEditText = findViewById(R.id.descripcionEditText);

        Button guardarButton = findViewById(R.id.guardarButton);
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = tituloEditText.getText().toString();
                String fecha = fechaEditText.getText().toString();
                String ubicacion = ubicacionEditText.getText().toString();
                String descripcion = descripcionEditText.getText().toString();

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
    }
}