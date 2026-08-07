package com.gruppo10;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerTK {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
