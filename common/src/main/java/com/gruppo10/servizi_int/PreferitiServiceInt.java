package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.gruppo10.eccezioni.PermessoNegatoException;

public interface PreferitiServiceInt extends Remote{

    boolean controlloPreferito(String token,int idUt, int idRis) throws RemoteException, PermessoNegatoException;

    void aggiungiPreferito(String token, int idUt, int idRis) throws RemoteException, PermessoNegatoException;

    void rimuoviPreferito(String token, int idUt, int idRis) throws RemoteException, PermessoNegatoException;

}
