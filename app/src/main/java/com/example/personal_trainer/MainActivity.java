package com.example.personal_trainer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.controllers.fitness_controller.EjercicioController;
import com.example.personal_trainer.controllers.registro_controller.ClienteController;
import com.example.personal_trainer.controllers.registro_controller.ObjetivoController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    Button btnGestionarEjercicio, btnGestionarObjetivo, btnGestionarCliente, btnGestionarRutina, btnGestionarCronograma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        context = getApplicationContext();

        btnGestionarEjercicio = findViewById(R.id.btn_gestionar_ejercicio);
        btnGestionarObjetivo = findViewById(R.id.btn_gestionar_objetivo);
        btnGestionarCliente = findViewById(R.id.btn_gestionar_cliente);
        btnGestionarRutina = findViewById(R.id.btn_gestionar_rutina);
        btnGestionarCronograma = findViewById(R.id.btn_gestionar_cronograma);

        btnGestionarEjercicio.setOnClickListener(this);
        btnGestionarObjetivo.setOnClickListener(this);
        btnGestionarCliente.setOnClickListener(this);
        btnGestionarRutina.setOnClickListener(this);
        btnGestionarCronograma.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent i;

        if (id == R.id.btn_gestionar_ejercicio) {
            i = new Intent(context, EjercicioController.class);

        } else if (id == R.id.btn_gestionar_objetivo) {
            i = new Intent(context, ObjetivoController.class);

        } else if (id == R.id.btn_gestionar_cliente) {
            i = new Intent(context, ClienteController.class);

        } else if (id == R.id.btn_gestionar_rutina) {

            // i = new Intent(context, PRutina.class);
            return;
        } else if (id == R.id.btn_gestionar_cronograma) {

            // i = new Intent(context, PCronograma.class);
            return;
        } else {
            return;
        }

        Bundle bolsa = new Bundle();
        bolsa.putInt("id", 0);
        i.putExtras(bolsa);
        startActivity(i);
    }
}