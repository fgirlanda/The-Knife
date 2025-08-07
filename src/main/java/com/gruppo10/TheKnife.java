package com.gruppo10;

import com.gruppo10.classi.UtenteReader;
import com.gruppo10.fileJava.Login;
import javafx.application.Application;

public class TheKnife {
    public static void main(String[] args) {
        // Carica gli utenti dal file CSV
        if (UtenteReader.caricaCSV())
            Application.launch(Login.class, args);
        // Application.launch(Registrazione.class, args);
    }
}
