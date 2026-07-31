package com.gruppo10.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;

/** Gestisce la persistenza PostgreSQL delle recensioni. */
public class RecensioneDAO {

    private static final String SELECT_BASE = """
            SELECT id_cliente, id_ristorante, id_recensione, username,
                   voto, testo, risposta
            FROM recensioni
            """;

    public List<Recensione> trovaTutte() throws SQLException {
        return eseguiRicerca(SELECT_BASE + " ORDER BY id_recensione");
    }

    /** Alias del caricamento completo precedentemente svolto da RecensioneCSV. */
    public List<Recensione> caricaRecensioni() throws SQLException {
        return trovaTutte();
    }

    public Optional<Recensione> cercaPerId(int idRecensione) throws SQLException {
        String sql = SELECT_BASE + " WHERE id_recensione = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, idRecensione);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(creaRecensione(result)) : Optional.empty();
            }
        }
    }

    public List<Recensione> trovaPerRistorante(int idRistorante) throws SQLException {
        return eseguiRicercaConId(
                SELECT_BASE + " WHERE id_ristorante = ? ORDER BY id_recensione", idRistorante);
    }

    public List<Recensione> trovaPerUtente(int idUtente) throws SQLException {
        return eseguiRicercaConId(
                SELECT_BASE + " WHERE id_cliente = ? ORDER BY id_recensione", idUtente);
    }

    /**
     * Restituisce le recensioni scritte da un utente associando a ciascuna il
     * relativo ristorante completo di tutte le sue recensioni.
     *
     * <p>Questa forma è adatta al profilo cliente: la card può mostrare il nome
     * del ristorante e aprirne la pagina senza lavorare su un oggetto
     * incompleto.</p>
     */
    public List<Recensione> trovaPerUtenteConRistorante(int idUtente)
            throws SQLException {
        String sql = """
                WITH ristoranti_recensiti AS (
                    SELECT DISTINCT id_ristorante
                    FROM recensioni
                    WHERE id_cliente = ?
                )
                SELECT r.id_ristorante, r.nome, r.indirizzo, r.delivery,
                       r.prenotazione_online, r.tipo_cucina, r.prezzo,
                       r.descrizione, r.latitudine, r.longitudine, r.proprietario,
                       COALESCE((SELECT AVG(media.voto)
                                 FROM recensioni media
                                 WHERE media.id_ristorante = r.id_ristorante), 0)
                           AS media_recensioni,
                       rec.id_cliente, rec.id_recensione, rec.username,
                       rec.voto, rec.testo, rec.risposta
                FROM ristoranti r
                JOIN ristoranti_recensiti rr
                  ON rr.id_ristorante = r.id_ristorante
                LEFT JOIN recensioni rec
                  ON rec.id_ristorante = r.id_ristorante
                ORDER BY r.id_ristorante, rec.id_recensione
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, idUtente);
            try (ResultSet result = statement.executeQuery()) {
                List<Ristorante> ristoranti = new RistoranteDAO()
                        .estraiRistoranti(result);
                return ristoranti.stream()
                        .flatMap(ristorante -> ristorante.getRecensioni().stream())
                        .filter(recensione -> recensione.getIdUtente() == idUtente)
                        .toList();
            }
        }
    }

    /**
     * Controlla se un cliente ha già recensito un determinato ristorante.
     */
    public boolean esisteRecensione(int idUtente, int idRistorante) throws SQLException {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM recensioni
                    WHERE id_cliente = ? AND id_ristorante = ?
                )
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, idUtente);
            statement.setInt(2, idRistorante);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getBoolean(1);
            }
        }
    }

    public Recensione aggiungiRecensione(Recensione recensione) throws SQLException {
        validaPerInserimento(recensione);
        String sql = """
                INSERT INTO recensioni (
                    id_cliente, id_ristorante, username, voto, testo, risposta
                ) VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id_recensione
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, recensione.getIdUtente());
            statement.setInt(2, recensione.getIdRistorante());
            statement.setString(3, recensione.getUsername());
            statement.setInt(4, recensione.getStelle());
            statement.setString(5, recensione.getTesto());
            statement.setString(6, recensione.getRisposta());
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                recensione.setIdRec(result.getInt("id_recensione"));
                return recensione;
            } catch (SQLException e) {
                String messaggio = e.getMessage();
                if ("23505".equals(e.getSQLState()) && messaggio != null
                        && messaggio.contains("recensioni_cliente_ristorante_unique")) {
                    throw new RecensioneDuplicataException(e);
                }
                throw e;
            }
        }
    }

    public boolean aggiungiRisposta(int idRecensione, String risposta) throws SQLException {
        richiediNonNull(risposta, "La risposta non può essere null");
        return eseguiAggiornamento(
                "UPDATE recensioni SET risposta = ? WHERE id_recensione = ?",
                risposta, idRecensione);
    }

    public boolean aggiungiRisposta(Recensione recensione, String risposta) throws SQLException {
        richiediNonNull(recensione, "La recensione non può essere null");
        validaIdRecensione(recensione.getIdRec());
        boolean aggiornata = aggiungiRisposta(recensione.getIdRec(), risposta);
        if (aggiornata) {
            recensione.setRisposta(risposta);
        }
        return aggiornata;
    }

    public boolean modificaRecensione(int idRecensione, String testo, int voto) throws SQLException {
        validaTestoEVoto(testo, voto);
        String sql = "UPDATE recensioni SET testo = ?, voto = ? WHERE id_recensione = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, testo);
            statement.setInt(2, voto);
            statement.setInt(3, idRecensione);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean modificaRecensione(Recensione recensione, String testo, int voto)
            throws SQLException {
        richiediNonNull(recensione, "La recensione non può essere null");
        validaTestoEVoto(testo, voto);
        validaIdRecensione(recensione.getIdRec());
        boolean aggiornata = modificaRecensione(recensione.getIdRec(), testo, voto);
        if (aggiornata) {
            recensione.setTesto(testo);
            recensione.setStelle(voto);
        }
        return aggiornata;
    }

    public boolean rimuoviRecensione(int idRecensione) throws SQLException {
        String sql = "DELETE FROM recensioni WHERE id_recensione = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, idRecensione);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean rimuoviRecensione(Recensione recensione) throws SQLException {
        richiediNonNull(recensione, "La recensione non può essere null");
        validaIdRecensione(recensione.getIdRec());
        return rimuoviRecensione(recensione.getIdRec());
    }

    private List<Recensione> eseguiRicerca(String sql) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            return estraiRecensioni(result);
        }
    }

    private List<Recensione> eseguiRicercaConId(String sql, int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return estraiRecensioni(result);
            }
        }
    }

    private List<Recensione> estraiRecensioni(ResultSet result) throws SQLException {
        List<Recensione> recensioni = new ArrayList<>();
        while (result.next()) {
            recensioni.add(creaRecensione(result));
        }
        return recensioni;
    }

    private Recensione creaRecensione(ResultSet result) throws SQLException {
        Recensione recensione = new Recensione();
        recensione.setIdUtente(result.getInt("id_cliente"));
        recensione.setIdRistorante(result.getInt("id_ristorante"));
        recensione.setIdRec(result.getInt("id_recensione"));
        recensione.setUsername(result.getString("username"));
        recensione.setStelle(result.getInt("voto"));
        recensione.setTesto(result.getString("testo"));
        String risposta = result.getString("risposta");
        recensione.setRisposta(risposta == null ? "" : risposta);
        return recensione;
    }

    private boolean eseguiAggiornamento(String sql, String valore, int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, valore);
            statement.setInt(2, id);
            return statement.executeUpdate() == 1;
        }
    }

    private void validaPerInserimento(Recensione recensione) {
        richiediNonNull(recensione, "La recensione non può essere null");
        richiediNonNull(recensione.getUsername(), "Lo username è obbligatorio");
        richiediNonNull(recensione.getTesto(), "Il testo è obbligatorio");
        validaTestoEVoto(recensione.getTesto(), recensione.getStelle());
    }

    private void validaTestoEVoto(String testo, int voto) {
        richiediNonNull(testo, "Il testo non può essere null");
        if (voto < 1 || voto > 5) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 1 e 5");
        }
    }

    private void validaIdRecensione(int idRecensione) {
        if (idRecensione <= 0) {
            throw new IllegalArgumentException(
                    "Per modificare o rimuovere una recensione serve un ID valido");
        }
    }

    private <T> T richiediNonNull(T valore, String messaggio) {
        if (valore == null) {
            throw new IllegalArgumentException(messaggio);
        }
        return valore;
    }
}
