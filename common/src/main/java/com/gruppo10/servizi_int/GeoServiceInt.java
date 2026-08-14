package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.eccezioni.GeocodingException;

/**
 * Interfaccia remota per la geocodifica degli indirizzi e la generazione di
 * suggerimenti di completamento.
 */
public interface GeoServiceInt extends Remote {

    /**
     * Converte un indirizzo testuale in coordinate geografiche.
     *
     * @param indirizzo indirizzo da geocodificare
     * @return coordinate corrispondenti all'indirizzo
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     * @throws GeocodingException se l'indirizzo non può essere risolto
     */
    Coordinate geocodifica(String indirizzo) throws RemoteException, GeocodingException;

    /**
     * Restituisce una lista di suggerimenti per completare una query di indirizzo.
     *
     * @param query testo inserito dall'utente
     * @return lista di suggerimenti disponibili
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<String> suggerimenti(String query) throws RemoteException;

}
