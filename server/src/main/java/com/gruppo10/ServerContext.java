package com.gruppo10;

import java.rmi.RemoteException;

import com.gruppo10.database.ManagerDB;
import com.gruppo10.permessi.SessionManager;
import com.gruppo10.servizi_imp.AuthServiceImp;
import com.gruppo10.servizi_imp.GeoServiceImp;
import com.gruppo10.servizi_imp.PreferitiServiceImp;
import com.gruppo10.servizi_imp.RecensioniServiceImp;
import com.gruppo10.servizi_imp.RistorantiServiceImp;

/**
 * Contesto server con i componenti condivisi dell'applicazione.
 * Inizializza il database, il gestore delle sessioni e tutti i service remoti.
 */
public class ServerContext {
    /** Gestore del database dell'applicazione. */
    ManagerDB managerDB;

    /** Gestore delle sessioni degli utenti autenticati. */
    SessionManager sessionManager;

    /** Implementazione del service di autenticazione. */
    AuthServiceImp authServiceImp;

    /** Implementazione del service di geocodifica. */
    GeoServiceImp geoServiceImp;

    /** Implementazione del service per la gestione dei ristoranti. */
    RistorantiServiceImp ristorantiServiceImp;

    /** Implementazione del service per la gestione delle recensioni. */
    RecensioniServiceImp recensioniServiceImp;

    /** Implementazione del service per i preferiti. */
    PreferitiServiceImp preferitiServiceImp;

    /**
     * Costruisce il contesto iniziale del server.
     *
     * @throws RemoteException se si verifica un errore durante la creazione dei service remoti
     */
    ServerContext() throws RemoteException {
        managerDB = new ManagerDB();
        sessionManager = new SessionManager();

        authServiceImp = new AuthServiceImp(managerDB, sessionManager);
        geoServiceImp = new GeoServiceImp();
        ristorantiServiceImp = new RistorantiServiceImp(managerDB, sessionManager);
        recensioniServiceImp = new RecensioniServiceImp(managerDB, sessionManager);
        preferitiServiceImp = new PreferitiServiceImp(managerDB, sessionManager);
    }

    /**
     * Restituisce il gestore del database.
     *
     * @return manager del database condiviso dal server
     */
    public ManagerDB getManagerDB() {
        return managerDB;
    }
}
