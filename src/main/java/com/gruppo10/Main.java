/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10;

import com.gruppo10.classi.BlockTeeStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javafx.application.Application;

/** Classe di lancio dell'applicazione TheKnife. */
/**
 * Punto di ingresso dell'applicazione.
 * <p>
 * Questo metodo richiama
 * {@link javafx.application.Application#launch(String...)} per avviare
 * l'applicazione JavaFX.
 * </p>
 * <p>
 * Crea anche un file log, tramite {@link com.gruppo10.classi.BlockTeeStream} in
 * cui scrive gli eventuali
 * messaggi di errore
 * sollevati durante l'esecuzione.
 * </p>
 *
 * @param args gli argomenti della riga di comando passati all'applicazione
 */

public class Main {
    public static void main(String[] args) {
        try {
            FileOutputStream fos = new FileOutputStream("data/log/events.log", true);

            PrintStream logOut = new PrintStream(new BlockTeeStream(System.out, fos), true);
            PrintStream logErr = new PrintStream(new BlockTeeStream(System.err, fos), true);

            System.setOut(logOut);
            System.setErr(logErr);

            System.out.println("=== Avvio applicazione ===");

            Application.launch(TheKnife.class, args);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
