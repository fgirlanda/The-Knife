package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.gruppo10.database.ManagerDB;
import com.gruppo10.servizi_int.GeoServiceInt;

public class GeoServiceImp extends UnicastRemoteObject implements GeoServiceInt {
    ManagerDB managerDB;
    String nome;
    
    public GeoServiceImp(ManagerDB managerDB) throws RemoteException {
        super();
        this.managerDB = managerDB;
        this.nome = "GeoService";
    }

}
