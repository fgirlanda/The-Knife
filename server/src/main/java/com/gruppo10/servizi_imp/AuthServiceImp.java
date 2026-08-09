package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import com.gruppo10.classi.Utente;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.servizi_int.AuthServiceInt;

public class AuthServiceImp extends UnicastRemoteObject implements AuthServiceInt {
    
    ManagerDB managerDB;
    String nome;

    public AuthServiceImp(ManagerDB managerDB) throws RemoteException {
        super();
        this.managerDB = managerDB;
        this.nome = "AuthService";
    }

    @Override
    public void registrati(Utente utente) throws RemoteException {
        try {
            managerDB.getUtenteDAO().aggiungiUtente(utente);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
