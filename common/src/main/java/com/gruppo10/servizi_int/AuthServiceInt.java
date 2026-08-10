package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import com.gruppo10.classi.Utente;
import com.gruppo10.eccezioni.UsernameGiaEsistenteException;

public interface AuthServiceInt extends Remote{

    Utente registrati(Utente utente) throws RemoteException, UsernameGiaEsistenteException;
    Utente login(String username, String password) throws RemoteException, SQLException;
    void trovaTutti() throws RemoteException, SQLException;

}
