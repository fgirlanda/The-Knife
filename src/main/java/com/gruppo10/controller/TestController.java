/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import com.gruppo10.classi.GestioneEccezioni;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class TestController extends BasicController{
    @FXML
    private ToggleGroup ruoloGroup;

    @FXML
    public void accedi() {
        Utente utente = creaUtenteTest();
        LoginController.utenteLoggato = utente;
        try {
            SceneManager.apriPaginaPrincipale(stage);
        } catch (Exception e) {
            GestioneEccezioni.errore("Errore caricamento pagina principale", e, false, null);
        }
    }

    private Utente creaUtenteTest() {
        Utente utente = new Utente();
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setCords(45.5, 9.4);
        utente.setId(404);
        utente.setDataDiNascita("08-02-1999");
        utente.setIndirizzo("Via Santa Maria 8, Casale Litta");
        utente.setPassword("passwordTest");
        utente.setRuolo(getRuolo());
        utente.setUsername("usernameTest");

        return utente;
    }

    private Ruolo getRuolo() {
        RadioButton select = (RadioButton) ruoloGroup.getSelectedToggle();
        Ruolo ruolo;
        if (select.getText().equals("CLIENTE")) {
            ruolo = Ruolo.CLIENTE;
        } else if (select.getText().equals("RISTORATORE")) {
            ruolo = Ruolo.RISTORATORE;
        } else {
            ruolo = Ruolo.NON_REGISTRATO;
        }

        return ruolo;
    }
}
