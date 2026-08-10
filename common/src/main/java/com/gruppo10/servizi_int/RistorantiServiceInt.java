package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import com.gruppo10.classi.Ristorante;

public interface RistorantiServiceInt extends Remote{

    void aggiungiRistorante(Ristorante ristorante) throws RemoteException, IllegalArgumentException, SQLException;

}
