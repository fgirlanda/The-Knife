package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;

public interface RecensioniServiceInt extends Remote{

    void aggiungiRecensione(Recensione recensione, Ristorante ristorante)
            throws RemoteException, IllegalArgumentException, SQLException;

}
