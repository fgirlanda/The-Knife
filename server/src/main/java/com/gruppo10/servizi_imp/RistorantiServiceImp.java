/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Delivery;
import com.gruppo10.classi.Distanza;
import com.gruppo10.classi.MediaRecensioni;
import com.gruppo10.classi.Prenotazione;
import com.gruppo10.classi.Prezzo;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.TipoCucina;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.eccezioni.PermessoNegatoException;
import com.gruppo10.permessi.SessionManager;
import com.gruppo10.servizi_int.RistorantiServiceInt;

/**
 * Implementazione del service remoto per la gestione dei ristoranti.
 */
public class RistorantiServiceImp extends BasicServiceImp implements RistorantiServiceInt {

    /**
     * Costruisce il service dei ristoranti.
     *
     * @param managerDB gestore del database
     * @param sessionManager gestore delle sessioni
     * @throws RemoteException se si verifica un errore di inizializzazione RMI
     */
    public RistorantiServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super(managerDB, sessionManager);
    }

    /**
     * Aggiunge un nuovo ristorante nel sistema.
     *
     * @param token token di sessione del ristoratore
     * @param ristorante ristorante da creare
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se il ristoratore non ha i permessi
     */
    @Override
    public void aggiungiRistorante(String token, Ristorante ristorante) throws RemoteException, PermessoNegatoException {

        verificaPermessi(token, Ruolo.RISTORATORE);

        try {
            managerDB.getRistoranteDAO().aggiungiRistorante(ristorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta del ristorante\n" + e.getMessage());
        }
    }

    /**
     * Aggiorna la media delle recensioni del ristorante specificato.
     *
     * @param token token di sessione dell'utente
     * @param ristorante ristorante da aggiornare
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    @Override
    public void aggiornaMediaRecensioni(String token,Ristorante ristorante) throws RemoteException {
        try {
            managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiornamento della media delle recensioni\n" + e.getMessage());
        }
    }

    /**
     * Recupera tutti i ristoranti disponibili.
     *
     * @return lista di tutti i ristoranti
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    @Override
    public List<Ristorante> getRistoranti() throws RemoteException{
        try {
            return managerDB.getRistoranteDAO().trovaTutti();
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca dei ristoranti\n" + e.getMessage());
        }
    }

    /**
     * Cerca ristoranti applicando i filtri specificati.
     *
     * @param text testo da cercare nel nome
     * @param cucina filtro per tipo di cucina
     * @param prezzo filtro per fascia di prezzo
     * @param media filtro per media recensioni minima
     * @param delivery filtro per disponibilità del delivery
     * @param prenotazione filtro per disponibilità della prenotazione online
     * @param cords coordinate dell'utente per il calcolo della distanza
     * @param distanza soglia massima di distanza
     * @return lista dei ristoranti matching i filtri
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    @Override
    public List<Ristorante> cercaConFiltri(String text, TipoCucina cucina, Prezzo prezzo, MediaRecensioni media,
            Delivery delivery, Prenotazione prenotazione, Coordinate cords, Distanza distanza)
            throws RemoteException{
        try {
            return managerDB.getRistoranteDAO().cercaConFiltri(text, cucina, prezzo, media, delivery, prenotazione, cords, distanza);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca dei ristoranti\n" + e.getMessage());
        }
    }

    /**
     * Recupera i ristoranti preferiti di un cliente.
     *
     * @param token token di sessione del cliente
     * @param idUtente identificativo del cliente
     * @return lista dei ristoranti preferiti
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se il cliente non ha i permessi
     */
    @Override
    public List<Ristorante> trovaPreferitiPerUtente(String token, int idUtente) throws RemoteException, PermessoNegatoException{

        verificaPermessi(token, Ruolo.CLIENTE);
        
        try {
            return managerDB.getRistoranteDAO().trovaPreferitiPerUtente(idUtente);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca dei ristoranti preferiti\n" + e.getMessage());
        }
    }

    /**
     * Recupera i ristoranti di proprietà di un dato ristoratore.
     *
     * @param token token di sessione del ristoratore
     * @param idUtente identificativo del ristoratore
     * @return lista dei ristoranti del proprietario
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se il ristoratore non ha i permessi
     */
    @Override
    public List<Ristorante> trovaPerProprietario(String token, int idUtente) throws RemoteException, PermessoNegatoException{

        verificaPermessi(token, Ruolo.RISTORATORE);

        try {
            return managerDB.getRistoranteDAO().trovaPerProprietario(idUtente);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca dei ristoranti del proprietario\n" + e.getMessage());
        }
    }
    
}
