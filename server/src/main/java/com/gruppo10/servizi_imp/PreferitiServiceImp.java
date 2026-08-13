package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
    import java.sql.SQLException;

import com.gruppo10.database.ManagerDB;
import com.gruppo10.eccezioni.PermessoNegatoException;
import com.gruppo10.permessi.SessionManager;
import com.gruppo10.servizi_int.PreferitiServiceInt;

public class PreferitiServiceImp extends BasicServiceImp implements PreferitiServiceInt {

    public PreferitiServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super(managerDB, sessionManager);
    }

    @Override
    public boolean controlloPreferito(String token,int idUt, int idRis) throws RemoteException, PermessoNegatoException {
        
        verificaPermessi(token, "CLIENTE");

        try {
            return managerDB.getPreferitoDAO().controlloPreferito(idUt, idRis);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante il controllo del preferito\n" + e.getMessage());
        }
    }

    @Override
    public void aggiungiPreferito(String token, int idUt, int idRis) throws RemoteException, PermessoNegatoException {

        verificaPermessi(token, "CLIENTE");

        try {
            managerDB.getPreferitoDAO().aggiungiPreferito(idUt, idRis);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta del preferito\n" + e.getMessage());
        }
    }

    @Override
    public void rimuoviPreferito(String token, int idUt, int idRis) throws RemoteException, PermessoNegatoException {
        
        verificaPermessi(token, "CLIENTE");

        try {
            managerDB.getPreferitoDAO().rimuoviPreferito(idUt, idRis);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la rimozione del preferito\n" + e.getMessage());
        }
    }
    
}
