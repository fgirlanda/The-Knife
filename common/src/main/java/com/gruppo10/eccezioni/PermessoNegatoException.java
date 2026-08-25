/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.eccezioni;

/**
 * Eccezione lanciata quando un utente non ha i permessi necessari per
 * eseguire un'operazione richiesta dal sistema.
 */
public class PermessoNegatoException extends Exception {
    /**
     * Costruisce l'eccezione con un messaggio esplicativo.
     *
     * @param message descrizione del motivo per cui il permesso è stato negato
     */
    public PermessoNegatoException(String message) {
        super(message);
    }
    
}
