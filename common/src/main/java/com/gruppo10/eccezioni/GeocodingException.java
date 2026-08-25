package com.gruppo10.eccezioni;

/** Segnala un errore durante la geocodifica di un indirizzo. */
public class GeocodingException extends Exception {

    /**
     * Crea un'eccezione con il messaggio specificato.
     *
     * @param messaggio descrizione dell'errore di geocodifica
     */
    public GeocodingException(String messaggio) {
        super(messaggio);
    }

    /**
     * Crea un'eccezione associata alla causa originale dell'errore.
     *
     * @param messaggio descrizione dell'errore di geocodifica
     * @param causa eccezione che ha originato l'errore
     */
    public GeocodingException(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }
}