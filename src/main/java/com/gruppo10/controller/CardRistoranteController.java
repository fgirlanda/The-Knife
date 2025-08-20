/*
 * Francesco Girlanda 760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.nio.file.Paths;

import com.gruppo10.classi.Card;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Controller per la card che visualizza un singolo ristorante. Implementa
 * l'interfaccia {@link Card} per gestire i dati di un ristorante e visualizzarli
 * in un formato di card nell'interfaccia utente. Gestisce anche l'azione di
 * clic sulla card per aprire la pagina dettagliata del ristorante.
 */
public class CardRistoranteController extends BasicController implements Card<Ristorante> {

    public Ristorante ristorante;

    @FXML
    private HBox card;

    @FXML
    private ImageView imgRistorante;

    @FXML
    private Text txtNomeRistorante;

    @FXML
    private Text txtRecensioni;

    @FXML
    private Text txtPrezzo;

    @FXML
    private Text txtTipoCucina;

    /**
     * Imposta un gestore di eventi per il clic sulla card.
     * Quando l'utente clicca sulla card, viene aperta la pagina dettagliata
     * del ristorante corrispondente.
     */
    public void setOnClick(){
        card.setOnMouseClicked(_ -> { // _ = event
            SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab);
        });
    }

    /**
     * Imposta l'oggetto ristorante e il contenitore associati a questa card.
     * Questo metodo è un'implementazione dell'interfaccia {@link Card}.
     *
     * @param ristorante il {@link Ristorante} da visualizzare nella card.
     * @param contenitore il {@link VBox} che contiene la card.
     */
    @Override
    public void setItem(Ristorante ristorante, VBox contenitore) {
        this.ristorante = ristorante;
    }

    /**
     * Imposta i dati dell'oggetto ristorante sulle etichette della card.
     * I dati includono l'immagine, il nome, la media delle recensioni,
     * il livello di prezzo e il tipo di cucina.
     */
    @Override
    public void setDati() {
        String cucina = this.ristorante.getTipoCucina().name();
        String path = Paths.get("images",cucina+".png").toString();
        Image immagine = new Image(path);
        imgRistorante.setImage(immagine);
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtRecensioni.setText(String.format("%.1f", this.ristorante.getMediaRec()) + " ★" + " ("
                + this.ristorante.getNumeroRecensioni() + " Recensione/i)");
        txtPrezzo.setText(this.ristorante.getPrezzo().toString());
        txtTipoCucina.setText(cucina);
    }
}