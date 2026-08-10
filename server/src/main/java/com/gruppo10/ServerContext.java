package com.gruppo10;

import java.rmi.RemoteException;

import com.gruppo10.database.ManagerDB;
import com.gruppo10.servizi_imp.AuthServiceImp;
import com.gruppo10.servizi_imp.GeoServiceImp;
import com.gruppo10.servizi_imp.PreferitiServiceImp;
import com.gruppo10.servizi_imp.ProfiloServiceImp;
import com.gruppo10.servizi_imp.RecensioniServiceImp;
import com.gruppo10.servizi_imp.RistorantiServiceImp;

public class ServerContext {
    ManagerDB managerDB;

    AuthServiceImp authServiceImp;
    GeoServiceImp geoServiceImp;
    RistorantiServiceImp ristorantiServiceImp;
    RecensioniServiceImp recensioniServiceImp;
    ProfiloServiceImp profiloServiceImp;
    PreferitiServiceImp preferitiServiceImp;

    ServerContext() throws RemoteException {
        managerDB = new ManagerDB();
        authServiceImp = new AuthServiceImp(managerDB);
        geoServiceImp = new GeoServiceImp();
        ristorantiServiceImp = new RistorantiServiceImp(managerDB);
        recensioniServiceImp = new RecensioniServiceImp(managerDB);
        profiloServiceImp = new ProfiloServiceImp(managerDB);
        preferitiServiceImp = new PreferitiServiceImp(managerDB);
    }
    
    public ManagerDB getManagerDB() {
        return managerDB;
    }
}
