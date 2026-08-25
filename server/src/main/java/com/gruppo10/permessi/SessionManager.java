/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.permessi;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.gruppo10.classi.Utente;

/** Gestisce in memoria le sessioni degli utenti autenticati. */
public class SessionManager {

    /** Associa ogni token di sessione all'utente autenticato. */
    private final Map<String, Utente> sessioni = new ConcurrentHashMap<>();
    /** Crea una nuova sessione per l'utente autenticato e restituisce il token. */
    public String generaToken(Utente utente) {
        String token = UUID.randomUUID().toString();
        sessioni.put(token, utente);
        return token;
    }
 
    /** Restituisce l'utente associato al token, se la sessione esiste ed e' valida. */
    public Optional<Utente> utenteDiSessione(String token) {
        return Optional.ofNullable(sessioni.get(token));
    }
 
    /** Invalida una sessione (logout). */
    public void invalidaSessione(String token) {
        sessioni.remove(token);
    }
 
    /** Restituisce gli utenti attualmente connessi, per il pannello admin. */
    public List<Utente> utentiConnessi() {
        return List.copyOf(sessioni.values());
    }
}

