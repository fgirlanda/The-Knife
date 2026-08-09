package com.gruppo10.eccezioni;

/** Segnala un errore durante la geocodifica di un indirizzo. */
public class GeocodingException extends Exception {

    public GeocodingException(String messaggio) {
        super(messaggio);
    }

    public GeocodingException(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }
}