/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.database;

import java.sql.SQLException;

/**
 * Segnala il tentativo di inserire una seconda recensione dello stesso cliente
 * per lo stesso ristorante.
 */
public class RecensioneDuplicataException extends SQLException {

    /**
     * Crea l'eccezione mantenendo la causa SQL del vincolo violato.
     *
     * @param causa eccezione SQL originale
     */
    public RecensioneDuplicataException(SQLException causa) {
        super("Hai già recensito questo ristorante.", causa.getSQLState(),
                causa.getErrorCode(), causa);
    }
}
