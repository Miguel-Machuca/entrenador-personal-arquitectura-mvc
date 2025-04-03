package com.example.personal_trainer.controllers.control_controller;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.R;
import com.example.personal_trainer.models.control_model.CronogramaModel;
import com.example.personal_trainer.models.fitness_model.EjercicioModel;
import com.example.personal_trainer.models.fitness_model.RutinaModel;
import com.example.personal_trainer.models.registro_model.ClienteModel;
import com.example.personal_trainer.views.control_view.CronogramaView;
import com.example.personal_trainer.views.fitness_view.RutinaView;

public class CronogramaController extends AppCompatActivity {
    private CronogramaView cronogramaView;
    private CronogramaModel cronogramaModel;
    private ClienteModel clienteModel;
    private RutinaModel rutinaModel;
    private int idCronogramaSeleccionado = -1;
    private int idClienteSeleccionado = -1;
    private int idRutinaSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcronograma);

        cronogramaModel = new CronogramaModel();
        cronogramaModel.initBD(this);
        clienteModel = new ClienteModel();
        clienteModel.initBD(this);
        rutinaModel = new RutinaModel();
        rutinaModel.initBD(this);


        View rootView = findViewById(android.R.id.content);
        cronogramaView = new CronogramaView(this, rootView);

        setupListeners();

        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {

    }

    private void setupListeners() {

    }


}
