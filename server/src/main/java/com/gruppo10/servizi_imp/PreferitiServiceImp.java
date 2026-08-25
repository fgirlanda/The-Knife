/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.gruppo10.classi.Ruolo;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.eccezioni.PermessoNegatoException;
import com.gruppo10.permessi.SessionManager;
import com.gruppo10.servizi_int.PreferitiServiceInt;

/**
 * Implementazione del service remoto per la gestione dei preferiti degli utenti.
 */
public class PreferitiServiceImp extends BasicServiceImp implements PreferitiServiceInt {

    /**
     * Costruisce il service dei preferiti.
     *
     * @param managerDB gestore del database
     * @param sessionManager gestore delle sessioni
     * @throws RemoteException se si verifica un errore di inizializzazione RMI
     */
    public PreferitiServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super(managerDB, sessionManager);
    }

    /**
     * Verifica se un ristorante compare tra i preferiti dell'utente.
     *
     * @param token token di sessione dell'utente
     * @param idUt identificativo dell'utente
     * @param idRis identificativo del ristorante
     * @return {@code true} se è preferito, {@code false} altrimenti
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi richiesti
     */
    @Override
    public boolean controlloPreferito(String token,int idUt, int idRis) throws RemoteException, PermessoNegatoException {
        
        verificaPermessi(token, Ruolo.CLIENTE);

        try {
            return managerDB.getPreferitoDAO().controlloPreferito(idUt, idRis);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante il controllo del preferito\n" + e.getMessage());
        }
    }

    /**
     * Aggiunge un ristorante ai preferiti di un utente.
     *
     * @param token token di sessione dell'utente
     * @param idUt identificativo dell'utente
     * @param idRis identificativo del ristorante
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi richiesti
     */
    @Override
    public void aggiungiPreferito(String token, int idUt, int idRis) throws RemoteException, PermessoNegatoException {

        verificaPermessi(token, Ruolo.CLIENTE);

        try {
            managerDB.getPreferitoDAO().aggiungiPreferito(idUt, idRis);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta del preferito\n" + e.getMessage());
        }
    }

    /**
     * Rimuove un ristorante dalla lista dei preferiti di un utente.
     *
     * @param token token di sessione dell'utente
     * @param idUt identificativo dell'utente
     * @param idRis identificativo del ristorante
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi richiesti
     */
    @Override
    public void rimuoviPreferito(String token, int idUt, int idRis) throws RemoteException, PermessoNegatoException {
        
        verificaPermessi(token, Ruolo.CLIENTE);

        try {
            managerDB.getPreferitoDAO().rimuoviPreferito(idUt, idRis);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la rimozione del preferito\n" + e.getMessage());
        }
    }
    
}
