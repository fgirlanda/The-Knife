package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import com.gruppo10.classi.Ristorante;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.servizi_int.RistorantiServiceInt;

public class RistorantiServiceImp extends UnicastRemoteObject implements RistorantiServiceInt {

    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;

    public RistorantiServiceImp(ManagerDB managerDB) throws RemoteException {
        super();
        this.managerDB = managerDB;
    }

    @Override
    public void aggiungiRistorante(Ristorante ristorante) throws RemoteException, IllegalArgumentException, SQLException {
        managerDB.getRistoranteDAO().aggiungiRistorante(ristorante);
    }
    
}
