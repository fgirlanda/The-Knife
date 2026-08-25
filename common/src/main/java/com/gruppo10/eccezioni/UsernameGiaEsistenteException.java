package com.gruppo10.eccezioni;

import java.sql.SQLException;

/** Segnala il tentativo di registrare uno username già presente. */
public class UsernameGiaEsistenteException extends SQLException {

    /**
     * Crea un'eccezione per un conflitto di username, preservandone la causa.
     *
     * @param causa eccezione SQL che descrive il vincolo violato
     */
    public UsernameGiaEsistenteException(SQLException causa) {
        super("Username già in uso.", causa.getSQLState(),
                causa.getErrorCode(), causa);
    }
}