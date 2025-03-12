package com.example.personal_trainer.views.registro_view;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.personal_trainer.R;

import java.util.ArrayList;
import java.util.List;

public class ObjetivoView {

    private int idObjetivo;
    private EditText txtNombreObjetivo;
    private ListView listViewObjetivos;
    private Button btnInsertar, btnModificar, btnBorrar;
    private Context context;


    public ObjetivoView(Context context, View rootView) {
        this.context = context;
        this.idObjetivo = -1;
        txtNombreObjetivo = rootView.findViewById(R.id.txt_nombre_objetivo);
        btnInsertar = rootView.findViewById(R.id.btn_insertar_objetivo);
        btnModificar = rootView.findViewById(R.id.btn_modificar_objetivo);
        btnBorrar = rootView.findViewById(R.id.btn_borrar_objetivo);
        listViewObjetivos = rootView.findViewById(R.id.listView_objetivos);
    }

    public void setBtnInsertarListener(Runnable listener) {
        btnInsertar.setOnClickListener(v -> listener.run());
    }

    public void setBtnModificarListener(Runnable listener) {
        btnModificar.setOnClickListener(v -> listener.run());
    }

    public void setBtnBorrarListener(Runnable listener) {
        btnBorrar.setOnClickListener(v -> listener.run());
    }

    public void mostrarObjetivosEnListView(List<String> nombres) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombres);
        listViewObjetivos.setAdapter(adapter);
    }

    public void setListViewObjetivosListener(ListView.OnItemClickListener listener) {
        listViewObjetivos.setOnItemClickListener(listener);
    }

    public int getIdObjetivo(){
        return idObjetivo;
    }

    public void setIdObjetivo(int idObjetivo) {
        this.idObjetivo = idObjetivo;
    }
    public String getNombreObjetivo() {
        return txtNombreObjetivo.getText().toString().trim();
    }

    public void setNombreObjetivo(String nombre) {
        txtNombreObjetivo.setText(nombre);
    }

    public void limpiarCampos(){
        txtNombreObjetivo.setText("");
        btnInsertar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
    }

    public void btnModificarBtnBorrar(){
        btnInsertar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnBorrar.setEnabled(true);
    }

    public void mensaje(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}