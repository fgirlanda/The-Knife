package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.gruppo10.classi.Utente;
import com.gruppo10.eccezioni.UsernameGiaEsistenteException;
import com.gruppo10.classi.Sessione;

public interface AuthServiceInt extends Remote{

    Utente registrati(Utente utente) throws RemoteException, UsernameGiaEsistenteException;
    Sessione login(String username, String password) throws RemoteException;
    void trovaTutti() throws RemoteException;

}
