package com.gruppo10.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.Preferito;

/** Gestisce la persistenza PostgreSQL dei ristoranti preferiti. */
public class PreferitoDAO {

    public List<Preferito> trovaTutti() throws SQLException {
        String sql = "SELECT id_cliente, id_ristorante FROM preferiti "
                + "ORDER BY id_cliente, id_ristorante";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            return estraiPreferiti(result);
        }
    }

    public List<Preferito> trovaPerUtente(int idUtente) throws SQLException {
        String sql = "SELECT id_cliente, id_ristorante FROM preferiti "
                + "WHERE id_cliente = ? ORDER BY id_ristorante";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, idUtente);
            try (ResultSet result = statement.executeQuery()) {
                return estraiPreferiti(result);
            }
        }
    }

    public boolean controlloPreferito(int idUtente, int idRistorante) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM preferiti "
                + "WHERE id_cliente = ? AND id_ristorante = ?)";
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

    /**
     * Inserisce un preferito. Se la coppia esiste già non genera duplicati.
     *
     * @return true se il preferito è stato inserito, false se esisteva già
     */
    public boolean aggiungiPreferito(Preferito preferito) throws SQLException {
        richiediNonNull(preferito, "Il preferito non può essere null");
        String sql = "INSERT INTO preferiti (id_cliente, id_ristorante) VALUES (?, ?) "
                + "ON CONFLICT (id_cliente, id_ristorante) DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, preferito.getIdUtente());
            statement.setInt(2, preferito.getIdRistorante());
            return statement.executeUpdate() == 1;
        }
    }

    public boolean aggiungiPreferito(int idUtente, int idRistorante) throws SQLException {
        return aggiungiPreferito(new Preferito(idUtente, idRistorante));
    }

    public boolean rimuoviPreferito(Preferito preferito) throws SQLException {
        richiediNonNull(preferito, "Il preferito non può essere null");
        return rimuoviPreferito(preferito.getIdUtente(), preferito.getIdRistorante());
    }

    public boolean rimuoviPreferito(int idUtente, int idRistorante) throws SQLException {
        String sql = "DELETE FROM preferiti WHERE id_cliente = ? AND id_ristorante = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, idUtente);
            statement.setInt(2, idRistorante);
            return statement.executeUpdate() == 1;
        }
    }

    private List<Preferito> estraiPreferiti(ResultSet result) throws SQLException {
        List<Preferito> preferiti = new ArrayList<>();
        while (result.next()) {
            preferiti.add(new Preferito(
                    result.getInt("id_cliente"), result.getInt("id_ristorante")));
        }
        return preferiti;
    }

    private <T> T richiediNonNull(T valore, String messaggio) {
        if (valore == null) {
            throw new IllegalArgumentException(messaggio);
        }
        return valore;
    }
}
