/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

public class GestioneEccezioni {
    public static void errore(String header, String errore, boolean bottone, Consumer<File> azione) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(header);
        alert.setContentText(errore);

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
