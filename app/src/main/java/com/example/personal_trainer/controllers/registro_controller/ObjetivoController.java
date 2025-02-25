package com.example.personal_trainer.controllers.registro_controller;

import com.example.personal_trainer.views.registro_view.ObjetivoView;
import com.example.personal_trainer.models.registro_model.ObjetivoModel;

public class ObjetivoController {
    ObjetivoModel objetivoModel;
    ObjetivoView objetivoView;

    public ObjetivoController(ObjetivoModel objetivoModel, ObjetivoView objetivoView) {
        this.objetivoModel = objetivoModel;
        this.objetivoView = objetivoView;
    }

    public void insertar() {
        try{
            objetivoModel.insertar( objetivoView );
        }catch (Exception e){
            mvcView.showErrorToast(e.getMessage());
        }
    }

}