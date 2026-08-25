/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.eccezioni.PermessoNegatoException;

/**
 * Interfaccia remota per la gestione delle recensioni dei ristoranti.
 */
public interface RecensioniServiceInt extends Remote {

    /**
     * Aggiunge una nuova recensione a un ristorante.
     *
     * @param token token di sessione dell'utente autenticato
     * @param recensione recensione da salvare
     * @param ristorante ristorante associato alla recensione
     * @return la recensione salvata, con eventuale identificativo generato dal database
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws IllegalArgumentException se i dati della recensione non sono validi
     * @throws SQLException se il salvataggio nel database fallisce
     * @throws PermessoNegatoException se l'utente non ha i permessi necessari
     */
    Recensione aggiungiRecensione(String token,Recensione recensione, Ristorante ristorante)
            throws RemoteException, IllegalArgumentException, SQLException, PermessoNegatoException;

    /**
     * Rimuove una recensione esistente.
     *
     * @param token token di sessione dell'utente autenticato
     * @param recensione recensione da rimuovere
     * @param ristorante ristorante cui appartiene la recensione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi necessari
     */
    void rimuoviRecensione(String token,Recensione recensione, Ristorante ristorante) throws RemoteException, PermessoNegatoException;

    /**
     * Modifica testo e voto di una recensione esistente.
     *
     * @param token token di sessione dell'utente autenticato
     * @param idRec identificativo della recensione
     * @param nuovoTesto nuovo testo della recensione
     * @param nuovoVoto nuovo voto assegnato
     * @return {@code true} se la modifica è avvenuta con successo
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi necessari
     */
    boolean modificaRecensione(String token,int idRec, String nuovoTesto, int nuovoVoto) throws RemoteException, PermessoNegatoException;

    /**
     * Verifica se un utente ha già lasciato una recensione per un ristorante.
     *
     * @param idUtente identificativo dell'utente
     * @param idRistorante identificativo del ristorante
     * @return {@code true} se la recensione esiste
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    boolean esisteRecensione(int idUtente, int idRistorante) throws RemoteException;

    /**
     * Recupera tutte le recensioni lasciate da un utente, con il ristorante associato.
     *
     * @param idUtente identificativo dell'utente
     * @return lista delle recensioni trovate
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<Recensione> trovaPerUtenteConRistorante(int idUtente) throws RemoteException;

    /**
     * Aggiunge una risposta del ristoratore a una recensione.
     *
     * @param token token di sessione del ristoratore autenticato
     * @param recensione recensione da aggiornare
     * @param risposta testo della risposta
     * @return {@code true} se la risposta è stata inserita correttamente
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi necessari
     */
    boolean aggiungiRisposta(String token, Recensione recensione, String risposta) throws RemoteException, PermessoNegatoException;

}
