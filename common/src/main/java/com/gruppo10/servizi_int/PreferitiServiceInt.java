package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PreferitiServiceInt extends Remote{

    boolean controlloPreferito(int idUt, int idRis) throws RemoteException;

    void aggiungiPreferito(int idUt, int idRis) throws RemoteException;

    void rimuoviPreferito(int idUt, int idRis) throws RemoteException;

}
