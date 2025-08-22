/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10;

import javafx.application.Application;
/**Classe di lancio dell'applicazione TheKnife. */
public class Main {
    /**
     * Punto di ingresso dell'applicazione.
     * <p>
     * Questo metodo richiama {@link javafx.application.Application#launch(String...)} per avviare
     * l'applicazione JavaFX.
     * </p>
     *
     * @param args gli argomenti della riga di comando passati all'applicazione
     */
    public static void main(String[] args) {
        try {
            // reindirizza stdout e stderr su file
            System.setOut(new java.io.PrintStream(new java.io.FileOutputStream("data/log/log.log", true)));
            System.setErr(new java.io.PrintStream(new java.io.FileOutputStream("data/log/log.log", true)));
            Application.launch(TheKnife.class, args);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
