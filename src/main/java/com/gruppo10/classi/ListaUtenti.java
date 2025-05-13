package com.gruppo10.classi;

import java.util.ArrayList;
import lombok.Data;

@Data
public class ListaUtenti {
    private ArrayList<Utente> utenti;

    public void addUtente(Utente utente) {
        this.utenti.add(utente);
    }

    public void removeUtente(Utente utente) {
        this.utenti.remove(utente);
    }
}
