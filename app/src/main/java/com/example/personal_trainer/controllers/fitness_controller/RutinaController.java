package com.example.personal_trainer.controllers.fitness_controller;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.R;
import com.example.personal_trainer.models.fitness_model.EjercicioModel;
import com.example.personal_trainer.models.fitness_model.RutinaModel;
import com.example.personal_trainer.utils.Quadruple;
import com.example.personal_trainer.views.fitness_view.RutinaView;


import java.util.ArrayList;
import java.util.List;

public class RutinaController extends AppCompatActivity {
    private RutinaView rutinaView;
    private RutinaModel rutinaModel;
    private EjercicioModel ejercicioModel;

    private int idRutinaSeleccionado = -1;
    private int idEjercicioSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrutina);

        rutinaModel = new RutinaModel();
        rutinaModel.initBD(this);
        ejercicioModel = new EjercicioModel();
        ejercicioModel.initBD(this);

        View rootView = findViewById(android.R.id.content);
        rutinaView = new RutinaView(this, rootView);

        setupListeners();

        cargarDatosIniciales();
    }

    private void cargarRutinas() {
        List<String> nombres = new ArrayList<>();
        List<RutinaModel> rutinas = rutinaModel.consultar();

        for (RutinaModel rutina : rutinas) {
            nombres.add(rutina.getNombre());
        }

        rutinaView.mostrarRutinasEnListView(nombres);
    }

    private void manejarInsercionRutina() {
        String nombre = rutinaView.getNombreRutina();

        if (validarDatosBasicosRutina(nombre)) {
            List<Quadruple<Integer, Integer, Integer, Integer>> ejercicios = obtenerEjerciciosDesdeVista();
            boolean exito = rutinaModel.insertar(nombre, ejercicios);

            if (exito) {
                rutinaView.mostrarMensaje("Rutina insertado correctamente");
                cargarDatosIniciales();
            } else {
                rutinaView.mostrarMensaje("Error al insertar rutina");
            }
        }
    }

    private void manejarModificacionRutina() {
        if (idRutinaSeleccionado == -1) {
            rutinaView.mostrarMensaje("No hay rutina seleccionada");
            return;
        }

        String nombre = rutinaView.getNombreRutina();

        if (validarDatosBasicosRutina(nombre)) {
            List<Quadruple<Integer, Integer, Integer, Integer>> ejercicios = obtenerEjerciciosDesdeVista();
            boolean exito = rutinaModel.modificar(idRutinaSeleccionado, nombre, ejercicios);

            if (exito) {
                rutinaView.mostrarMensaje("Rutina modificada correctamente");
                cargarDatosIniciales();
            } else {
                rutinaView.mostrarMensaje("Error al modificar rutina");
            }
        }
    }

    private void manejarBorradoRutina() {
        if (idRutinaSeleccionado == -1) {
            rutinaView.mostrarMensaje("No hay rutina seleccionada");
            return;
        }

        boolean exito = rutinaModel.borrar(idRutinaSeleccionado);

        if (exito) {
            rutinaView.mostrarMensaje("Rutina borrada correctamente");
            cargarDatosIniciales();
        } else {
            rutinaView.mostrarMensaje("Error al borrar rutina");
        }
    }

    private void manejarAgregarEjercicio() {
        int posicion = rutinaView.getPosicionEjercicioSeleccionado();
        if (posicion <= 0) {
            rutinaView.mostrarMensaje("Selecciona un ejercicio válido");
            return;
        }

        EjercicioModel ejercicio = ejercicioModel.consultar().get(posicion - 1);
        int series = rutinaView.getCantidadSerie();
        int repeticiones = rutinaView.getCantidadRepeticion();
        int descanso = rutinaView.getDuracionReposo();

        String ejercicioConDetalles = construirTextoEjercicio(
                ejercicio.getNombre(),
                series,
                repeticiones,
                descanso
        );

        if (ejercicioYaAgregado(ejercicioConDetalles)) {
            rutinaView.mostrarMensaje("Este ejercicio ya fue agregado");
            return;
        }

        rutinaView.agregarEjercicioALista(ejercicioConDetalles);
        rutinaView.limpiarDescripcion();
        rutinaView.setPosicionEjercicioSeleccionado(0);
    }

    private void manejarSacarEjercicio() {
        if (idEjercicioSeleccionado == -1) {
            rutinaView.mostrarMensaje("Selecciona un ejercicio de la lista");
            return;
        }

        int posicion = obtenerPosicionEjercicioEnLista(idEjercicioSeleccionado);
        if (posicion != -1) {
            rutinaView.removerEjercicioDeLista(posicion);
            idEjercicioSeleccionado = -1;
            rutinaView.activarBotonSacar(false);
        }
    }

    private List<Quadruple<Integer, Integer, Integer, Integer>> obtenerEjerciciosDesdeVista() {
        List<Quadruple<Integer, Integer, Integer, Integer>> ejercicios = new ArrayList<>();

        for (String ejercicioConDetalles : rutinaView.getEjerciciosRutina()) {
            String[] partes = ejercicioConDetalles.split(" - ");
            String nombreEjercicio = partes[0];
            int series = Integer.parseInt(partes[1].replaceAll("[^0-9]", ""));
            int repeticiones = Integer.parseInt(partes[2].replaceAll("[^0-9]", ""));
            int descanso = Integer.parseInt(partes[3].replaceAll("[^0-9]", ""));

            EjercicioModel ejercicio = ejercicioModel.buscarPorNombre(nombreEjercicio);
            if (ejercicio != null) {
                ejercicios.add(new Quadruple<>(
                        ejercicio.getIdEjercicio(),
                        series,
                        repeticiones,
                        descanso
                ));
            }
        }

        return ejercicios;
    }

    private boolean validarDatosBasicosRutina(String nombre) {
        if (nombre.isEmpty()) {
            rutinaView.mostrarMensaje("El nombre de la rutina es obligatorio");
            return false;
        }
        return true;
    }


    private void setupListeners() {
        rutinaView.setBtnInsertarListener(v -> manejarInsercionRutina());
        rutinaView.setBtnModificarListener(v -> manejarModificacionRutina());
        rutinaView.setBtnBorrarListener(v -> manejarBorradoRutina());
        rutinaView.setBtnAgregarListener(v -> manejarAgregarEjercicio());
        rutinaView.setBtnSacarListener(v -> manejarSacarEjercicio());

        rutinaView.setListViewRutinasListener(this::manejarSeleccionRutina);
        rutinaView.setListViewEjerciciosRutinaListener(this::manejarSeleccionEjercicio);

        rutinaView.setSpinnerEjercicioListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rutinaView.activarBotonAgregar(position > 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //ejercicioView.activarBotonAgregar(false);
            }
        });
    }

    private void manejarSeleccionEjercicio(AdapterView<?> parent, View view, int position, long id) {
        String ejercicioSeleccionado = rutinaView.getEjerciciosRutina().get(position);
        String nombreEjercicio = ejercicioSeleccionado.split(" - ")[0];
        EjercicioModel ejercicio = ejercicioModel.buscarPorNombre(nombreEjercicio);

        if (ejercicio != null) {
            idEjercicioSeleccionado = ejercicio.getIdEjercicio();
            rutinaView.activarBotonSacar(true);
        }
    }


    private void cargarDatosIniciales() {
        cargarRutinas();
        cargarEjerciciosEnSpinner();
        resetearUI();
    }

    private void cargarEjerciciosEnSpinner() {
        List<String> nombresEjercicios = new ArrayList<>();
        nombresEjercicios.add("Elige una opción");

        for (EjercicioModel ejercicio : ejercicioModel.consultar()) {
            nombresEjercicios.add(ejercicio.getNombre());
        }

        rutinaView.configurarSpinnerEjercicios(nombresEjercicios);
    }

    private void cargarEjerciciosRutina(int idRutina) {
        rutinaView.limpiarEjerciciosRutina();
        List<Quadruple<Integer, Integer, Integer, Integer>> ejerciciosDetalle =
                rutinaModel.obtenerIdsEjerciciosRutina(idRutina);

        for (Quadruple<Integer, Integer, Integer, Integer> detalle : ejerciciosDetalle) {
            EjercicioModel ejercicio = ejercicioModel.buscar(detalle.getFirst());
            if (ejercicio != null) {
                String ejercicioConDetalles = construirTextoEjercicio(
                        ejercicio.getNombre(),
                        detalle.getSecond(),
                        detalle.getThird(),
                        detalle.getFourth()
                );
                rutinaView.agregarEjercicioALista(ejercicioConDetalles);
            }
        }
    }

    private String construirTextoEjercicio(String nombre, int series, int repeticiones, int descanso) {
        return String.format("%s - %d series - %d repeticiones - %d seg descanso",
                nombre, series, repeticiones, descanso);
    }

    private boolean ejercicioYaAgregado(String ejercicioConDetalles) {
        return rutinaView.getEjerciciosRutina().contains(ejercicioConDetalles);
    }

    private int obtenerPosicionEjercicioEnLista(int idEjercicio) {
        List<String> ejercicios = rutinaView.getEjerciciosRutina();
        for (int i = 0; i < ejercicios.size(); i++) {
            String nombreEjercicio = ejercicios.get(i).split(" - ")[0];
            EjercicioModel ejercicio = ejercicioModel.buscarPorNombre(nombreEjercicio);
            if (ejercicio != null && ejercicio.getIdEjercicio() == idEjercicio) {
                return i;
            }
        }
        return -1;
    }

    private void manejarSeleccionRutina(AdapterView<?> parent, View view, int position, long id) {
        List<RutinaModel> rutinas = rutinaModel.consultar();
        RutinaModel rutina = rutinas.get(position);
        idRutinaSeleccionado = rutina.getIdRutina();

        rutinaView.setNombreRutina(rutina.getNombre());
        rutinaView.habilitarModoEdicion(true);

        cargarEjerciciosRutina(idRutinaSeleccionado);
    }

    private void resetearUI() {
        rutinaView.limpiarCampos();
        idRutinaSeleccionado = -1;
        idEjercicioSeleccionado = -1;
    }
}
