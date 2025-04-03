package com.example.personal_trainer.views.fitness_view;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.personal_trainer.R;

import java.util.List;

public class EjercicioView {
    private EditText txtNombreEjercicio;
    private ImageView ivImagenEjercicio;
    private ListView listViewEjercicios;
    private Button btnInsertar;
    private Button btnModificar;
    private Button btnBorrar;
    private Button btnSubirImagen;
    private Context context;

    public EjercicioView(Context context, View rootView) {
        this.context = context;
        initializeUI(rootView);
    }

    private void initializeUI(View rootView) {
        txtNombreEjercicio = rootView.findViewById(R.id.txt_nombre_ejercicio);
        btnInsertar = rootView.findViewById(R.id.btn_insertar_ejercicio);
        btnModificar = rootView.findViewById(R.id.btn_modificar_ejercicio);
        btnBorrar = rootView.findViewById(R.id.btn_borrar_ejercicio);
        btnSubirImagen = rootView.findViewById(R.id.btn_subir_imagen);
        listViewEjercicios = rootView.findViewById(R.id.lv_items_ejercicio);
        ivImagenEjercicio = rootView.findViewById(R.id.iv_imagen_ejercicio);
    }

    // Métodos para configurar listeners
    public void setBtnInsertarListener(View.OnClickListener listener) {
        btnInsertar.setOnClickListener(listener);
    }

    public void setBtnModificarListener(View.OnClickListener listener) {
        btnModificar.setOnClickListener(listener);
    }

    public void setBtnBorrarListener(View.OnClickListener listener) {
        btnBorrar.setOnClickListener(listener);
    }

    public void setBtnSubirImagenListener(View.OnClickListener listener) {
        btnSubirImagen.setOnClickListener(listener);
    }

    public void setListViewEjerciciosListener(ListView.OnItemClickListener listener) {
        listViewEjercicios.setOnItemClickListener(listener);
    }

    // Métodos para manipulación de datos en la UI
    public void mostrarEjerciciosEnListView(List<String> nombres) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombres);
        listViewEjercicios.setAdapter(adapter);
    }

    // Getters para obtener datos de la UI
    public String getNombreEjercicio() {
        return txtNombreEjercicio.getText().toString().trim();
    }

    // Setters para establecer datos en la UI
    public void setNombreEjercicio(String nombre) {
        txtNombreEjercicio.setText(nombre);
    }

    public void setImagenEjercicio(Uri imagenUri) {
        if (imagenUri != null) {
            ivImagenEjercicio.setImageURI(imagenUri);
        } else {
            ivImagenEjercicio.setImageResource(R.drawable.sin_imagen);
        }
    }

    // Métodos para control de estado de la UI
    public void habilitarModoEdicion(boolean habilitar) {
        btnInsertar.setEnabled(!habilitar);
        btnModificar.setEnabled(habilitar);
        btnBorrar.setEnabled(habilitar);
    }

    // Métodos para limpieza de la UI
    public void limpiarCampos() {
        txtNombreEjercicio.setText("");
        setImagenEjercicio(null);
        habilitarModoEdicion(false);
    }

    // Métodos para feedback al usuario
    public void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}