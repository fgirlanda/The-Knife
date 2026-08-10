package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import com.gruppo10.database.ManagerDB;
import com.gruppo10.servizi_int.PreferitiServiceInt;

public class PreferitiServiceImp extends UnicastRemoteObject implements PreferitiServiceInt {

    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;

    public PreferitiServiceImp(ManagerDB managerDB) throws RemoteException {
        super();
        this.managerDB = managerDB;
    }

    @Override
    public boolean controlloPreferito(int idUt, int idRis) throws IllegalArgumentException, RemoteException, SQLException {
        return managerDB.getPreferitoDAO().controlloPreferito(idUt, idRis);
    }

    @Override
    public void aggiungiPreferito(int idUt, int idRis) throws IllegalArgumentException, RemoteException, SQLException {
        managerDB.getPreferitoDAO().aggiungiPreferito(idUt, idRis);
    }

    @Override
    public void rimuoviPreferito(int idUt, int idRis) throws IllegalArgumentException, RemoteException, SQLException {
        managerDB.getPreferitoDAO().rimuoviPreferito(idUt, idRis);
    }
    
}
