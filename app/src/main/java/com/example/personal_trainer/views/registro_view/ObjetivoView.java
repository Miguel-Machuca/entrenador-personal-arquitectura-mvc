package com.example.personal_trainer.views.registro_view;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.personal_trainer.R;

import java.util.List;

public class ObjetivoView {
    private EditText txtNombreObjetivo;
    private ListView listViewObjetivos;
    private Button btnInsertar;
    private Button btnModificar;
    private Button btnBorrar;
    private Context context;

    public ObjetivoView(Context context, View rootView) {
        this.context = context;
        initializeUI(rootView);
    }

    private void initializeUI(View rootView) {
        txtNombreObjetivo = rootView.findViewById(R.id.txt_nombre_objetivo);
        btnInsertar = rootView.findViewById(R.id.btn_insertar_objetivo);
        btnModificar = rootView.findViewById(R.id.btn_modificar_objetivo);
        btnBorrar = rootView.findViewById(R.id.btn_borrar_objetivo);
        listViewObjetivos = rootView.findViewById(R.id.listView_objetivos);
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

    public void setListViewObjetivosListener(ListView.OnItemClickListener listener) {
        listViewObjetivos.setOnItemClickListener(listener);
    }

    public void mostrarObjetivosEnListView(List<String> nombres) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombres);
        listViewObjetivos.setAdapter(adapter);
    }

    public String getNombreObjetivo() {
        return txtNombreObjetivo.getText().toString().trim();
    }

    public void setNombreObjetivo(String nombre) {
        txtNombreObjetivo.setText(nombre);
    }

    public void habilitarModoEdicion(boolean habilitar) {
        btnInsertar.setEnabled(!habilitar);
        btnModificar.setEnabled(habilitar);
        btnBorrar.setEnabled(habilitar);
    }

    public void limpiarCampos() {
        txtNombreObjetivo.setText("");
        habilitarModoEdicion(false);
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}