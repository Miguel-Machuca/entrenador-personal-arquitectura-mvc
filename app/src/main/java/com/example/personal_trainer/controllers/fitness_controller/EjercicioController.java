package com.example.personal_trainer.controllers.fitness_controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.R;
import com.example.personal_trainer.models.fitness_model.EjercicioModel;
import com.example.personal_trainer.views.fitness_view.EjercicioView;

import java.util.ArrayList;
import java.util.List;

public class EjercicioController extends AppCompatActivity {
    private EjercicioView ejercicioView;
    private EjercicioModel ejercicioModel;
    private ActivityResultLauncher<Intent> seleccionarImagenLauncher;
    private Uri imagenSeleccionadaUri;
    private int idEjercicioSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vejercicio);

        ejercicioModel = new EjercicioModel();
        ejercicioModel.initBD(this);

        View rootView = findViewById(android.R.id.content);
        ejercicioView = new EjercicioView(this, rootView);

        configurarSeleccionarImagenLauncher();

        setupListeners();

        cargarDatosIniciales();
    }

    private void configurarSeleccionarImagenLauncher() {
        seleccionarImagenLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imagenSeleccionadaUri = result.getData().getData();
                        ejercicioView.setImagenEjercicio(imagenSeleccionadaUri);
                    }
                }
        );
    }

    private void setupListeners() {
        ejercicioView.setBtnInsertarListener(v -> manejarInsercionEjercicio());
        ejercicioView.setBtnModificarListener(v -> manejarModificacionEjercicio());
        ejercicioView.setBtnBorrarListener(v -> manejarBorradoEjercicio());
        ejercicioView.setBtnSubirImagenListener(v -> abrirSelectorImagenes());

        ejercicioView.setListViewEjerciciosListener(this::manejarSeleccionEjercicio);
    }

    private void cargarEjercicios() {
        List<String> nombres = new ArrayList<>();
        List<EjercicioModel> ejercicios = ejercicioModel.consultar();

        for (EjercicioModel ejercicio : ejercicios) {
            nombres.add(ejercicio.getNombre());
        }

        ejercicioView.mostrarEjerciciosEnListView(nombres);
    }

    private void manejarInsercionEjercicio() {
        String nombre = ejercicioView.getNombreEjercicio();
        String urlImagen = (imagenSeleccionadaUri != null) ? imagenSeleccionadaUri.toString() : "";

        if (validarDatosEjercicio(nombre)) {
            boolean exito = ejercicioModel.insertar(nombre, urlImagen);

            if (exito) {
                ejercicioView.mostrarMensaje("Ejercicio insertado correctamente");
                resetearUI();
                cargarEjercicios();
            } else {
                ejercicioView.mostrarMensaje("Error al insertar ejercicio");
            }
        }
    }

    private void manejarModificacionEjercicio() {
        if (idEjercicioSeleccionado == -1) {
            ejercicioView.mostrarMensaje("No hay ejercicio seleccionado");
            return;
        }

        String nombre = ejercicioView.getNombreEjercicio();
        String urlImagen = (imagenSeleccionadaUri != null) ? imagenSeleccionadaUri.toString() : "";

        if (validarDatosEjercicio(nombre)) {
            boolean exito = ejercicioModel.modificar(idEjercicioSeleccionado, nombre, urlImagen);

            if (exito) {
                ejercicioView.mostrarMensaje("Ejercicio modificado correctamente");
                resetearUI();
                cargarEjercicios();
            } else {
                ejercicioView.mostrarMensaje("Error al modificar ejercicio");
            }
        }
    }

    private void manejarBorradoEjercicio() {
        if (idEjercicioSeleccionado == -1) {
            ejercicioView.mostrarMensaje("No hay ejercicio seleccionado");
            return;
        }

        boolean exito = ejercicioModel.borrar(idEjercicioSeleccionado);

        if (exito) {
            ejercicioView.mostrarMensaje("Ejercicio borrado correctamente");
            resetearUI();
            cargarEjercicios();
        } else {
            ejercicioView.mostrarMensaje("Error al borrar ejercicio");
        }
    }

    private void manejarSeleccionEjercicio(AdapterView<?> parent, View view, int position, long id) {
        List<EjercicioModel> ejercicios = ejercicioModel.consultar();
        EjercicioModel ejercicio = ejercicios.get(position);
        idEjercicioSeleccionado = ejercicio.getIdEjercicio();

        ejercicioView.setNombreEjercicio(ejercicio.getNombre());
        ejercicioView.habilitarModoEdicion(true);

        if (ejercicio.getUrlImagen() != null && !ejercicio.getUrlImagen().isEmpty()) {
            imagenSeleccionadaUri = Uri.parse(ejercicio.getUrlImagen());
            ejercicioView.setImagenEjercicio(imagenSeleccionadaUri);
        } else {
            imagenSeleccionadaUri = null;
            ejercicioView.setImagenEjercicio(null);
        }
    }

    private void abrirSelectorImagenes() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        seleccionarImagenLauncher.launch(intent);
    }

    private boolean validarDatosEjercicio(String nombre) {
        if (nombre.isEmpty()) {
            ejercicioView.mostrarMensaje("El nombre del ejercicio no puede estar vacío");
            return false;
        }
        return true;
    }

    private void resetearUI() {
        ejercicioView.limpiarCampos();
        idEjercicioSeleccionado = -1;
        imagenSeleccionadaUri = null;
    }

    private void cargarDatosIniciales() {
        cargarEjercicios();
        resetearUI();
    }
}