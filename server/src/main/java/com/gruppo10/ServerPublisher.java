/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Classe responsabile della pubblicazione dei service RMI sul registro del sistema.
 * Registra i service del server e li rimuove alla chiusura dell'applicazione.
 */
public class ServerPublisher {
    /** Contesto del server contenente i service da esporre. */
    ServerContext serverContext;

    /** Registro RMI su cui i service vengono pubblicati. */
    Registry registry;

    /**
     * Costruisce un pubblicatore di service associato a un contesto server.
     *
     * @param serverContext contesto del server con i service da esporre
     */
    public ServerPublisher(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    /**
     * Avvia il registro RMI e registra tutti i service disponibili.
     *
     * @throws RemoteException se la registrazione dei service fallisce
     */
    public void avvia() throws RemoteException {
        registry = LocateRegistry.createRegistry(1099);
        registry.rebind("AuthService", serverContext.authServiceImp);
        registry.rebind("GeoService", serverContext.geoServiceImp);
        registry.rebind("RistorantiService", serverContext.ristorantiServiceImp);
        registry.rebind("RecensioniService", serverContext.recensioniServiceImp);
        registry.rebind("PreferitiService", serverContext.preferitiServiceImp);
    }

    /**
     * Arresta il registro RMI e libera i service remoti pubblicati.
     */
    public void arresta() {
        try {
            UnicastRemoteObject.unexportObject(serverContext.geoServiceImp, true);
            UnicastRemoteObject.unexportObject(serverContext.authServiceImp, true);
            UnicastRemoteObject.unexportObject(serverContext.ristorantiServiceImp, true);
            UnicastRemoteObject.unexportObject(serverContext.recensioniServiceImp, true);
            UnicastRemoteObject.unexportObject(serverContext.preferitiServiceImp, true);
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }
}
