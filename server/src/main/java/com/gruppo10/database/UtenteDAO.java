package com.gruppo10.server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.gruppo10.common.classi.Coordinate;
import com.gruppo10.common.classi.Ruolo;
import com.gruppo10.common.classi.Utente;

/** Gestisce la persistenza PostgreSQL degli utenti. */
public class UtenteDAO extends ManagerDB {

    private static final String SELECT_BASE = """
            SELECT id_utente, nome, cognome, username, password, data_nascita,
                   indirizzo, ruolo, latitudine, longitudine
            FROM utenti
            """;

    public List<Utente> trovaTutti() throws SQLException {
        return selezionaLista(SELECT_BASE + " ORDER BY id_utente", this::creaUtente);
    }

    /** Alias del caricamento completo precedentemente svolto da UtenteCSV. */
    public List<Utente> caricaUtenti() throws SQLException {
        return trovaTutti();
    }

    public Optional<Utente> cercaPerId(int idUtente) throws SQLException {
        return selezionaUnica(SELECT_BASE + " WHERE id_utente = ?", this::creaUtente, idUtente);
    }

    public Optional<Utente> cercaUtente(String username) throws SQLException {
        richiediNonNull(username, "Lo username non può essere null");
        return selezionaUnica(SELECT_BASE + " WHERE username = ?", this::creaUtente, username);
    }

    public boolean esisteUsername(String username) throws SQLException {
        richiediNonNull(username, "Lo username non può essere null");
        return selezionaBooleano("SELECT EXISTS (SELECT 1 FROM utenti WHERE username = ?)", username);
    }

    public Utente aggiungiUtente(Utente utente) throws SQLException {
        validaPerInserimento(utente);
        String sql = """
                INSERT INTO utenti (
                    nome, cognome, username, password, data_nascita,
                    indirizzo, ruolo, latitudine, longitudine
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id_utente
                """;
        int id = inserisciERitornaId(sql, "utenti_username_unique", UsernameGiaEsistenteException::new,
                utente.getNome(), utente.getCognome(), utente.getUsername(), utente.getPassword(),
                utente.getDataDiNascita(), utente.getIndirizzo(), utente.getRuolo().name(),
                utente.getCords().getLat(), utente.getCords().getLon());
        utente.setId(id);
        return utente;
    }

    private Utente creaUtente(ResultSet result) throws SQLException {
        Utente utente = new Utente();
        utente.setId(result.getInt("id_utente"));
        utente.setNome(result.getString("nome"));
        utente.setCognome(result.getString("cognome"));
        utente.setUsername(result.getString("username"));
        utente.setPassword(result.getString("password"));
        LocalDate dataNascita = result.getObject("data_nascita", LocalDate.class);
        utente.setDataDiNascita(dataNascita.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        utente.setIndirizzo(result.getString("indirizzo"));
        utente.setRuolo(result.getString("ruolo"));
        utente.setCords(new Coordinate(
                result.getDouble("latitudine"), result.getDouble("longitudine")));
        return utente;
    }

    private void validaPerInserimento(Utente utente) {
        richiediNonNull(utente, "L'utente non può essere null");
        richiediNonNull(utente.getNome(), "Il nome è obbligatorio");
        richiediNonNull(utente.getCognome(), "Il cognome è obbligatorio");
        richiediNonNull(utente.getUsername(), "Lo username è obbligatorio");
        richiediNonNull(utente.getPassword(), "La password è obbligatoria");
        richiediNonNull(utente.getDataDiNascita(), "La data di nascita è obbligatoria");
        richiediNonNull(utente.getIndirizzo(), "L'indirizzo è obbligatorio");
        richiediNonNull(utente.getRuolo(), "Il ruolo è obbligatorio");
        richiediNonNull(utente.getCords(), "Le coordinate sono obbligatorie");
        richiediNonNull(utente.getCords().getLat(), "La latitudine è obbligatoria");
        richiediNonNull(utente.getCords().getLon(), "La longitudine è obbligatoria");
        if (utente.getRuolo() == Ruolo.NON_REGISTRATO) {
            throw new IllegalArgumentException("Un utente ospite non può essere salvato");
        }
    }
}