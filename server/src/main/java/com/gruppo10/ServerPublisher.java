package com.gruppo10;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerPublisher {
    ServerContext serverContext;
    Registry registry;

    public ServerPublisher(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    public void avvia() throws RemoteException {
        registry = LocateRegistry.createRegistry(1099);
        registry.rebind("AuthService", serverContext.authServiceImp);
        registry.rebind("GeoService", serverContext.geoServiceImp);
        registry.rebind("RistorantiService", serverContext.ristorantiServiceImp);
        registry.rebind("RecensioniService", serverContext.recensioniServiceImp);
        registry.rebind("ProfiloService", serverContext.profiloServiceImp);
        registry.rebind("PreferitiService", serverContext.preferitiServiceImp);
    }
}
