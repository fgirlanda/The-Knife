package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface PreferitiServiceInt extends Remote{

    boolean controlloPreferito(int idUt, int idRis) throws IllegalArgumentException, RemoteException, SQLException;

    void aggiungiPreferito(int idUt, int idRis) throws IllegalArgumentException, RemoteException, SQLException;

    void rimuoviPreferito(int idUt, int idRis) throws IllegalArgumentException, RemoteException, SQLException;

}
