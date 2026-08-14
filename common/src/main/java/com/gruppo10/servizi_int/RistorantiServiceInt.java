package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Delivery;
import com.gruppo10.classi.Distanza;
import com.gruppo10.classi.MediaRecensioni;
import com.gruppo10.classi.Prenotazione;
import com.gruppo10.classi.Prezzo;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.TipoCucina;
import com.gruppo10.eccezioni.PermessoNegatoException;

/**
 * Interfaccia remota per la gestione e la ricerca dei ristoranti.
 */
public interface RistorantiServiceInt extends Remote {

    /**
     * Aggiunge un nuovo ristorante nel sistema.
     *
     * @param token token di sessione del proprietario
     * @param ristorante ristorante da inserire
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi necessari
     */
    void aggiungiRistorante(String token, Ristorante ristorante) throws RemoteException, PermessoNegatoException;

    /**
     * Ricalcola e aggiorna la media delle recensioni di un ristorante.
     *
     * @param token token di sessione dell'utente
     * @param ristorante ristorante da aggiornare
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    void aggiornaMediaRecensioni(String token, Ristorante ristorante) throws RemoteException;

    /**
     * Restituisce la lista completa dei ristoranti disponibili.
     *
     * @return elenco dei ristoranti
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<Ristorante> getRistoranti() throws RemoteException;

    /**
     * Cerca i ristoranti applicando i filtri impostati dall'utente.
     *
     * @param text testo libero da cercare nel nome del ristorante
     * @param cucina tipo di cucina da includere
     * @param prezzo fascia di prezzo da includere
     * @param media soglia minima di media recensioni
     * @param delivery filtro sul delivery
     * @param prenotazione filtro sulla prenotazione online
     * @param cords coordinate dell'utente per il calcolo della distanza
     * @param distanza soglia massima di distanza
     * @return lista dei ristoranti filtrati
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<Ristorante> cercaConFiltri(String text, TipoCucina cucina, Prezzo prezzo, MediaRecensioni media,
            Delivery delivery, Prenotazione prenotazione, Coordinate cords, Distanza distanza) throws RemoteException;

    /**
     * Recupera i ristoranti preferiti da un utente.
     *
     * @param token token di sessione dell'utente
     * @param idUtente identificativo dell'utente
     * @return lista dei ristoranti preferiti
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi necessari
     */
    List<Ristorante> trovaPreferitiPerUtente(String token, int idUtente) throws RemoteException, PermessoNegatoException;

    /**
     * Recupera i ristoranti di proprietà di un ristoratore.
     *
     * @param token token di sessione del ristoratore
     * @param id identificativo del ristoratore
     * @return lista dei ristoranti del proprietario
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws PermessoNegatoException se l'utente non ha i permessi necessari
     */
    List<Ristorante> trovaPerProprietario(String token, int id) throws RemoteException, PermessoNegatoException;

}
