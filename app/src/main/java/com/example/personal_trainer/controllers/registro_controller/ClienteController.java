package com.example.personal_trainer.controllers.registro_controller;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.R;
import com.example.personal_trainer.models.registro_model.ClienteModel;
import com.example.personal_trainer.models.registro_model.ObjetivoModel;
import com.example.personal_trainer.views.registro_view.ClienteView;

import java.util.ArrayList;
import java.util.List;

public class ClienteController extends AppCompatActivity {
    private ClienteView clienteView;
    private ClienteModel clienteModel;
    private ObjetivoModel objetivoModel;

    private int idClienteSeleccionado = -1;
    private int idObjetivoSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcliente);

        clienteModel = new ClienteModel();
        clienteModel.initBD(this);
        objetivoModel = new ObjetivoModel();
        objetivoModel.initBD(this);

        View rootView = findViewById(android.R.id.content);
        clienteView = new ClienteView(this, rootView);

        setupListeners();

        cargarDatosIniciales();
    }

    private void cargarClientes() {
        List<String> nombres = new ArrayList<>();
        List<ClienteModel> clientes = clienteModel.consultar();

        for (ClienteModel cliente : clientes) {
            nombres.add(cliente.getNombre());
        }

        clienteView.mostrarClientesEnListView(nombres);
    }

    private void manejarInsercionCliente() {
        String nombre = clienteView.getNombreCliente();
        String apellido = clienteView.getApellidoCliente();
        String celular = clienteView.getCelular();

        if (validarDatosBasicosCliente(nombre, apellido, celular)) {
            List<Pair<Integer, String>> objetivos = obtenerObjetivosDesdeVista();
            boolean exito = clienteModel.insertar(nombre, apellido, celular, objetivos);

            if (exito) {
                clienteView.mostrarMensaje("Cliente insertado correctamente");
                cargarDatosIniciales();
            } else {
                clienteView.mostrarMensaje("Error al insertar cliente");
            }
        }
    }

    private void manejarModificacionCliente() {
        if (idClienteSeleccionado == -1) {
            clienteView.mostrarMensaje("No hay cliente seleccionado");
            return;
        }

        String nombre = clienteView.getNombreCliente();
        String apellido = clienteView.getApellidoCliente();
        String celular = clienteView.getCelular();

        if (validarDatosBasicosCliente(nombre, apellido, celular)) {
            List<Pair<Integer, String>> objetivos = obtenerObjetivosDesdeVista();
            boolean exito = clienteModel.modificar(idClienteSeleccionado, nombre, apellido, celular, objetivos);

            if (exito) {
                clienteView.mostrarMensaje("Cliente modificado correctamente");
                cargarDatosIniciales();
            } else {
                clienteView.mostrarMensaje("Error al modificar cliente");
            }
        }
    }

    private void manejarBorradoCliente() {
        if (idClienteSeleccionado == -1) {
            clienteView.mostrarMensaje("No hay cliente seleccionado");
            return;
        }

        boolean exito = clienteModel.borrar(idClienteSeleccionado);

        if (exito) {
            clienteView.mostrarMensaje("Cliente borrado correctamente");
            cargarDatosIniciales();
        } else {
            clienteView.mostrarMensaje("Error al borrar cliente");
        }
    }

    private void manejarSeleccionCliente(AdapterView<?> parent, View view, int position, long id) {
        List<ClienteModel> clientes = clienteModel.consultar();
        ClienteModel cliente = clientes.get(position);
        idClienteSeleccionado = cliente.getIdCliente();

        // Mostrar datos del cliente
        clienteView.setNombreCliente(cliente.getNombre());
        clienteView.setApellidoCliente(cliente.getApellido());
        clienteView.setCelular(cliente.getCelular());
        clienteView.habilitarModoEdicion(true);

        // Cargar objetivos del cliente
        cargarObjetivosCliente(idClienteSeleccionado);
    }

    private void cargarObjetivosCliente(int idCliente) {
        clienteView.limpiarObjetivosCliente();
        List<Pair<Integer, String>> objetivosDetalle = clienteModel.obtenerIdsObjetivosCliente(idCliente);

        for (Pair<Integer, String> detalle : objetivosDetalle) {
            ObjetivoModel objetivo = objetivoModel.buscar(detalle.first);
            if (objetivo != null) {
                String objetivoConDescripcion = construirTextoObjetivo(objetivo.getNombre(), detalle.second);
                clienteView.agregarObjetivoALista(objetivoConDescripcion);
            }
        }
    }

    private void cargarObjetivosEnSpinner() {
        List<String> nombresObjetivos = new ArrayList<>();
        nombresObjetivos.add("Elige una opción");

        for (ObjetivoModel objetivo : objetivoModel.consultar()) {
            nombresObjetivos.add(objetivo.getNombre());
        }

        clienteView.configurarSpinnerObjetivos(nombresObjetivos);
    }

    private void manejarAgregarObjetivo() {
        int posicion = clienteView.getPosicionObjetivoSeleccionado();
        if (posicion <= 0) {
            clienteView.mostrarMensaje("Selecciona un objetivo válido");
            return;
        }

        ObjetivoModel objetivo = objetivoModel.consultar().get(posicion - 1);
        String descripcion = clienteView.getDescripcion();
        String objetivoConDescripcion = construirTextoObjetivo(objetivo.getNombre(), descripcion);

        if (objetivoYaAgregado(objetivoConDescripcion)) {
            clienteView.mostrarMensaje("Este objetivo ya fue agregado");
            return;
        }

        clienteView.agregarObjetivoALista(objetivoConDescripcion);
        clienteView.limpiarDescripcion();
        clienteView.setPosicionObjetivoSeleccionado(0);
    }

    private void manejarSacarObjetivo() {
        if (idObjetivoSeleccionado == -1) {
            clienteView.mostrarMensaje("Selecciona un objetivo de la lista");
            return;
        }

        int posicion = obtenerPosicionObjetivoEnLista(idObjetivoSeleccionado);
        if (posicion != -1) {
            clienteView.removerObjetivoDeLista(posicion);
            idObjetivoSeleccionado = -1;
            clienteView.activarBotonSacar(false);
        }
    }

    private void manejarSeleccionObjetivo(AdapterView<?> parent, View view, int position, long id) {
        String objetivoSeleccionado = clienteView.getObjetivosCliente().get(position);
        String nombreObjetivo = objetivoSeleccionado.split(" - ")[0];
        ObjetivoModel objetivo = objetivoModel.buscarPorNombre(nombreObjetivo);

        if (objetivo != null) {
            idObjetivoSeleccionado = objetivo.getIdObjetivo();
            clienteView.activarBotonSacar(true);
        }
    }

    private boolean validarDatosBasicosCliente(String nombre, String apellido, String celular) {
        if (nombre.isEmpty() || apellido.isEmpty() || celular.isEmpty()) {
            clienteView.mostrarMensaje("Todos los campos básicos son obligatorios");
            return false;
        }
        return true;
    }

    private List<Pair<Integer, String>> obtenerObjetivosDesdeVista() {
        List<Pair<Integer, String>> objetivos = new ArrayList<>();

        for (String objetivoConDescripcion : clienteView.getObjetivosCliente()) {
            String[] partes = objetivoConDescripcion.split(" - ", 2);
            String nombreObjetivo = partes[0];
            String descripcion = partes.length > 1 ? partes[1] : "";

            ObjetivoModel objetivo = objetivoModel.buscarPorNombre(nombreObjetivo);
            if (objetivo != null) {
                objetivos.add(new Pair<>(objetivo.getIdObjetivo(), descripcion));
            }
        }

        return objetivos;
    }

    private String construirTextoObjetivo(String nombreObjetivo, String descripcion) {
        return descripcion.isEmpty() ? nombreObjetivo : nombreObjetivo + " - " + descripcion;
    }

    private boolean objetivoYaAgregado(String objetivoConDescripcion) {
        return clienteView.getObjetivosCliente().contains(objetivoConDescripcion);
    }

    private int obtenerPosicionObjetivoEnLista(int idObjetivo) {
        List<String> objetivos = clienteView.getObjetivosCliente();
        for (int i = 0; i < objetivos.size(); i++) {
            String nombreObjetivo = objetivos.get(i).split(" - ")[0];
            ObjetivoModel objetivo = objetivoModel.buscarPorNombre(nombreObjetivo);
            if (objetivo != null && objetivo.getIdObjetivo() == idObjetivo) {
                return i;
            }
        }
        return -1;
    }

    private void setupListeners() {
        clienteView.setBtnInsertarListener(v -> manejarInsercionCliente());
        clienteView.setBtnModificarListener(v -> manejarModificacionCliente());
        clienteView.setBtnBorrarListener(v -> manejarBorradoCliente());
        clienteView.setBtnAgregarListener(v -> manejarAgregarObjetivo());
        clienteView.setBtnSacarListener(v -> manejarSacarObjetivo());

        clienteView.setListViewClientesListener(this::manejarSeleccionCliente);
        clienteView.setListViewObjetivosClienteListener(this::manejarSeleccionObjetivo);

        clienteView.setSpinnerObjetivoListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clienteView.activarBotonAgregar(position > 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                clienteView.activarBotonAgregar(false);
            }
        });
    }

    private void cargarDatosIniciales() {
        cargarClientes();
        cargarObjetivosEnSpinner();
        resetearUI();
    }

    private void resetearUI() {
        clienteView.limpiarCampos();
        idClienteSeleccionado = -1;
        idObjetivoSeleccionado = -1;
    }
}