package com.example.personal_trainer.views.registro_view;

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

public class ClienteView {
    private EditText txtNombreCliente;
    private EditText txtApellidoCliente;
    private EditText txtCelular;
    private ListView listViewClientes;
    private Spinner spinnerObjetivo;
    private Button btnInsertar;
    private Button btnModificar;
    private Button btnBorrar;
    private Button btnAgregar;
    private Button btnSacar;
    private ListView listViewObjetivosCliente;
    private EditText txtDescripcion;
    private ArrayAdapter<String> objetivosClienteAdapter;
    private Context context;

    public ClienteView(Context context, View rootView) {
        this.context = context;
        initializeUI(rootView);
        setupAdapters();
    }

    public void setBtnInsertarListener(View.OnClickListener listener) {
        btnInsertar.setOnClickListener(listener);
    }

    public void setBtnModificarListener(View.OnClickListener listener) {
        btnModificar.setOnClickListener(listener);
    }

    public void setBtnBorrarListener(View.OnClickListener listener) {
        btnBorrar.setOnClickListener(listener);
    }

    public void setBtnAgregarListener(View.OnClickListener listener) {
        btnAgregar.setOnClickListener(listener);
    }

    public void setBtnSacarListener(View.OnClickListener listener) {
        btnSacar.setOnClickListener(listener);
    }

    public void setListViewClientesListener(ListView.OnItemClickListener listener) {
        listViewClientes.setOnItemClickListener(listener);
    }

    public void setListViewObjetivosClienteListener(ListView.OnItemClickListener listener) {
        listViewObjetivosCliente.setOnItemClickListener(listener);
    }

    public void setSpinnerObjetivoListener(AdapterView.OnItemSelectedListener listener) {
        spinnerObjetivo.setOnItemSelectedListener(listener);
    }

    public void mostrarClientesEnListView(List<String> nombres) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombres);
        listViewClientes.setAdapter(adapter);
    }

    public void configurarSpinnerObjetivos(List<String> nombresObjetivos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                nombresObjetivos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerObjetivo.setAdapter(adapter);
    }
    private void initializeUI(View rootView) {
        txtNombreCliente = rootView.findViewById(R.id.txt_nombre_cliente);
        txtApellidoCliente = rootView.findViewById(R.id.txt_apellido_cliente);
        txtCelular = rootView.findViewById(R.id.txt_celular_cliente);
        btnInsertar = rootView.findViewById(R.id.btn_insertar_cliente);
        btnModificar = rootView.findViewById(R.id.btn_modificar_cliente);
        btnBorrar = rootView.findViewById(R.id.btn_borrar_cliente);
        listViewClientes = rootView.findViewById(R.id.listView_clientes);
        spinnerObjetivo = rootView.findViewById(R.id.spinner_objetivo);
        btnAgregar = rootView.findViewById(R.id.btn_agregar_objetivo);
        btnSacar = rootView.findViewById(R.id.btn_sacar_objetivo);
        txtDescripcion = rootView.findViewById(R.id.txt_descripcion);
        listViewObjetivosCliente = rootView.findViewById(R.id.listView_objetivo_cliente);
    }

    private void setupAdapters() {
        configurarAdaptadorListaObjetivos(
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );
    }

    public String getNombreCliente() {
        return txtNombreCliente.getText().toString().trim();
    }

    public String getApellidoCliente() {
        return txtApellidoCliente.getText().toString().trim();
    }

    public String getCelular() {
        return txtCelular.getText().toString().trim();
    }

    public String getDescripcion() {
        return txtDescripcion.getText().toString();
    }

    public int getPosicionObjetivoSeleccionado() {
        return spinnerObjetivo.getSelectedItemPosition();
    }

    public void setNombreCliente(String nombre) {
        txtNombreCliente.setText(nombre);
    }

    public void setApellidoCliente(String apellido) {
        txtApellidoCliente.setText(apellido);
    }

    public void setCelular(String celular) {
        txtCelular.setText(celular);
    }

    public void setPosicionObjetivoSeleccionado(int posicion) {
        if(posicion >= 0 && posicion < spinnerObjetivo.getCount()) {
            spinnerObjetivo.setSelection(posicion);
        }
    }

    public void agregarObjetivoALista(String objetivo) {
        objetivosClienteAdapter.add(objetivo);
        objetivosClienteAdapter.notifyDataSetChanged();
    }

    public void removerObjetivoDeLista(int position) {
        if(position >= 0 && position < objetivosClienteAdapter.getCount()) {
            objetivosClienteAdapter.remove(objetivosClienteAdapter.getItem(position));
            objetivosClienteAdapter.notifyDataSetChanged();
        }
    }

    public List<String> getObjetivosCliente() {
        List<String> objetivos = new ArrayList<>();
        for(int i = 0; i < objetivosClienteAdapter.getCount(); i++) {
            objetivos.add(objetivosClienteAdapter.getItem(i));
        }
        return objetivos;
    }

    public void limpiarObjetivosCliente() {
        objetivosClienteAdapter.clear();
        objetivosClienteAdapter.notifyDataSetChanged();
    }

    public void activarBotonAgregar(boolean activar) {
        btnAgregar.setEnabled(activar);
    }

    public void activarBotonSacar(boolean activar) {
        btnSacar.setEnabled(activar);
    }

    public void habilitarModoEdicion(boolean habilitar) {
        btnInsertar.setEnabled(!habilitar);
        btnModificar.setEnabled(habilitar);
        btnBorrar.setEnabled(habilitar);
    }

    public void limpiarCampos() {
        txtNombreCliente.setText("");
        txtApellidoCliente.setText("");
        txtCelular.setText("");
        txtDescripcion.setText("");
        habilitarModoEdicion(false);
        limpiarObjetivosCliente();
        setPosicionObjetivoSeleccionado(0);
        activarBotonAgregar(false);
        activarBotonSacar(false);
    }

    public void limpiarDescripcion() {
        txtDescripcion.setText("");
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void configurarAdaptadorListaObjetivos(int layoutResId, List<String> datosIniciales) {
        objetivosClienteAdapter = new ArrayAdapter<>(
                context,
                layoutResId,
                datosIniciales
        );
        listViewObjetivosCliente.setAdapter(objetivosClienteAdapter);
    }
}