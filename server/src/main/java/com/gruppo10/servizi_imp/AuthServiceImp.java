package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Optional;

import com.gruppo10.classi.Sessione;
import com.gruppo10.classi.Utente;
import com.gruppo10.eccezioni.UsernameGiaEsistenteException;
import com.gruppo10.permessi.SessionManager;
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
public class AuthServiceImp extends BasicServiceImp implements AuthServiceInt {

    /**
     * Crea il servizio di autenticazione usando il database e il gestore sessioni.
     *
     * @param managerDB gestore dei DAO
     * @param sessionManager gestore delle sessioni attive
     * @throws RemoteException se l'esportazione RMI fallisce
     */
    public AuthServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super(managerDB, sessionManager);
    }

    /**
     * Verifica le credenziali e crea una sessione per l'utente autenticato.
     *
     * @param username username dell'utente
     * @param password password da verificare
     * @return sessione creata o {@code null} se le credenziali non sono valide
     * @throws RemoteException se il database non è raggiungibile
     */
    @Override
    public Sessione login(String username, String password) throws RemoteException {
        try {
            Optional<Utente> utente = managerDB.getUtenteDAO().cercaUtente(username);
            if (utente.isPresent() && utente.get().getPassword().equals(password)) {
                String token = sessionManager.generaToken(utente.get());
                return new Sessione(utente.get(), token);
            }
            return null;
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la verifica delle credenziali\n" + e.getMessage());
        }
    }

    /**
     * Registra un nuovo utente nel database.
     *
     * @param utente dati dell'utente da registrare
     * @return utente salvato con l'ID assegnato
     * @throws RemoteException se il salvataggio fallisce
     * @throws UsernameGiaEsistenteException se lo username è già utilizzato
     */
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

    /**
     * Carica gli utenti presenti nel database per la verifica del servizio.
     *
     * @throws RemoteException se il caricamento degli utenti fallisce
     */
    @Override
    public void trovaTutti() throws RemoteException {
        try {
            managerDB.getUtenteDAO().trovaTutti();
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca di tutti gli utenti\n" + e.getMessage());
        }
    }
}