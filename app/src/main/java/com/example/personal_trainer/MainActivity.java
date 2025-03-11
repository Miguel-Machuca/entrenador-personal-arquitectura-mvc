package com.example.personal_trainer;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.controllers.registro_controller.ObjetivoController;

import com.example.personal_trainer.views.fitness_view.EjercicioView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    Button btnGestionarEjercicio, btnGestionarObjetivo, btnGestionarCliente, btnGestionarRutina, btnGestionarCronograma;
    int id = 0;

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
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_gestionar_ejercicio) {
            Intent i = new Intent(context, EjercicioView.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        } else if (id == R.id.btn_gestionar_objetivo) {
            Intent i = new Intent(context, ObjetivoController.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        }/* else if (id == R.id.btn_gestionar_cliente) {

            Intent i = new Intent(context, PCliente.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        } else if (id == R.id.btn_gestionar_rutina) {

            Intent i = new Intent(context, PRutina.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        } else if (id == R.id.btn_gestionar_cronograma) {

            Intent i = new Intent(context, PCronograma.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        }*/
    }
}