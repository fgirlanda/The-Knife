package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.gruppo10.eccezioni.PermessoNegatoException;

/**
 * Interfaccia remota per la gestione dei ristoranti preferiti di un utente.
 */
public interface PreferitiServiceInt extends Remote {

    /**
     * Verifica se un utente ha già aggiunto un ristorante ai preferiti.
     *
     * @param token token di sessione dell'utente
     * @param idUt identificativo dell'utente
     * @param idRis identificativo del ristorante
     * @return {@code true} se il ristorante è tra i preferiti, {@code false} altrimenti
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi richiesti
     */
    boolean controlloPreferito(String token,int idUt, int idRis) throws RemoteException, PermessoNegatoException;

    /**
     * Aggiunge un ristorante alla lista dei preferiti di un utente.
     *
     * @param token token di sessione dell'utente
     * @param idUt identificativo dell'utente
     * @param idRis identificativo del ristorante
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi richiesti
     */
    void aggiungiPreferito(String token, int idUt, int idRis) throws RemoteException, PermessoNegatoException;

    /**
     * Rimuove un ristorante dalla lista dei preferiti di un utente.
     *
     * @param token token di sessione dell'utente
     * @param idUt identificativo dell'utente
     * @param idRis identificativo del ristorante
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi richiesti
     */
    void rimuoviPreferito(String token, int idUt, int idRis) throws RemoteException, PermessoNegatoException;

}
