/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

/**
 * Classe utility per la gestione centralizzata delle eccezioni e la
 * visualizzazione di messaggi di errore all’utente tramite finestre di dialogo
 * JavaFX.
 *
 * <p>
 * La classe fornisce un metodo statico
 * {@link #errore(String, Exception, boolean, Consumer)}
 * che mostra un {@link Alert} di tipo {@code ERROR}. In caso di eccezione
 * non nulla, lo stack trace viene mostrato in un riquadro espandibile.
 * </p>
 *
 * <p>
 * Se richiesto, la finestra può includere un pulsante aggiuntivo per permettere
 * all’utente di selezionare un file alternativo, tramite una {@link Consumer}.
 * </p>
 *
 * <p>
 * Questa classe non è istanziabile in quanto pensata come contenitore di
 * metodi statici.
 * </p>
 *
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 1.0
 */
public class GestioneEccezioni {
    /**
     * Mostra un messaggio di errore all’utente tramite una finestra di dialogo
     * JavaFX.
     *
     * @param header  il testo da mostrare come intestazione del messaggio di
     *                errore.
     * @param e       l’eccezione associata (opzionale). Se non è {@code null},
     *                lo stack trace verrà mostrato in un riquadro espandibile.
     * @param bottone se {@code true}, viene aggiunto un pulsante "Scegli un altro
     *                file"
     *                che consente all’utente di selezionare un file alternativo.
     * @param azione  l’azione da eseguire sul file scelto (ovvero sostituire il
     *                file che ha causato l'eccezione) nel caso in cui
     *                l’utente selezioni un file alternativo. Ignorata se
     *                {@code bottone}
     *                è {@code false}.
     *
     * @implNote L’uso di questo metodo blocca il thread corrente fino alla chiusura
     *           della finestra di dialogo, poiché si basa su
     *           {@link Alert#showAndWait()}.
     */
    public static void errore(String header, Exception e, boolean bottone, Consumer<File> azione) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(header);

        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            TextArea textArea = new TextArea(sw.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setExpandableContent(textArea);
        }

        ButtonType scegliAltro = new ButtonType("Scegli un altro file");
        if (bottone)
            alert.getButtonTypes().add(scegliAltro);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == scegliAltro) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Scegli un file CSV");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                azione.accept(file);
            }
        }
    }
}
