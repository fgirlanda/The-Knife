package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.Utente;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.eccezioni.PermessoNegatoException;
import com.gruppo10.permessi.SessionManager;

/**
 * Classe base per tutti i service remoti del server.
 * Fornisce il riferimento al database e al manager delle sessioni e un metodo
 * comune per verificare i permessi richiesti dalle operazioni.
 */
public abstract class BasicServiceImp extends UnicastRemoteObject {
    private static final long serialVersionUID = 1L;

    /** Gestore del database condiviso da tutti i service. */
    ManagerDB managerDB;

    /** Gestore delle sessioni attive. */
    SessionManager sessionManager;

    /**
     * Costruisce un nuovo service base.
     *
     * @param managerDB gestore del database
     * @param sessionManager gestore delle sessioni utente
     * @throws RemoteException se si verifica un errore di inizializzazione RMI
     */
    public BasicServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super();
        this.managerDB = managerDB;
        this.sessionManager = sessionManager;
    }

    /**
     * Verifica che la sessione indicata sia valida e che l'utente abbia il ruolo richiesto.
     *
     * @param token token della sessione da verificare
     * @param ruoloRichiesto ruolo richiesto per l'operazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se la sessione non è valida oppure il ruolo non è corretto
     */
    protected void verificaPermessi(String token, Ruolo ruoloRichiesto) throws RemoteException, PermessoNegatoException {
        Utente utenteLoggato = sessionManager.utenteDiSessione(token).orElseThrow(() -> new PermessoNegatoException("Sessione non valida"));

        if (utenteLoggato.getRuolo() != ruoloRichiesto) {
            throw new PermessoNegatoException("Permesso negato: ruolo richiesto - " + ruoloRichiesto.name());
        }
    }
    
}
