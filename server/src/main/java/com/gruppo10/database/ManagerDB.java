package com.gruppo10.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Superclasse comune per i DAO: centralizza apertura della connessione ed
 * esecuzione delle query, lasciando alle sottoclassi solo la scrittura dello
 * SQL specifico e la mappatura delle righe sui rispettivi oggetti di
 * dominio.
 */
public abstract class ManagerDB {

    private static final String URL = "jdbc:postgresql://localhost:5432/TheKnife";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Mattia2004";

    /** Apre una nuova connessione al database. */
    protected static Connection apriConnessione() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Trasforma un {@link ResultSet} nel tipo desiderato. Usata sia per
     * mappare una singola riga (dentro {@link #selezionaLista} o
     * {@link #selezionaUnica}), sia per elaborare l'intero result set quando
     * una sottoclasse ha bisogno di una logica di aggregazione custom (vedi
     * {@code RistoranteDAO#estraiRistoranti}).
     */
    @FunctionalInterface
    protected interface MappaRisultato<T> {
        T mappa(ResultSet result) throws SQLException;
    }

    /** Costruisce l'eccezione applicativa da lanciare quando un insert viola un vincolo UNIQUE. */
    @FunctionalInterface
    protected interface EccezioneVincolo {
        SQLException crea(SQLException causa);
    }

    /**
     * Esegue una query e lascia al chiamante la piena elaborazione del
     * {@link ResultSet}. È il metodo più generico: le altre selezioni si
     * appoggiano su questo.
     */
    protected <T> T eseguiQuery(String sql, MappaRisultato<T> mapper, Object... parametri)
            throws SQLException {
        try (Connection conn = apriConnessione();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            impostaParametri(statement, parametri);
            try (ResultSet result = statement.executeQuery()) {
                return mapper.mappa(result);
            }
        }
    }

    /** Esegue una SELECT e restituisce tutte le righe mappate in una lista. */
    protected <T> List<T> selezionaLista(String sql, MappaRisultato<T> mapper, Object... parametri)
            throws SQLException {
        return eseguiQuery(sql, result -> {
            List<T> righe = new ArrayList<>();
            while (result.next()) {
                righe.add(mapper.mappa(result));
            }
            return righe;
        }, parametri);
    }

    /** Esegue una SELECT e restituisce al più una riga mappata. */
    protected <T> Optional<T> selezionaUnica(String sql, MappaRisultato<T> mapper, Object... parametri)
            throws SQLException {
        return eseguiQuery(sql,
                result -> result.next() ? Optional.of(mapper.mappa(result)) : Optional.empty(),
                parametri);
    }

    /** Esegue una query che restituisce un singolo valore booleano (tipicamente un EXISTS). */
    protected boolean selezionaBooleano(String sql, Object... parametri) throws SQLException {
        return eseguiQuery(sql, result -> {
            result.next();
            return result.getBoolean(1);
        }, parametri);
    }

    /** Esegue una query che restituisce un singolo valore numerico (es. AVG, COUNT). */
    protected double selezionaDouble(String sql, Object... parametri) throws SQLException {
        return eseguiQuery(sql, result -> {
            result.next();
            return result.getDouble(1);
        }, parametri);
    }

    /** Esegue un INSERT/UPDATE/DELETE e restituisce il numero di righe modificate. */
    protected int aggiorna(String sql, Object... parametri) throws SQLException {
        try (Connection conn = apriConnessione();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            impostaParametri(statement, parametri);
            return statement.executeUpdate();
        }
    }

    /**
     * Esegue un {@code INSERT ... RETURNING} e restituisce l'id generato
     * (prima colonna del result set), senza intercettare eventuali
     * violazioni di vincoli UNIQUE.
     */
    protected int inserisciERitornaId(String sql, Object... parametri) throws SQLException {
        return eseguiQuery(sql, result -> {
            result.next();
            return result.getInt(1);
        }, parametri);
    }

    /**
     * Come {@link #inserisciERitornaId(String, Object...)}, ma traduce la
     * violazione di uno specifico vincolo UNIQUE in un'eccezione applicativa
     * dedicata (es. username o recensione duplicati), lasciando propagare
     * inalterata qualsiasi altra {@link SQLException}.
     */
    protected int inserisciERitornaId(String sql, String nomeVincoloUnico,
            EccezioneVincolo eccezione, Object... parametri) throws SQLException {
        try (Connection conn = apriConnessione();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            impostaParametri(statement, parametri);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getInt(1);
            } catch (SQLException e) {
                if (violaVincolo(e, nomeVincoloUnico)) {
                    throw eccezione.crea(e);
                }
                throw e;
            }
        }
    }

    private boolean violaVincolo(SQLException e, String nomeVincolo) {
        String messaggio = e.getMessage();
        return "23505".equals(e.getSQLState()) && messaggio != null && messaggio.contains(nomeVincolo);
    }

    /** Imposta i parametri di uno statement, inferendo il tipo SQL da quello Java. */
    protected void impostaParametri(PreparedStatement statement, Object... parametri)
            throws SQLException {
        for (int i = 0; i < parametri.length; i++) {
            Object parametro = parametri[i];
            int indice = i + 1;
            if (parametro instanceof String valore) {
                statement.setString(indice, valore);
            } else if (parametro instanceof Integer valore) {
                statement.setInt(indice, valore);
            } else if (parametro instanceof Boolean valore) {
                statement.setBoolean(indice, valore);
            } else if (parametro instanceof Double valore) {
                statement.setDouble(indice, valore);
            } else {
                statement.setObject(indice, parametro);
            }
        }
    }

    /** Validazione di comodo condivisa da tutti i DAO. */
    protected <T> T richiediNonNull(T valore, String messaggio) {
        if (valore == null) {
            throw new IllegalArgumentException(messaggio);
        }
        return valore;
    }
}
