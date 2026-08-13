package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import com.gruppo10.database.ManagerDB;
import com.gruppo10.permessi.SessionManager;
import com.gruppo10.servizi_int.PreferitiServiceInt;

public class PreferitiServiceImp extends UnicastRemoteObject implements PreferitiServiceInt {

    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;
    SessionManager sessionManager;

    public PreferitiServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super();
        this.managerDB = managerDB;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean controlloPreferito(int idUt, int idRis) throws RemoteException {
        try {
            return managerDB.getPreferitoDAO().controlloPreferito(idUt, idRis);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante il controllo del preferito\n" + e.getMessage());
        }
    }

    @Override
    public void aggiungiPreferito(int idUt, int idRis) throws RemoteException {
        try {
            managerDB.getPreferitoDAO().aggiungiPreferito(idUt, idRis);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta del preferito\n" + e.getMessage());
        }
    }

    @Override
    public void rimuoviPreferito(int idUt, int idRis) throws RemoteException {
        try {
            managerDB.getPreferitoDAO().rimuoviPreferito(idUt, idRis);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la rimozione del preferito\n" + e.getMessage());
        }
    }
    
}
