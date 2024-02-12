package com.example.comercial.partners;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comercial.BBDD.DbHelper;
import com.example.comercial.BBDD.Partner;
import com.example.comercial.Metodos;
import com.example.comercial.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Actividad_AltaPartner extends AppCompatActivity {

    EditText eNombre, eDireccion, eCif, eTelefono, eEmail;
    Button bAlta, bLimpiar;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altapartner);

        db = new DbHelper(this);

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
            } else {
                Toast.makeText(this, "Rellene todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void altaPartner() {
        String nombre = eNombre.getText().toString();
        String direccion = eDireccion.getText().toString();
        String cif = eCif.getText().toString();
        String telefono = eTelefono.getText().toString();
        String email = eEmail.getText().toString();

        String fechaRegistro = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Partner nuevoPartner = new Partner(nombre, cif, direccion, telefono, email, fechaRegistro);

        long id = db.addPartner(nuevoPartner);
        if (id > 0) {
            Toast.makeText(this, "Partner agregado con éxito.", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al agregar partner.", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validarCampos() {
        // Asegurarse de que todos los campos cumplen con los criterios de validación
        boolean nombreValido = Metodos.isValidCampo(eNombre, "Nombre", Metodos.FieldType.STRING, this);
        boolean direccionValida = Metodos.isValidCampo(eDireccion, "Dirección", Metodos.FieldType.STRING, this);
        boolean cifValido = Metodos.isValidCampo(eCif, "CIF", Metodos.FieldType.STRING, this);
        boolean telefonoValido = Metodos.isValidCampo(eTelefono, "Teléfono", Metodos.FieldType.TELEPHONE, this);
        boolean emailValido = Metodos.isValidCampo(eEmail, "Email", Metodos.FieldType.EMAIL, this);

        // Si todos los campos son válidos, retorna true. De lo contrario, false.
        return nombreValido && direccionValida && cifValido && telefonoValido && emailValido;
    }

    private void limpiarCampos() {
        eNombre.setText("");
        eDireccion.setText("");
        eCif.setText("");
        eTelefono.setText("");
        eEmail.setText("");
    }
}
