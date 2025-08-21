/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
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
 * l'interfaccia {@link Card} per gestire i dati di un ristorante e
 * visualizzarli nella card. Gestisce anche l'azione di
 * clic sulla card per aprire la pagina del ristorante.
 */
public class CardRistoranteController extends BasicController implements Card<Ristorante> {

    /** L'oggetto {@link Ristorante} associato a questa card. */
    public Ristorante ristorante;

    /** Contenitore principale della card (HBox). */
    @FXML
    private HBox card;

    /** Immagine rappresentativa del ristorante. */
    @FXML
    private ImageView imgRistorante;

    /** Testo che mostra il nome del ristorante. */
    @FXML
    private Text txtNomeRistorante;

    /**
     * Testo che mostra la media delle recensioni e il numero totale di recensioni.
     */
    @FXML
    private Text txtRecensioni;

    /** Testo che mostra la fascia di prezzo del ristorante. */
    @FXML
    private Text txtPrezzo;

    /** Testo che mostra il tipo di cucina del ristorante. */
    @FXML
    private Text txtTipoCucina;

    /**
     * Imposta un gestore di eventi per il clic sulla card.
     * Quando l'utente clicca sulla card, viene aperta la pagina dettagliata
     * del ristorante corrispondente.
     */
    public void setOnClick() {
        card.setOnMouseClicked(_ -> { // _ = event
            SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab);
        });
    }

    /**
     * Imposta l'oggetto ristorante e il contenitore associati a questa card.
     * Questo metodo è un'implementazione dell'interfaccia {@link Card}.
     *
     * @param ristorante  il {@link Ristorante} da visualizzare nella card.
     * @param contenitore il {@link VBox} che contiene la card.
     */
    @Override
    public void setItem(Ristorante ristorante, VBox contenitore) {
        this.ristorante = ristorante;
    }

    /**
     * Imposta i dati dell'oggetto ristorante sulle etichette della card.
     * I dati includono l'immagine, il nome, la media delle recensioni,
     * la fascia di prezzo e il tipo di cucina.
     */
    @Override
    public void setDati() {
        String cucina = this.ristorante.getTipoCucina().name();
        String path = Paths.get("images", cucina + ".png").toString();
        Image immagine = new Image(path);
        imgRistorante.setImage(immagine);
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtRecensioni.setText(String.format("%.1f", this.ristorante.getMediaRec()) + " ★" + " ("
                + this.ristorante.getNumeroRecensioni() + " Recensione/i)");
        txtPrezzo.setText(this.ristorante.getPrezzo().toString());
        txtTipoCucina.setText(cucina);
    }
}