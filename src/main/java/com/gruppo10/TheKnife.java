package com.gruppo10;
import com.gruppo10.classi.UtenteReader;
import com.gruppo10.fileJava.Login;
import javafx.application.Application;

public class TheKnife {
    public static void main(String[] args) throws Exception {
        // Carica gli utenti dal file CSV
        UtenteReader utenteReader = new UtenteReader();
        utenteReader.caricaUtenti();
        Application.launch(Login.class, args);
        // Application.launch(Registrazione.class, args);
    }
}
