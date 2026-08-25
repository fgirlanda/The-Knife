/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.database;

import java.sql.SQLException;

/** Segnala il tentativo di registrare uno username già presente. */
public class UsernameGiaEsistenteException extends SQLException {

    /**
     * Crea l'eccezione mantenendo la causa SQL del vincolo violato.
     *
     * @param causa eccezione SQL originale
     */
    public UsernameGiaEsistenteException(SQLException causa) {
        super("Username già in uso.", causa.getSQLState(),
                causa.getErrorCode(), causa);
    }
}
