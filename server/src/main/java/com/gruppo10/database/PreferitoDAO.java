package com.gruppo10.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Preferito;

/** Gestisce la persistenza PostgreSQL dei ristoranti preferiti. */
public class PreferitoDAO extends BasicDAO {

    /** Query di base per leggere le associazioni tra clienti e ristoranti. */
    private static final String SELECT_BASE = "SELECT id_cliente, id_ristorante FROM preferiti";

    /**
     * Restituisce tutti i preferiti ordinati per cliente e ristorante.
     *
     * @return tutti i preferiti presenti nel database
     * @throws SQLException se la lettura fallisce
     */
    public List<Preferito> trovaTutti() throws SQLException {
        return selezionaLista(SELECT_BASE + " ORDER BY id_cliente, id_ristorante", this::creaPreferito);
    }

    /**
     * Restituisce i preferiti di un cliente.
     *
     * @param idUtente identificativo del cliente
     * @return preferiti del cliente
     * @throws SQLException se la lettura fallisce
     */
    public List<Preferito> trovaPerUtente(int idUtente) throws SQLException {
        return selezionaLista(SELECT_BASE + " WHERE id_cliente = ? ORDER BY id_ristorante",
                this::creaPreferito, idUtente);
    }

    /**
     * Verifica se un ristorante è tra i preferiti di un cliente.
     *
     * @param idUtente cliente da verificare
     * @param idRistorante ristorante da verificare
     * @return {@code true} se l'associazione esiste
     * @throws SQLException se la verifica fallisce
     */
    public boolean controlloPreferito(int idUtente, int idRistorante) throws SQLException {
        return selezionaBooleano(
                "SELECT EXISTS (SELECT 1 FROM preferiti WHERE id_cliente = ? AND id_ristorante = ?)",
                idUtente, idRistorante);
    }

    /**
     * Inserisce un preferito. Se la coppia esiste già non genera duplicati.
     *
     * @return true se il preferito è stato inserito, false se esisteva già
     */
    public boolean aggiungiPreferito(Preferito preferito) throws SQLException {
        richiediNonNull(preferito, "Il preferito non può essere null");
        return aggiungiPreferito(preferito.getIdUtente(), preferito.getIdRistorante());
    }

    /**
     * Inserisce l'associazione tra un cliente e un ristorante preferito.
     *
     * @param idUtente identificativo del cliente
     * @param idRistorante identificativo del ristorante
     * @return {@code true} se l'inserimento è avvenuto
     * @throws SQLException se il salvataggio fallisce
     */
    public boolean aggiungiPreferito(int idUtente, int idRistorante) throws SQLException {
        return aggiorna("INSERT INTO preferiti (id_cliente, id_ristorante) VALUES (?, ?) "
                + "ON CONFLICT (id_cliente, id_ristorante) DO NOTHING", idUtente, idRistorante) == 1;
    }

    /**
     * Rimuove un'associazione preferito dal database.
     *
     * @param preferito associazione da rimuovere
     * @return {@code true} se l'associazione è stata rimossa
     * @throws SQLException se la cancellazione fallisce
     */
    public boolean rimuoviPreferito(Preferito preferito) throws SQLException {
        richiediNonNull(preferito, "Il preferito non può essere null");
        return rimuoviPreferito(preferito.getIdUtente(), preferito.getIdRistorante());
    }

    /**
     * Rimuove l'associazione tra un cliente e un ristorante.
     *
     * @param idUtente identificativo del cliente
     * @param idRistorante identificativo del ristorante
     * @return {@code true} se l'associazione è stata rimossa
     * @throws SQLException se la cancellazione fallisce
     */
    public boolean rimuoviPreferito(int idUtente, int idRistorante) throws SQLException {
        return aggiorna("DELETE FROM preferiti WHERE id_cliente = ? AND id_ristorante = ?",
                idUtente, idRistorante) == 1;
    }

    /**
     * Crea un preferito leggendo la riga corrente del result set.
     *
     * @param result result set posizionato sulla riga da leggere
     * @return preferito ricostruito
     * @throws SQLException se la lettura fallisce
     */
    private Preferito creaPreferito(ResultSet result) throws SQLException {
        return new Preferito(result.getInt("id_cliente"), result.getInt("id_ristorante"));
    }
}