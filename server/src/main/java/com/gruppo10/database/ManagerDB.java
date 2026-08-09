package com.gruppo10.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManagerDB {
    PreferitoDAO preferitoDAO;
    RecensioneDAO recensioneDAO;
    RistoranteDAO ristoranteDAO;
    UtenteDAO utenteDAO;

    static String url;
    static String user;
    static String password;

    public ManagerDB() {
        preferitoDAO = new PreferitoDAO();
        recensioneDAO = new RecensioneDAO();
        ristoranteDAO = new RistoranteDAO();
        utenteDAO = new UtenteDAO();
    }

    public void connetti(String host, int porta, String database, String user, String password) {
        ManagerDB.url = "jdbc:postgresql://" + host + ":" + porta + "/" + database;
        ManagerDB.user = user;
        ManagerDB.password = password;
    }

    static Connection apri() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
