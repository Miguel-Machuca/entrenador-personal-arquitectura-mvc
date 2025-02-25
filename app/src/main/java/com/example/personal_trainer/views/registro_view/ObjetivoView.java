package com.example.personal_trainer.views.registro_view;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ObjetivoView extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNombreObjetivo;
    private Button btnInsertar, btnModificar, btnBorrar;
    private ListView listViewConsultar;


    public EditText getTxtNombreObjetivo() {
        return txtNombreObjetivo;
    }

    public void setTxtNombreObjetivo(EditText txtNombreObjetivo) {
        this.txtNombreObjetivo = txtNombreObjetivo;
    }

    public Button getBtnInsertar() {
        return btnInsertar;
    }

    public void setBtnInsertar(Button btnInsertar) {
        this.btnInsertar = btnInsertar;
    }

    public Button getBtnModificar() {
        return btnModificar;
    }

    public void setBtnModificar(Button btnModificar) {
        this.btnModificar = btnModificar;
    }

    public Button getBtnBorrar() {
        return btnBorrar;
    }

    public void setBtnBorrar(Button btnBorrar) {
        this.btnBorrar = btnBorrar;
    }

    public ListView getListViewConsultar() {
        return listViewConsultar;
    }

    public void setListViewConsultar(ListView listViewConsultar) {
        this.listViewConsultar = listViewConsultar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vobjetivo);
        iniciar();
    }

    public void iniciar() {
        context = this;
        txtNombreObjetivo = findViewById(R.id.txt_nombre_objetivo);
        btnInsertar = findViewById(R.id.btn_insertar_objetivo);
        btnModificar = findViewById(R.id.btn_modificar_objetivo);
        btnBorrar = findViewById(R.id.btn_borrar_objetivo);
        listViewConsultar = findViewById(R.id.lv_items_objetivo);

        //nObjetivo.iniciarBD(this);

        configurarBotones();

        //consultarObjetivos();


    }

    private void configurarBotones() {
        btnInsertar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {

    }
}
