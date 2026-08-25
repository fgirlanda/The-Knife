package com.gruppo10.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce la configurazione di connessione al database e i DAO principali
 * dell'applicazione.
 */
public class ManagerDB {
    /** DAO per i preferiti degli utenti. */
    PreferitoDAO preferitoDAO;

    /** DAO per le recensioni. */
    RecensioneDAO recensioneDAO;

    /** DAO per i ristoranti. */
    RistoranteDAO ristoranteDAO;

    /** DAO per gli utenti. */
    UtenteDAO utenteDAO;

    /** URL JDBC del database attualmente configurato. */
    static String url;

    /** Nome utente per la connessione al database. */
    static String user;

    /** Password per la connessione al database. */
    static String password;

    /**
     * Istanzia i DAO del sistema.
     */
    public ManagerDB() {
        preferitoDAO = new PreferitoDAO();
        recensioneDAO = new RecensioneDAO();
        ristoranteDAO = new RistoranteDAO();
        utenteDAO = new UtenteDAO();
    }

    /**
     * Configura i parametri di connessione al database PostgreSQL e ne verifica la validità.
     *
     * @param host indirizzo del server PostgreSQL
     * @param porta porta del server PostgreSQL
     * @param database nome del database
     * @param user username per l'accesso
     * @param password password per l'accesso
     * @throws SQLException se i parametri non consentono di aprire la connessione
     */
    public void connetti(String host, int porta, String database, String user, String password) throws SQLException {
        ManagerDB.url = "jdbc:postgresql://" + host + ":" + porta + "/" + database;
        ManagerDB.user = user;
        ManagerDB.password = password;

        try (Connection testConn = apri()) {
            testConn.isValid(2);
        }
    }

    /**
     * Apre una nuova connessione JDBC usando la configurazione corrente.
     *
     * @return connessione attiva al database
     * @throws SQLException se la connessione fallisce
     */
    static Connection apri() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Restituisce il DAO dei preferiti.
     *
     * @return DAO dei preferiti
     */
    public PreferitoDAO getPreferitoDAO() {
        return preferitoDAO;
    }

    /**
     * Restituisce il DAO delle recensioni.
     *
     * @return DAO delle recensioni
     */
    public RecensioneDAO getRecensioneDAO() {
        return recensioneDAO;
    }

    /**
     * Restituisce il DAO dei ristoranti.
     *
     * @return DAO dei ristoranti
     */
    public RistoranteDAO getRistoranteDAO() {
        return ristoranteDAO;
    }

    /**
     * Restituisce il DAO degli utenti.
     *
     * @return DAO degli utenti
     */
    public UtenteDAO getUtenteDAO() {
        return utenteDAO;
    }
}
