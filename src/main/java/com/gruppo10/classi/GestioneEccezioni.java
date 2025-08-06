package com.gruppo10.classi;

import com.gruppo10.controller.StatusController;

public class GestioneEccezioni {
    public static Object errore(String errore){
        SceneManager.finestraDialogo("/GUI/status.fxml", "Errore", null, (StatusController controller) -> controller.setTesto(errore)); 
        return null;
    }
}
