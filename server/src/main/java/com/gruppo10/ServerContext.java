package com.gruppo10;

import java.rmi.RemoteException;

import com.gruppo10.database.ManagerDB;
import com.gruppo10.servizi_imp.AuthServiceImp;
import com.gruppo10.servizi_imp.GeoServiceImp;

public class ServerContext {
    ManagerDB managerDB;

    AuthServiceImp authServiceImp;
    GeoServiceImp geoServiceImp;

    ServerContext() throws RemoteException {
        managerDB = new ManagerDB();
        authServiceImp = new AuthServiceImp(managerDB);
        geoServiceImp = new GeoServiceImp(managerDB);
    }
    
    public ManagerDB getManagerDB() {
        return managerDB;
    }
}
