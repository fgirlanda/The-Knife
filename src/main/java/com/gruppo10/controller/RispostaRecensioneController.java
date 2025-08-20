/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * Controller per la vista che consente a un ristoratore di rispondere a una recensione.
 * Gestisce l'interazione con il campo di testo per la risposta e l'invio della risposta stessa.
 */
public class RispostaRecensioneController extends BasicController{

    private Recensione recensione;

    private Runnable onCloseCallBack;

    @FXML
    private TextArea txtRisposta;

    @FXML
    private Button btnInvia;


    /**
     * Metodo di inizializzazione chiamato automaticamente dal framework JavaFX
     * dopo che tutti gli elementi FXML sono stati iniettati.
     * Aggiunge un listener al campo di testo per controllare lo stato e
     * abilitare/disabilitare il pulsante di invio.
     */
    @FXML
    private void initialize() {
        txtRisposta.textProperty().addListener((_, _, _) -> controllaCampi());
    }

    /**
     * Imposta la recensione a cui il ristoratore sta rispondendo.
     *
     * @param recensione la {@link Recensione} a cui rispondere.
     */
    public void setRecensione(Recensione recensione) {
        this.recensione = recensione;
    }

    /**
     * Controlla se il campo della risposta è vuoto.
     * Disabilita il pulsante di invio se non è stato inserito alcun testo.
     */
    private void controllaCampi() {
        boolean campiVuoti = txtRisposta.getText().isEmpty();
        disabilitaBottone(btnInvia, campiVuoti);
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Invia".
     * Salva la risposta nel file CSV e chiude la finestra di dialogo.
     *
     * @throws Exception se si verifica un errore durante il salvataggio su file.
     */
    @FXML
    private void aggiungiRisposta() throws Exception {

        String risposta = txtRisposta.getText();

        try {
            RecensioneCSV recensioneCSV = new RecensioneCSV();
            recensioneCSV.aggiungiRisposta(recensione, risposta);
        } catch (Exception e) {
            return;
        }

        if(onCloseCallBack != null){
            onCloseCallBack.run();
        }
        
        chiudi();
    }

    /**
     * Restituisce il testo inserito come risposta.
     *
     * @return la risposta inserita nel campo di testo.
     */
    public String getRisposta(){
        return this.txtRisposta.getText();
    }

    /**
     * Imposta una callback da eseguire quando la finestra viene chiusa.
     *
     * @param callback il {@link Runnable} da eseguire.
     */
    public void setOnCloseCallBack(Runnable callback){
        this.onCloseCallBack = callback;
    }

}