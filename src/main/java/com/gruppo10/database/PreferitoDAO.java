package com.gruppo10.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Preferito;

/** Gestisce la persistenza PostgreSQL dei ristoranti preferiti. */
public class PreferitoDAO extends ManagerDB {

    private static final String SELECT_BASE = "SELECT id_cliente, id_ristorante FROM preferiti";

    public List<Preferito> trovaTutti() throws SQLException {
        return selezionaLista(SELECT_BASE + " ORDER BY id_cliente, id_ristorante", this::creaPreferito);
    }

    public List<Preferito> trovaPerUtente(int idUtente) throws SQLException {
        return selezionaLista(SELECT_BASE + " WHERE id_cliente = ? ORDER BY id_ristorante",
                this::creaPreferito, idUtente);
    }

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

    public boolean aggiungiPreferito(int idUtente, int idRistorante) throws SQLException {
        return aggiorna("INSERT INTO preferiti (id_cliente, id_ristorante) VALUES (?, ?) "
                + "ON CONFLICT (id_cliente, id_ristorante) DO NOTHING", idUtente, idRistorante) == 1;
    }

    public boolean rimuoviPreferito(Preferito preferito) throws SQLException {
        richiediNonNull(preferito, "Il preferito non può essere null");
        return rimuoviPreferito(preferito.getIdUtente(), preferito.getIdRistorante());
    }

    public boolean rimuoviPreferito(int idUtente, int idRistorante) throws SQLException {
        return aggiorna("DELETE FROM preferiti WHERE id_cliente = ? AND id_ristorante = ?",
                idUtente, idRistorante) == 1;
    }

    private Preferito creaPreferito(ResultSet result) throws SQLException {
        return new Preferito(result.getInt("id_cliente"), result.getInt("id_ristorante"));
    }
}