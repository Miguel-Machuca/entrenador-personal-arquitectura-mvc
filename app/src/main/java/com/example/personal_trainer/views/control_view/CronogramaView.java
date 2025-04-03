package com.example.personal_trainer.views.control_view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.personal_trainer.R;

import java.util.ArrayList;
import java.util.List;

public class CronogramaView {
    private EditText txtFecha;
    private Spinner spinnerCliente;
    private Spinner spinnerRutina;
    private Button btnInsertar;
    private Button btnModificar;
    private Button btnBorrar;
    private Button btnEnviarRutina;
    private Button btnEnviarEjemplos;
    private ListView listViewCronogramas;
    private ArrayAdapter<String> cronogramasAdapter;
    private Context context;

    public CronogramaView(Context context, View rootView) {
        this.context = context;
        initializeUI(rootView);
        setupAdapters();
    }

    private void initializeUI(View rootView) {
        txtFecha = rootView.findViewById(R.id.txt_fecha);
        spinnerCliente = rootView.findViewById(R.id.spinner_cliente);
        spinnerRutina = rootView.findViewById(R.id.spinner_rutina);
        btnInsertar = rootView.findViewById(R.id.btn_insertar_cronograma);
        btnModificar = rootView.findViewById(R.id.btn_modificar_cronograma);
        btnBorrar = rootView.findViewById(R.id.btn_borrar_cronograma);
        btnEnviarRutina = rootView.findViewById(R.id.btn_enviar_rutina_cronograma);
        btnEnviarEjemplos = rootView.findViewById(R.id.btn_enviar_ejercicios_cronograma);
        listViewCronogramas = rootView.findViewById(R.id.lv_items_cronograma);
    }

    private void setupAdapters() {
        cronogramasAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );
        listViewCronogramas.setAdapter(cronogramasAdapter);
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

    public void setBtnEnviarRutinaListener(View.OnClickListener listener) {
        btnEnviarRutina.setOnClickListener(listener);
    }

    public void setBtnEnviarEjemplosListener(View.OnClickListener listener) {
        btnEnviarEjemplos.setOnClickListener(listener);
    }

    public void setListViewCronogramasListener(ListView.OnItemClickListener listener) {
        listViewCronogramas.setOnItemClickListener(listener);
    }

    public void setSpinnerClienteListener(AdapterView.OnItemSelectedListener listener) {
        spinnerCliente.setOnItemSelectedListener(listener);
    }

    public void setSpinnerRutinaListener(AdapterView.OnItemSelectedListener listener) {
        spinnerRutina.setOnItemSelectedListener(listener);
    }

    // Métodos para manejar datos
    public String getFecha() {
        return txtFecha.getText().toString().trim();
    }

    public void setFecha(String fecha) {
        txtFecha.setText(fecha);
    }

    public int getPosicionClienteSeleccionado() {
        return spinnerCliente.getSelectedItemPosition();
    }

    public void setPosicionClienteSeleccionado(int posicion) {
        if(posicion >= 0 && posicion < spinnerCliente.getCount()) {
            spinnerCliente.setSelection(posicion);
        }
    }

    public int getPosicionRutinaSeleccionado() {
        return spinnerRutina.getSelectedItemPosition();
    }

    public void setPosicionRutinaSeleccionado(int posicion) {
        if(posicion >= 0 && posicion < spinnerRutina.getCount()) {
            spinnerRutina.setSelection(posicion);
        }
    }

    // Métodos para configurar spinners
    public void configurarSpinnerClientes(List<String> nombresClientes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                nombresClientes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(adapter);
    }

    public void configurarSpinnerRutinas(List<String> nombresRutinas) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                nombresRutinas
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRutina.setAdapter(adapter);
    }

    // Métodos para manejar la lista de cronogramas
    public void mostrarCronogramasEnListView(List<String> cronogramas) {
        cronogramasAdapter.clear();
        cronogramasAdapter.addAll(cronogramas);
        cronogramasAdapter.notifyDataSetChanged();
    }

    // Métodos para control de UI
    public void habilitarModoEdicion(boolean habilitar) {
        btnInsertar.setEnabled(!habilitar);
        btnModificar.setEnabled(habilitar);
        btnBorrar.setEnabled(habilitar);
    }

    public void limpiarCampos() {
        txtFecha.setText("");
        setPosicionClienteSeleccionado(0);
        setPosicionRutinaSeleccionado(0);
        habilitarModoEdicion(false);
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void activarBotonesEnvio(boolean activar) {
        btnEnviarRutina.setEnabled(activar);
        btnEnviarEjemplos.setEnabled(activar);
    }
}