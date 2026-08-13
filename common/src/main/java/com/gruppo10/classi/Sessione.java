package com.gruppo10.classi;

import java.io.Serializable;

public class Sessione implements Serializable {
 
    private final Utente utente;
    private final String token;
 
    public Sessione(Utente utente, String token) {
        this.utente = utente;
        this.token = token;
    }
 
    public Utente getUtente() {
        return utente;
    }
 
    public String getToken() {
        return token;
    }
}