package com.gruppo10;

import java.rmi.RemoteException;

import com.gruppo10.database.ManagerDB;
import com.gruppo10.permessi.SessionManager;
import com.gruppo10.servizi_imp.AuthServiceImp;
import com.gruppo10.servizi_imp.GeoServiceImp;
import com.gruppo10.servizi_imp.PreferitiServiceImp;
import com.gruppo10.servizi_imp.RecensioniServiceImp;
import com.gruppo10.servizi_imp.RistorantiServiceImp;

public class ServerContext {
    ManagerDB managerDB;
    SessionManager sessionManager;

    AuthServiceImp authServiceImp;
    GeoServiceImp geoServiceImp;
    RistorantiServiceImp ristorantiServiceImp;
    RecensioniServiceImp recensioniServiceImp;
    PreferitiServiceImp preferitiServiceImp;

    ServerContext() throws RemoteException {
        managerDB = new ManagerDB();
        sessionManager = new SessionManager();

        authServiceImp = new AuthServiceImp(managerDB, sessionManager);
        geoServiceImp = new GeoServiceImp();
        ristorantiServiceImp = new RistorantiServiceImp(managerDB, sessionManager);
        recensioniServiceImp = new RecensioniServiceImp(managerDB, sessionManager);
        preferitiServiceImp = new PreferitiServiceImp(managerDB, sessionManager);
    }
    
    public ManagerDB getManagerDB() {
        return managerDB;
    }
}
