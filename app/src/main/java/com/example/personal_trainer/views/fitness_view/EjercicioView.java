package com.example.personal_trainer.views.fitness_view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.R;

import java.util.List;

public class EjercicioView {

    private int idEjercicio;
    private EditText txtNombreEjercicio;
    private ImageView ivImagenEjercicio;
    private ListView listViewEjercicios;
    private Button btnInsertar, btnModificar, btnBorrar, btnSubirImagen;
    private Uri imagenSeleccionadaUri;
    private Context context;

    private ActivityResultLauncher<Intent> seleccionarImagenLauncher;

    public EjercicioView(Context context, View rootView, AppCompatActivity activity) {
        this.context = context;
        this.idEjercicio = -1;
        txtNombreEjercicio = rootView.findViewById(R.id.txt_nombre_ejercicio);
        btnInsertar = rootView.findViewById(R.id.btn_insertar_ejercicio);
        btnModificar = rootView.findViewById(R.id.btn_modificar_ejercicio);
        btnBorrar = rootView.findViewById(R.id.btn_borrar_ejercicio);
        btnSubirImagen = rootView.findViewById(R.id.btn_subir_imagen);
        listViewEjercicios = rootView.findViewById(R.id.lv_items_ejercicio);
        ivImagenEjercicio = rootView.findViewById(R.id.iv_imagen_ejercicio);

        configurarSeleccionarImagenLauncher(activity);
        configurarBotonSubirImagen();
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

    public void mostrarEjerciciosEnListView(List<String> nombres) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombres);
        listViewEjercicios.setAdapter(adapter);
    }

    public void setListViewEjerciciosListener(ListView.OnItemClickListener listener) {
        listViewEjercicios.setOnItemClickListener(listener);
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getNombreEjercicio() {
        return txtNombreEjercicio.getText().toString().trim();
    }

    public void setNombreEjercicio(String nombre) {
        txtNombreEjercicio.setText(nombre);
    }

    public String getUrlImagen() {
        return (imagenSeleccionadaUri != null) ? imagenSeleccionadaUri.toString() : "";
    }

    public void setUrlImagen(String urlImagen) {
        if (urlImagen != null && !urlImagen.isEmpty()) {
            imagenSeleccionadaUri = Uri.parse(urlImagen);
            ivImagenEjercicio.setImageURI(imagenSeleccionadaUri);
        }
    }

    private void configurarSeleccionarImagenLauncher(AppCompatActivity activity) {
        seleccionarImagenLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                        imagenSeleccionadaUri = result.getData().getData();
                        ivImagenEjercicio.setImageURI(imagenSeleccionadaUri);
                    }
                }
        );
    }

    private void configurarBotonSubirImagen() {
        btnSubirImagen.setOnClickListener(v -> seleccionarImagen());
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        seleccionarImagenLauncher.launch(intent);
    }

    public void limpiarCampos() {
        txtNombreEjercicio.setText("");
        imagenSeleccionadaUri = null;
        ivImagenEjercicio.setImageURI(null);
        btnInsertar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
    }

    public void btnModificarBtnBorrar() {
        btnInsertar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnBorrar.setEnabled(true);
    }

    public void mensaje(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}