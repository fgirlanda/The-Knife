package com.gruppo10.server.database;

import java.sql.SQLException;

/** Segnala il tentativo di registrare uno username già presente. */
public class UsernameGiaEsistenteException extends SQLException {

    public UsernameGiaEsistenteException(SQLException causa) {
        super("Username già in uso.", causa.getSQLState(),
                causa.getErrorCode(), causa);
    }
}
