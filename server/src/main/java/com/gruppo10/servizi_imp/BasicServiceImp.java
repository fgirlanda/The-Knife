package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.Utente;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.eccezioni.PermessoNegatoException;
import com.gruppo10.permessi.SessionManager;

public abstract class BasicServiceImp extends UnicastRemoteObject {
    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;
    SessionManager sessionManager;

    public BasicServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super();
        this.managerDB = managerDB;
        this.sessionManager = sessionManager;
    }

    protected void verificaPermessi(String token, Ruolo ruoloRichiesto) throws RemoteException, PermessoNegatoException {
        Utente utenteLoggato = sessionManager.utenteDiSessione(token).orElseThrow(() -> new PermessoNegatoException("Sessione non valida"));

        if (utenteLoggato.getRuolo() != ruoloRichiesto) {
            throw new PermessoNegatoException("Permesso negato: ruolo richiesto - " + ruoloRichiesto.name());
        }
    }
    
}
