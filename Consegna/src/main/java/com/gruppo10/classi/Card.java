/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Interfaccia generica che rappresenta una card grafica contenente un
 * CardController di tipo {@code Ristorante} oppure {@code Recensione} .
 *
 * <p>
 * Le implementazioni di {@code Card} sono responsabili della gestione dei dati,
 * della loro visualizzazione e dell’integrazione con la scena JavaFX tramite
 * uno {@link Stage}.
 * </p>
 * @param <T> il tipo di oggetto di riferimento per la card: {@link Ristorante} oppure {@link Recensione}
 *
 */
public interface Card<T> {

    /**
     * Imposta lo stage principale in cui la card verrà visualizzata.
     *
     * @param stage stage di riferimento per la card
     */
    void setStage(Stage stage);

    /**
     * Aggiorna o inizializza i dati interni della card.
     */
    void setDati();

    /**
     * Imposta l'elemento specifico da visualizzare nella card.
     *
     * @param item        oggetto di tipo {@code T} da visualizzare nella card
     * @param contenitore {@link VBox} in cui aggiungere la card
     */
    void setItem(T item, VBox contenitore);
}
