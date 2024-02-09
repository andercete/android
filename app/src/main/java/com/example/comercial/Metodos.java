package com.example.comercial;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Metodos {

    public static enum FieldType {
        STRING,
        INTEGER,
        FLOAT,
        PERCENTAGE,
        EMAIL,
        TELEPHONE
    }


    public static void mostrarAlertaValidacion(String titulo, String mensaje, final EditText editText, Context actividad) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(actividad);
        dialog.setTitle(titulo);
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
    public static void mostrarAlerta(String titulo, String mensaje, Context actividad) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(actividad);
        dialog.setTitle(titulo);
        dialog.setMessage(mensaje);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogo, int id) {
                dialogo.cancel();
            }
        });
        dialog.show();
    }
    public static boolean isValidCampo(EditText editText, String campoName, Metodos.FieldType fieldType, Context actividad ) {
        String input = editText.getText().toString().trim();
        Pattern phonePattern = Pattern.compile("^[0-9]{9}$");

        if (input.length() == 0) {
            mostrarAlertaValidacion("Error","Por favor, ingrese un " + campoName, editText, actividad);
            return false;
        }

        switch (fieldType) {
            case STRING:
                if (input.length() <= 1) {
                    mostrarAlertaValidacion("Error",campoName + " debe tener más de un carácter", editText, actividad);
                    return false;
                }if (isNumeric(input)) {
                mostrarAlertaValidacion("Error","El campo " + campoName + " no puede contener solo números", editText, actividad);
                return false;
            }
                break;
            case INTEGER:
                try {
                    int value = Integer.parseInt(input);
                    if (value <= 0) {
                        mostrarAlertaValidacion("Error",campoName + " debe ser mayor que cero", editText, actividad);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    mostrarAlertaValidacion("Error","Por favor, ingrese un " + campoName + " válido", editText, actividad);
                    return false;
                }
                break;
            case FLOAT:
                try {
                    float value = Float.parseFloat(input);
                    if (value <= 0) {
                        mostrarAlertaValidacion("Error",campoName + " debe ser mayor que cero", editText, actividad);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    mostrarAlertaValidacion("Error","Por favor, ingrese un " + campoName + " válido", editText, actividad);
                    return false;
                }
                break;
            case PERCENTAGE:
                try {
                    float value = Float.parseFloat(input);
                    if (value < 0 || value > 100) {
                        mostrarAlertaValidacion("Error",campoName + " debe estar entre 0 y 100", editText, actividad);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    mostrarAlertaValidacion("Error","Por favor, ingrese un " + campoName + " válido", editText, actividad);
                    return false;
                }
                break;
            case EMAIL:
                if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    mostrarAlertaValidacion("Error","Por favor, ingrese un " + campoName + " válido", editText,actividad);
                    return false;
                }
                break;
            case TELEPHONE:
                if (!phonePattern.matcher(input).matches()) {
                    mostrarAlertaValidacion("Error","Por favor, ingrese un teléfono válido", editText,actividad);
                    return false;
                }
                break;
        }

        return true;
    }
    public static Element createElementWithText(Document doc, String tagName, String text) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(text));
        return element;
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    public static void lanzarToast(String mensaje, Context actividad) {
        Toast.makeText(actividad, mensaje, Toast.LENGTH_SHORT).show();
    }

    public static String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getNombreArchivo(String extension) {
        return obtenerFechaActual() + extension;
    }

    public static String getNombreArchivoPartners() {
        return getNombreArchivo("_partners.xml");
    }

    public static String getNombreArchivoPedidos() {
        return getNombreArchivo("_pedidos.xml");
    }




}
