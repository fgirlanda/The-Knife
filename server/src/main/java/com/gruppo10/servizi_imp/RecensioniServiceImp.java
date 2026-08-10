package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.servizi_int.RecensioniServiceInt;

public class RecensioniServiceImp extends UnicastRemoteObject implements RecensioniServiceInt {

    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;

    public RecensioniServiceImp(ManagerDB managerDB) throws RemoteException {
        super();
        this.managerDB = managerDB;
    }

    @Override
    public void aggiungiRecensione(Recensione recensione, Ristorante ristorante) throws RemoteException, IllegalArgumentException, SQLException {
        managerDB.getRecensioneDAO().aggiungiRecensione(recensione);
        ristorante.aggiungiRecensione(recensione);
        managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
    }
    
}
