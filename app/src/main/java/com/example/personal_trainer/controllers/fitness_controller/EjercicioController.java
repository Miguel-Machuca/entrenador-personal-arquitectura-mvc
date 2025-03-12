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
    private EjercicioModel ejercicioModel;
    private EjercicioView ejercicioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ejercicioModel = new EjercicioModel();
        ejercicioModel.initBD(this);

        setContentView(R.layout.activity_vejercicio);
        View rootView = findViewById(android.R.id.content);
        ejercicioView = new EjercicioView(this, rootView, this);

        ejercicioView.setBtnInsertarListener(this::insertarEjercicio);
        ejercicioView.setBtnModificarListener(this::modificarEjercicio);
        ejercicioView.setBtnBorrarListener(this::borrarEjercicio);
        ejercicioView.setListViewEjerciciosListener(this::itemClickListView);

        this.reiniciarActividad();
    }

    public void insertarEjercicio() {
        try {
            String nombre = ejercicioView.getNombreEjercicio();
            if (nombre.isEmpty()) {
                ejercicioView.mensaje("El nombre del ejercicio no puede estar vacío.");
                return;
            }

            String urlImagen = ejercicioView.getUrlImagen();

            boolean resultado = ejercicioModel.insertar(nombre, urlImagen);

            if (resultado) {
                ejercicioView.mensaje("Ejercicio insertado correctamente.");
                this.reiniciarActividad();
            } else {
                ejercicioView.mensaje("Error al insertar el ejercicio.");
            }
        } catch (Exception e) {
            ejercicioView.mensaje("Error: " + e.getMessage());
        }
    }

    public void modificarEjercicio() {
        try {
            int idEjercicio = ejercicioView.getIdEjercicio();
            String nombre = ejercicioView.getNombreEjercicio();
            String urlImagen = ejercicioView.getUrlImagen();

            if (nombre.isEmpty()) {
                ejercicioView.mensaje("El nombre del objetivo no puede estar vacío.");
                return;
            }

            boolean resultado = ejercicioModel.modificar(idEjercicio, nombre, urlImagen);

            if (resultado) {
                ejercicioView.mensaje("Objetivo modificado correctamente.");
                this.reiniciarActividad();
            } else {
                ejercicioView.mensaje("Error al modificar el objetivo.");
            }
        } catch (Exception e) {
            ejercicioView.mensaje("Error: " + e.getMessage());
        }
    }

    public void borrarEjercicio() {
        try {
            int idEjercicio = ejercicioView.getIdEjercicio();

            if (idEjercicio == -1) {
                ejercicioView.mensaje("No hay ningún objetivo seleccionado.");
                return;
            }

            boolean resultado = ejercicioModel.borrar(idEjercicio);

            if (resultado) {
                ejercicioView.mensaje("Objetivo borrado correctamente.");
                this.reiniciarActividad();
            } else {
                ejercicioView.mensaje("Error al borrar el objetivo.");
            }
        } catch (Exception e) {
            ejercicioView.mensaje("Error: " + e.getMessage());
        }
    }

    public void consultarEjercicios() {
        List<String> nombres = new ArrayList<>();
        List<Integer> idEjercicios = new ArrayList<>();

        List<EjercicioModel> listaEjercicios = ejercicioModel.consultar();

        for (EjercicioModel ejercicio : listaEjercicios) {
            nombres.add(ejercicio.getNombre());
            idEjercicios.add(ejercicio.getIdEjercicio());
        }
        ejercicioView.mostrarEjerciciosEnListView(nombres);
    }

    private void itemClickListView(AdapterView<?> adapterView, View view, int position, long id) {
        List<EjercicioModel> listaEjercicios = ejercicioModel.consultar();
        int ejercicioIdSeleccionado = listaEjercicios.get(position).getIdEjercicio();

        EjercicioModel ejercicio = ejercicioModel.buscar(ejercicioIdSeleccionado);

        if (ejercicio != null) {
            ejercicioView.setNombreEjercicio(ejercicio.getNombre());
            ejercicioView.setIdEjercicio(ejercicio.getIdEjercicio());
            establecerImagenEjercicio(ejercicio);
            ejercicioView.btnModificarBtnBorrar();
        } else {
            ejercicioView.mensaje("Objetivo no encontrado.");
        }
    }

    private void establecerImagenEjercicio(EjercicioModel ejercicio) {
        if (ejercicio.getUrlImagen() == null || ejercicio.getUrlImagen().isEmpty()) {
            Uri uriImagenPorDefecto = Uri.parse("android.resource://com.example.personal_trainer/" + R.drawable.sin_imagen);
            ejercicioView.setUrlImagen(uriImagenPorDefecto.toString());
        } else {
            ejercicioView.setUrlImagen(ejercicio.getUrlImagen());
        }
    }

    private void reiniciarActividad() {
        ejercicioView.limpiarCampos();
        this.consultarEjercicios();
    }
}