package com.example.personal_trainer.controllers.fitness_controller;

import com.example.personal_trainer.models.fitness_model.EjercicioModel;
import com.example.personal_trainer.views.fitness_view.EjercicioView;

public class EjercicioController {
    private EjercicioModel ejercicioModel;
    private EjercicioView ejercicioView;

    public EjercicioController(EjercicioModel ejercicioModel, EjercicioView ejercicioView) {
        this.ejercicioModel = ejercicioModel;
        this.ejercicioView = ejercicioView;
    }

    public void insertar() {
        try {
           // EjercicioModel ejercicio = new EjercicioModel(ejercicioView, ejercicioView);
            //ejercicioModel.insertar(ejercicio);
            //ejercicioView.mostrarMensaje("Cliente agregado correctamente.");
        } catch (Exception e) {
            //ejercicioView.mostrarMensaje("Error al agregar cliente: " + e.getMessage());
        }
    }

    public void consultar() {
        try {
            //List<Cliente> clientes = clienteDAO.obtenerClientes();
            //clienteView.mostrarClientes(clientes);
        } catch (Exception e) {
            //clienteView.mostrarMensaje("Error al listar clientes: " + e.getMessage());
        }
    }
}
