package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Optional;

import com.gruppo10.classi.Utente;
import com.gruppo10.eccezioni.UsernameGiaEsistenteException;
import com.gruppo10.servizi_int.AuthServiceInt;
import com.gruppo10.database.ManagerDB;

/**
 * Implementazione remota di {@link AuthService}.
 *
 * <p>
 * Nota: il confronto password qui sotto è un placeholder (confronto in
 * chiaro). Se nel progetto avete già un'utility di hashing per le password
 * (usata in fase di registrazione), va usata anche qui al posto di
 * {@code equals}.
 * </p>
 */
public class AuthServiceImp extends UnicastRemoteObject implements AuthServiceInt {

    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;

    public AuthServiceImp(ManagerDB managerDB) throws RemoteException {
        super();
        this.managerDB = managerDB;
    }

    @Override
    public Utente login(String username, String password) throws RemoteException {
        try {
            Optional<Utente> utente = managerDB.getUtenteDAO().cercaUtente(username);
            if (utente.isPresent() && utente.get().getPassword().equals(password)) {
                return utente.get();
            }
            return null;
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la verifica delle credenziali\n" + e.getMessage());
        }
    }

    @Override
    public Utente registrati(Utente utente) throws RemoteException, UsernameGiaEsistenteException {
        try {
            return managerDB.getUtenteDAO().aggiungiUtente(utente);
        } catch (UsernameGiaEsistenteException e) {
            throw e;
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la registrazione\n" + e.getMessage());
        }
    }

    @Override
    public void trovaTutti() throws RemoteException {
        try {
            managerDB.getUtenteDAO().trovaTutti();
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca di tutti gli utenti\n" + e.getMessage());
        }
    }
}