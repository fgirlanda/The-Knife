package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;

public interface RecensioniServiceInt extends Remote{

    void aggiungiRecensione(Recensione recensione, Ristorante ristorante)
            throws RemoteException, IllegalArgumentException, SQLException;

    void rimuoviRecensione(Recensione recensione, Ristorante ristorante) throws RemoteException;

    boolean modificaRecensione(int idRec, String nuovoTesto, int nuovoVoto) throws RemoteException;

    boolean esisteRecensione(int idUtente, int idRistorante) throws RemoteException;

    List<Recensione> trovaPerUtenteConRistorante(int id) throws RemoteException;

    boolean aggiungiRisposta(Recensione recensione, String risposta) throws RemoteException;

}
