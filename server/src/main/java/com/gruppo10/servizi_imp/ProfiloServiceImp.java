package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.gruppo10.database.ManagerDB;
import com.gruppo10.servizi_int.ProfiloServiceInt;

public class ProfiloServiceImp extends UnicastRemoteObject implements ProfiloServiceInt {

    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;

    public ProfiloServiceImp(ManagerDB managerDB) throws RemoteException {
        super();
        this.managerDB = managerDB;
    }
    
}
