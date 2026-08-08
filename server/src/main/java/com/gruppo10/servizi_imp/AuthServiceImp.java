package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.gruppo10.servizi_int.AuthServiceInt;

public class AuthServiceImp extends UnicastRemoteObject implements AuthServiceInt {
    
    public AuthServiceImp() throws RemoteException {
        super();
    }

}
