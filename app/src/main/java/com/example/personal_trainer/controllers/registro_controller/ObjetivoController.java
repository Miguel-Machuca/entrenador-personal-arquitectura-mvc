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

    private ObjetivoModel objetivoModel;
    private ObjetivoView objetivoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objetivoModel = new ObjetivoModel();
        objetivoModel.initBD(this);

        setContentView(R.layout.activity_vobjetivo);
        View rootView = findViewById(android.R.id.content);
        objetivoView = new ObjetivoView(this, rootView);

        objetivoView.setBtnInsertarListener(this::insertarObjetivo);
        objetivoView.setBtnModificarListener(this::modificarObjetivo);
        objetivoView.setBtnBorrarListener(this::borrarObjetivo);
        objetivoView.setListViewObjetivosListener(this::itemClickListView);
        this.reiniciarActividad();
    }

    public void insertarObjetivo() {
        try {
            String nombre = objetivoView.getNombreObjetivo();
            if (nombre.isEmpty()) {
                objetivoView.mensaje("El nombre del objetivo no puede estar vacío.");
                return;
            }
            boolean resultado = objetivoModel.insertar(nombre);
            if (resultado) {
                objetivoView.mensaje("Objetivo insertado correctamente.");
                this.reiniciarActividad();
            } else {
                objetivoView.mensaje("Error al insertar el objetivo.");
            }
        } catch (Exception e) {
            objetivoView.mensaje("Error: " + e.getMessage());
        }
    }

    public void modificarObjetivo() {
        try {
            int idObjetivo = objetivoView.getIdObjetivo();
            String nombre = objetivoView.getNombreObjetivo();

            if (nombre.isEmpty()) {
                objetivoView.mensaje("El nombre del objetivo no puede estar vacío.");
                return;
            }

            boolean resultado = objetivoModel.modificar(idObjetivo, nombre);

            if (resultado) {
                objetivoView.mensaje("Objetivo modificado correctamente.");
                this.reiniciarActividad();
            } else {
                objetivoView.mensaje("Error al modificar el objetivo.");
            }
        } catch (Exception e) {
            objetivoView.mensaje("Error: " + e.getMessage());
        }
    }

    public void borrarObjetivo() {
        try {
            int idObjetivo = objetivoView.getIdObjetivo();

            if (idObjetivo == -1) {
                objetivoView.mensaje("No hay ningún objetivo seleccionado.");
                return;
            }

            boolean resultado = objetivoModel.borrar(idObjetivo);

            if (resultado) {
                objetivoView.mensaje("Objetivo borrado correctamente.");
                this.reiniciarActividad();
            } else {
                objetivoView.mensaje("Error al borrar el objetivo.");
            }
        } catch (Exception e) {
            objetivoView.mensaje("Error: " + e.getMessage());
        }
    }

    public void consultarObjetivos() {
        List<String> nombres = new ArrayList<>();
        List<Integer> idObjetivos = new ArrayList<>();

        List<ObjetivoModel> listaObjetivos = objetivoModel.consultar();

        for (ObjetivoModel objetivo : listaObjetivos) {
            nombres.add(objetivo.getNombre());
            idObjetivos.add(objetivo.getIdObjetivo());
        }
        objetivoView.mostrarObjetivosEnListView(nombres);
    }

    private void itemClickListView(AdapterView<?> adapterView, View view, int position, long id) {

        List<ObjetivoModel> listaObjetivos = objetivoModel.consultar();
        int objetivoIdSeleccionado = listaObjetivos.get(position).getIdObjetivo();

        ObjetivoModel objetivo = objetivoModel.buscar(objetivoIdSeleccionado);

        if (objetivo != null) {
            objetivoView.setNombreObjetivo(objetivo.getNombre());
            objetivoView.setIdObjetivo(objetivo.getIdObjetivo());
            objetivoView.btnModificarBtnBorrar();
        } else {
            objetivoView.mensaje("Objetivo no encontrado.");
        }
    }

    private void reiniciarActividad() {
        objetivoView.limpiarCampos();
        this.consultarObjetivos();
    }
}