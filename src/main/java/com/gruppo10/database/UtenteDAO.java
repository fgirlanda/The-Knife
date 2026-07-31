package com.gruppo10.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.Utente;

/** Gestisce la persistenza PostgreSQL degli utenti. */
public class UtenteDAO {

    private static final String SELECT_BASE = """
            SELECT id_utente, nome, cognome, username, password, data_nascita,
                   indirizzo, ruolo, latitudine, longitudine
            FROM utenti
            """;

    public List<Utente> trovaTutti() throws SQLException {
        String sql = SELECT_BASE + " ORDER BY id_utente";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            return estraiUtenti(result);
        }
    }

    /** Alias del caricamento completo precedentemente svolto da UtenteCSV. */
    public List<Utente> caricaUtenti() throws SQLException {
        return trovaTutti();
    }

    public Optional<Utente> cercaPerId(int idUtente) throws SQLException {
        String sql = SELECT_BASE + " WHERE id_utente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, idUtente);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(creaUtente(result)) : Optional.empty();
            }
        }
    }

    public Optional<Utente> cercaUtente(String username) throws SQLException {
        richiediNonNull(username, "Lo username non può essere null");
        String sql = SELECT_BASE + " WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(creaUtente(result)) : Optional.empty();
            }
        }
    }

    public boolean esisteUsername(String username) throws SQLException {
        richiediNonNull(username, "Lo username non può essere null");
        String sql = "SELECT EXISTS (SELECT 1 FROM utenti WHERE username = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getBoolean(1);
            }
        }
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

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, utente.getNome());
            statement.setString(2, utente.getCognome());
            statement.setString(3, utente.getUsername());
            statement.setString(4, utente.getPassword());
            statement.setObject(5, utente.getDataDiNascita());
            statement.setString(6, utente.getIndirizzo());
            statement.setString(7, utente.getRuolo().name());
            statement.setDouble(8, utente.getCords().getLat());
            statement.setDouble(9, utente.getCords().getLon());

            try (ResultSet result = statement.executeQuery()) {
                result.next();
                utente.setId(result.getInt("id_utente"));
                return utente;
            } catch (SQLException e) {
                String messaggio = e.getMessage();
                if ("23505".equals(e.getSQLState()) && messaggio != null
                        && messaggio.contains("utenti_username_unique")) {
                    throw new UsernameGiaEsistenteException(e);
                }
                throw e;
            }
        }
    }

    private List<Utente> estraiUtenti(ResultSet result) throws SQLException {
        List<Utente> utenti = new ArrayList<>();
        while (result.next()) {
            utenti.add(creaUtente(result));
        }
        return utenti;
    }

    private Utente creaUtente(ResultSet result) throws SQLException {
        Utente utente = new Utente();
        utente.setId(result.getInt("id_utente"));
        utente.setNome(result.getString("nome"));
        utente.setCognome(result.getString("cognome"));
        utente.setUsername(result.getString("username"));
        utente.setPassword(result.getString("password"));
        java.time.LocalDate dataNascita = result.getObject(
                "data_nascita", java.time.LocalDate.class);
        utente.setDataDiNascita(dataNascita.format(
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
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

    private <T> T richiediNonNull(T valore, String messaggio) {
        if (valore == null) {
            throw new IllegalArgumentException(messaggio);
        }
        return valore;
    }
}
