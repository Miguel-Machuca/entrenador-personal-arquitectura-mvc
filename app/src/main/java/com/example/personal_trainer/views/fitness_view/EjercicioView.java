package com.example.personal_trainer.views.fitness_view;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.R;

public class EjercicioView extends AppCompatActivity implements View.OnClickListener {
    private EditText txtNombreEjercicio;
    private Button btnInsertar, btnModificar, btnBorrar, btnSubirImagen;
    private ListView listViewConsultar;
    private ImageView ivImagenEjercicio;
    private Uri imagenSeleccionadaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vejercicio);
        init();
    }

    private void init() {
        // Inicializar vistas
        txtNombreEjercicio = findViewById(R.id.txt_nombre_ejercicio);
        btnInsertar = findViewById(R.id.btn_insertar_ejercicio);
        btnModificar = findViewById(R.id.btn_modificar_ejercicio);
        btnBorrar = findViewById(R.id.btn_borrar_ejercicio);
        btnSubirImagen = findViewById(R.id.btn_subir_imagen);
        listViewConsultar = findViewById(R.id.lv_items_ejercicio);
        ivImagenEjercicio = findViewById(R.id.iv_imagen_ejercicio);
        configurarBotones();
    }

    private void configurarBotones() {
        btnInsertar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnSubirImagen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_insertar_ejercicio) {
            //insertarEjercicio();
        } else if (id == R.id.btn_modificar_ejercicio) {
            //modificarEjercicio();
        } else if (id == R.id.btn_borrar_ejercicio) {
            //borrarEjercicio();
        } else if (id == R.id.btn_subir_imagen) {
            //subirImagen();
        }
    }

}
