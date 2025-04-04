package com.example.personal_trainer.controllers.registro_controller;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.R;
import com.example.personal_trainer.models.registro_model.ObjetivoModel;
import com.example.personal_trainer.views.registro_view.ObjetivoView;

import java.util.ArrayList;
import java.util.List;

public class ObjetivoController extends AppCompatActivity {
    private ObjetivoView objetivoView;
    private ObjetivoModel objetivoModel;
    private int idObjetivoSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vobjetivo);

        objetivoModel = new ObjetivoModel();
        objetivoModel.initBD(this);

        View rootView = findViewById(android.R.id.content);
        objetivoView = new ObjetivoView(this, rootView);

        setupListeners();

        cargarDatosIniciales();
    }

    private void setupListeners() {
        objetivoView.setBtnInsertarListener(v -> manejarInsercionObjetivo());
        objetivoView.setBtnModificarListener(v -> manejarModificacionObjetivo());
        objetivoView.setBtnBorrarListener(v -> manejarBorradoObjetivo());

        objetivoView.setListViewObjetivosListener(this::manejarSeleccionObjetivo);
    }

    private void cargarObjetivos() {
        List<String> nombres = new ArrayList<>();
        List<ObjetivoModel> objetivos = objetivoModel.consultar();

        for (ObjetivoModel objetivo : objetivos) {
            nombres.add(objetivo.getNombre());
        }

        objetivoView.mostrarObjetivosEnListView(nombres);
    }

    private void manejarInsercionObjetivo() {
        String nombre = objetivoView.getNombreObjetivo();

        if (validarDatosObjetivo(nombre)) {
            boolean exito = objetivoModel.insertar(nombre);

            if (exito) {
                objetivoView.mostrarMensaje("Objetivo insertado correctamente");
                resetearUI();
                cargarObjetivos();
            } else {
                objetivoView.mostrarMensaje("Error al insertar objetivo");
            }
        }
    }

    private void manejarModificacionObjetivo() {
        if (idObjetivoSeleccionado == -1) {
            objetivoView.mostrarMensaje("No hay objetivo seleccionado");
            return;
        }

        String nombre = objetivoView.getNombreObjetivo();

        if (validarDatosObjetivo(nombre)) {
            boolean exito = objetivoModel.modificar(idObjetivoSeleccionado, nombre);

            if (exito) {
                objetivoView.mostrarMensaje("Objetivo modificado correctamente");
                resetearUI();
                cargarObjetivos();
            } else {
                objetivoView.mostrarMensaje("Error al modificar objetivo");
            }
        }
    }

    private void manejarBorradoObjetivo() {
        if (idObjetivoSeleccionado == -1) {
            objetivoView.mostrarMensaje("No hay objetivo seleccionado");
            return;
        }

        boolean exito = objetivoModel.borrar(idObjetivoSeleccionado);

        if (exito) {
            objetivoView.mostrarMensaje("Objetivo borrado correctamente");
            resetearUI();
            cargarObjetivos();
        } else {
            objetivoView.mostrarMensaje("Error al borrar objetivo");
        }
    }

    private void manejarSeleccionObjetivo(AdapterView<?> parent, View view, int position, long id) {
        List<ObjetivoModel> objetivos = objetivoModel.consultar();
        ObjetivoModel objetivo = objetivos.get(position);
        idObjetivoSeleccionado = objetivo.getIdObjetivo();

        objetivoView.setNombreObjetivo(objetivo.getNombre());
        objetivoView.habilitarModoEdicion(true);
    }

    private boolean validarDatosObjetivo(String nombre) {
        if (nombre.isEmpty()) {
            objetivoView.mostrarMensaje("El nombre del objetivo no puede estar vacío");
            return false;
        }
        return true;
    }

    private void resetearUI() {
        objetivoView.limpiarCampos();
        idObjetivoSeleccionado = -1;
    }

    private void cargarDatosIniciales() {
        cargarObjetivos();
        resetearUI();
    }
}