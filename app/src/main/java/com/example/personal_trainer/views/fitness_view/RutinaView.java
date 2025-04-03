package com.example.personal_trainer.views.fitness_view;

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

public class RutinaView {
    private EditText txtNombreRutina;
    private ListView listViewRutinas;
    private ListView listViewEjercicios;
    private Spinner spinnerEjercicios;
    private Button btnInsertar;
    private Button btnModificar;
    private Button btnBorrar;
    private Button btnAgregar;
    private Button btnSacar;
    private ListView listViewEjerciciosRutina;
    private EditText txtCantidadSerie;
    private EditText txtCantidadRepeticion;
    private EditText txtDuracionReposo;
    private ArrayAdapter<String> ejerciciosRutinaAdapter;
    private Context context;

    public RutinaView(Context context, View rootView) {
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

    public void setListViewEjerciciosListener(ListView.OnItemClickListener listener) {
        listViewEjercicios.setOnItemClickListener(listener);
    }

    public void setListViewEjerciciosRutinaListener(ListView.OnItemClickListener listener) {
        listViewEjerciciosRutina.setOnItemClickListener(listener);
    }

    public void setSpinnerEjercicioListener(AdapterView.OnItemSelectedListener listener) {
        spinnerEjercicios.setOnItemSelectedListener(listener);
    }

    public void mostrarRutinasEnListView(List<String> nombres) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombres);
        listViewRutinas.setAdapter(adapter);
    }

    public void configurarSpinnerEjercicios(List<String> nombresEjercicios) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                nombresEjercicios
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEjercicios.setAdapter(adapter);
    }
    private void initializeUI(View rootView) {
        txtNombreRutina = rootView.findViewById(R.id.txt_nombre_rutina);
        btnInsertar = rootView.findViewById(R.id.btn_insertar_rutina);
        btnModificar = rootView.findViewById(R.id.btn_modificar_rutina);
        btnBorrar = rootView.findViewById(R.id.btn_borrar_rutina);
        listViewRutinas = rootView.findViewById(R.id.listView_rutinas);
        spinnerEjercicios = rootView.findViewById(R.id.spinner_ejercicio);
        btnAgregar = rootView.findViewById(R.id.btn_agregar_ejercicio);
        btnSacar = rootView.findViewById(R.id.btn_sacar_ejercicio);
        txtCantidadSerie = rootView.findViewById(R.id.txt_cantidad_serie);
        txtCantidadRepeticion = rootView.findViewById(R.id.txt_cantidad_repeticion);
        txtDuracionReposo = rootView.findViewById(R.id.txt_duracion_reposo);
        listViewEjerciciosRutina = rootView.findViewById(R.id.listView_ejercicio_rutina);

    }

    private void setupAdapters() {
        configurarAdaptadorListaEjercicios(
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );
    }

    public String getNombreRutina() {
        return txtNombreRutina.getText().toString().trim();
    }

    public void setNombreRutina(String nombre) {
        txtNombreRutina.setText(nombre);
    }

    public int getCantidadSerie() {
        try {
            return Integer.parseInt(txtCantidadSerie.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getCantidadRepeticion() {
        try {
            return Integer.parseInt(txtCantidadRepeticion.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getDuracionReposo() {
        try {
            return Integer.parseInt(txtDuracionReposo.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getPosicionEjercicioSeleccionado() {
        return spinnerEjercicios.getSelectedItemPosition();
    }

    public void setPosicionEjercicioSeleccionado(int posicion) {
        if(posicion >= 0 && posicion < spinnerEjercicios.getCount()) {
            spinnerEjercicios.setSelection(posicion);
        }
    }

    public void agregarEjercicioALista(String ejercicio) {
        ejerciciosRutinaAdapter.add(ejercicio);
        ejerciciosRutinaAdapter.notifyDataSetChanged();
    }

    public void removerEjercicioDeLista(int position) {
        if(position >= 0 && position < ejerciciosRutinaAdapter.getCount()) {
            ejerciciosRutinaAdapter.remove(ejerciciosRutinaAdapter.getItem(position));
            ejerciciosRutinaAdapter.notifyDataSetChanged();
        }
    }

    public List<String> getEjerciciosRutina() {
        List<String> ejercicios = new ArrayList<>();
        for(int i = 0; i < ejerciciosRutinaAdapter.getCount(); i++) {
            ejercicios.add(ejerciciosRutinaAdapter.getItem(i));
        }
        return ejercicios;
    }

    public void limpiarEjerciciosRutina() {
        ejerciciosRutinaAdapter.clear();
        ejerciciosRutinaAdapter.notifyDataSetChanged();
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
        txtNombreRutina.setText("");
        habilitarModoEdicion(false);
        limpiarEjerciciosRutina();
        setPosicionEjercicioSeleccionado(0);
        activarBotonAgregar(false);
        activarBotonSacar(false);
    }

    public void limpiarDescripcion() {
        txtCantidadSerie.setText("");
        txtCantidadRepeticion.setText("");
        txtDuracionReposo.setText("");
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void configurarAdaptadorListaEjercicios(int layoutResId, List<String> datosIniciales) {
        ejerciciosRutinaAdapter = new ArrayAdapter<>(
                context,
                layoutResId,
                datosIniciales
        );
        listViewEjerciciosRutina.setAdapter(ejerciciosRutinaAdapter);
    }

    public void setListViewRutinasListener(ListView.OnItemClickListener listener) {
        listViewRutinas.setOnItemClickListener(listener);
    }
}
