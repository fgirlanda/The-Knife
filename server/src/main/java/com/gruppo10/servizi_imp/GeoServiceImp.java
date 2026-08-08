package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.gruppo10.servizi_int.GeoServiceInt;

public class GeoServiceImp extends UnicastRemoteObject implements GeoServiceInt {
    
    public GeoServiceImp() throws RemoteException {
        super();
    }

}
