/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10;

import java.util.List;

import com.gruppo10.classi.Utente;
import com.gruppo10.classi.UtenteCSV;
import com.gruppo10.fileJava.Login;
import javafx.application.Application;

public class TheKnife {
    public static void main(String[] args) {
        // Carica gli utenti dal file CSV
        UtenteCSV utenteCSV = new UtenteCSV();
        List<Utente> listaUtenti = utenteCSV.caricaCSV();
        utenteCSV.creaMappa(listaUtenti);
        if(listaUtenti == null){
            System.err.println("Errore caricamento utenti");
        }else{
            Application.launch(Login.class, args);
        }
    }
}
