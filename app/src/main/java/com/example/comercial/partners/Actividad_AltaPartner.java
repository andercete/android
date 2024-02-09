package com.example.comercial.partners;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comercial.BBDD.AnderBD;
import com.example.comercial.R;

public class Actividad_AltaPartner extends AppCompatActivity {

    EditText eNombre, eDireccion,eCif, eTelefono, eEmail;
    Button bAlta, bLimpiar;
    AnderBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altapartner);

        db = new AnderBD(this);

        eNombre = findViewById(R.id.eAltaNombre);
        eDireccion = findViewById(R.id.eAltaDireccion);
        eCif = findViewById(R.id.eAltaCif);
        eTelefono = findViewById(R.id.eAltaTelefono);
        eEmail = findViewById(R.id.eAltaEmail);
        bAlta = findViewById(R.id.bAlta);
        bLimpiar = findViewById(R.id.bAltaLimpiar);

        bLimpiar.setOnClickListener(v -> limpiarCampos());

        bAlta.setOnClickListener(v -> {
            if (validarCampos()) {
                altaPartner();
            }
        });
    }

    private void altaPartner() {
        String nombre = eNombre.getText().toString();
        String direccion = eDireccion.getText().toString();
        String cif = eCif.getText().toString();
        String telefono = eTelefono.getText().toString();
        String email = eEmail.getText().toString();

        // Aquí deberías determinar el idZona basado en la población o cualquier otra lógica.
        // Como ejemplo, simplemente ponemos 1, pero deberías reemplazar esto con la lógica correcta.
        int idZona = 1;

        Partner nuevoPartner = new Partner(0, idZona, nombre, cif, direccion, telefono, email, ""); // Asume fechaRegistro vacío o establece una fecha actual.

        long id = db.addPartner(nuevoPartner);
        if (id > 0) {
            Toast.makeText(this, "Partner agregado con éxito.", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al agregar partner.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        // Aquí implementarías la lógica de validación para cada campo.
        // Por ejemplo, puedes verificar que el nombre no esté vacío, que el CIF tenga un formato válido, etc.
        return !eNombre.getText().toString().isEmpty() &&
                !eDireccion.getText().toString().isEmpty() &&
                !eCif.getText().toString().isEmpty() &&
                !eTelefono.getText().toString().isEmpty() &&
                !eEmail.getText().toString().isEmpty();
    }

    private void limpiarCampos() {
        eNombre.setText("");
        eDireccion.setText("");
        eCif.setText("");
        eTelefono.setText("");
        eEmail.setText("");
    }
}
