/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.Utente;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.eccezioni.PermessoNegatoException;
import com.gruppo10.permessi.SessionManager;
import com.gruppo10.servizi_int.RecensioniServiceInt;

/**
 * Implementazione del service remoto per la gestione delle recensioni dei ristoranti.
 */
public class RecensioniServiceImp extends BasicServiceImp implements RecensioniServiceInt {

    /**
     * Costruisce il service delle recensioni.
     *
     * @param managerDB gestore del database
     * @param sessionManager gestore delle sessioni
     * @throws RemoteException se si verifica un errore di inizializzazione RMI
     */
    public RecensioniServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super(managerDB, sessionManager);
    }

    /**
     * Aggiunge una nuova recensione e aggiorna la media del ristorante.
     *
     * @param token token di sessione del cliente
     * @param recensione recensione da inserire
     * @param ristorante ristorante recensito
     * @return recensione inserita con l'ID eventualmente generato dal database
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se il cliente non ha i permessi
     */
    @Override
    public Recensione aggiungiRecensione(String token, Recensione recensione, Ristorante ristorante) throws RemoteException, PermessoNegatoException {

        verificaPermessi(token, Ruolo.CLIENTE);
        
        try {
            Recensione recensioneInserita = managerDB.getRecensioneDAO().aggiungiRecensione(recensione);
            managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
            return recensioneInserita;
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta della recensione\n" + e.getMessage());
        }
    }

    /**
     * Rimuove una recensione esistente e aggiorna la media del ristorante.
     *
     * @param token token di sessione del cliente
     * @param recensione recensione da rimuovere
     * @param ristorante ristorante di cui si rimuove la recensione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se il cliente non ha i permessi
     */
    @Override
    public void rimuoviRecensione(String token, Recensione recensione, Ristorante ristorante) throws RemoteException, PermessoNegatoException {
        verificaPermessi(token, Ruolo.CLIENTE);
        try {
            managerDB.getRecensioneDAO().rimuoviRecensione(recensione);
            ristorante.rimuoviRecensione(recensione);
            managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la rimozione della recensione\n" + e.getMessage());
        }
    }

    /**
     * Modifica il testo e il voto di una recensione presente nel database.
     *
     * @param token token di sessione del cliente
     * @param idRec identificativo della recensione
     * @param nuovoTesto nuovo testo della recensione
     * @param nuovoVoto nuovo voto da assegnare
     * @return {@code true} se la modifica è andata a buon fine
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi o non è il proprietario della recensione
     */
    @Override
    public boolean modificaRecensione(String token, int idRec, String nuovoTesto, int nuovoVoto)
            throws RemoteException, PermessoNegatoException{

        try {
            Utente utenteLoggato = sessionManager.utenteDiSessione(token).orElseThrow(() -> new PermessoNegatoException("Sessione non valida"));

            if (!utenteLoggato.getRuolo().equals(Ruolo.CLIENTE) || !managerDB.getRecensioneDAO().isRecensioneOwner(idRec, utenteLoggato.getId())) {
                throw new PermessoNegatoException("Permesso negato: non hai i permessi per modificare questa recensione");
            }

            return managerDB.getRecensioneDAO().modificaRecensione(idRec, nuovoTesto, nuovoVoto);

        } catch (SQLException e) {
            throw new RemoteException("Errore durante la modifica della recensione\n" + e.getMessage());
        }
    }

    /**
     * Verifica se esiste già una recensione di un utente per un ristorante.
     *
     * @param idUtente identificativo dell'utente
     * @param idRistorante identificativo del ristorante
     * @return {@code true} se la recensione esiste
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    @Override
    public boolean esisteRecensione(int idUtente, int idRistorante) throws RemoteException {
        try {
            return managerDB.getRecensioneDAO().esisteRecensione(idUtente, idRistorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la verifica dell'esistenza della recensione\n" + e.getMessage());
        }
    }

    /**
     * Recupera tutte le recensioni lasciate da un utente, insieme al ristorante associato.
     *
     * @param idUtente identificativo dell'utente
     * @return elenco delle recensioni trovate
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    @Override
    public List<Recensione> trovaPerUtenteConRistorante(int idUtente) throws RemoteException{
        try {
            return managerDB.getRecensioneDAO().trovaPerUtenteConRistorante(idUtente);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca delle recensioni\n" + e.getMessage());
        }
    }

    /**
     * Aggiunge una risposta del ristoratore a una recensione.
     *
     * @param token token di sessione del ristoratore
     * @param recensione recensione da aggiornare
     * @param risposta testo della risposta
     * @return {@code true} se l'operazione è andata a buon fine
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se il ristoratore non ha i permessi
     */
    @Override
    public boolean aggiungiRisposta(String token, Recensione recensione, String risposta) throws RemoteException, PermessoNegatoException {
        
        Utente utenteLoggato = sessionManager.utenteDiSessione(token).orElseThrow(() -> new PermessoNegatoException("Sessione non valida"));

        if (!utenteLoggato.getRuolo().equals(Ruolo.RISTORATORE) ||
            !managerDB.getRistoranteDAO().isRistoranteOwner(recensione.getRistorante().getIdUtente(), utenteLoggato.getId())) {
            throw new PermessoNegatoException("Permesso negato: non hai i permessi per rispondere a questa recensione");
        }

        try {
            return managerDB.getRecensioneDAO().aggiungiRisposta(recensione, risposta);

        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta della risposta\n" + e.getMessage());
        }
    }
    
}
