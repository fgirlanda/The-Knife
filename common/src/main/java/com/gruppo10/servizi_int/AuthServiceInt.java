package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;

import com.gruppo10.classi.Utente;

public interface AuthServiceInt extends Remote{

    void registrati(Utente utente) throws RemoteException;
    Optional<Utente> login(String username, String password) throws RemoteException;

}
