/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.gruppo10.classi.Sessione;
import com.gruppo10.classi.Utente;
import com.gruppo10.eccezioni.UsernameGiaEsistenteException;

/**
 * Interfaccia remota per la registrazione, il login e la verifica della
 * disponibilità del backend.
 */
public interface AuthServiceInt extends Remote {

    /**
     * Registra un nuovo utente nel sistema.
     *
     * @param utente dati dell'utente da registrare
     * @return l'utente creato
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws UsernameGiaEsistenteException se lo username è già in uso
     */
    Utente registrati(Utente utente) throws RemoteException, UsernameGiaEsistenteException;

    /**
     * Esegue il login di un utente.
     *
     * @param username username dell'utente
     * @param password password criptata dell'utente
     * @return la sessione associata all'utente, oppure {@code null} se le credenziali non sono valide
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    Sessione login(String username, String password) throws RemoteException;

    /**
     * Recupera tutti gli utenti disponibili dal database.
     *
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    void trovaTutti() throws RemoteException;

}
