package com.gruppo10.classi;

import java.io.Serializable;

/**
 * Rappresenta la sessione attiva di un utente autenticato.
 * Contiene sia l'istanza dell'utente corrente sia il token di autorizzazione
 * associato alla sessione.
 */
public class Sessione implements Serializable {
 
    /** Utente autenticato associato alla sessione. */
    private final Utente utente;

    /** Token di sessione usato per verificare i permessi sulle chiamate remote. */
    private final String token;
 
    /**
     * Costruisce una nuova sessione.
     *
     * @param utente utente autenticato
     * @param token token di sessione assegnato all'utente
     */
    public Sessione(Utente utente, String token) {
        this.utente = utente;
        this.token = token;
    }
 
    /**
     * Restituisce l'utente associato alla sessione.
     *
     * @return utente corrente
     */
    public Utente getUtente() {
        return utente;
    }
 
    /**
     * Restituisce il token di sessione.
     *
     * @return token dell'utente attualmente autenticato
     */
    public String getToken() {
        return token;
    }
}