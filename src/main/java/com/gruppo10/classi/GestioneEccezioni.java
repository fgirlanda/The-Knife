package com.gruppo10.classi;

import com.gruppo10.controller.StatusController;

public class GestioneEccezioni {
    public static void errore(String errore){
        SceneManager.finestraDialogo("/GUI/status.fxml", "Errore", null, (StatusController controller) -> controller.setTesto(errore)); 
    }
}
