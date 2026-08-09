package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.eccezioni.GeocodingException;

public interface GeoServiceInt extends Remote{

    Coordinate geocodifica(String indirizzo) throws RemoteException, GeocodingException;

}
