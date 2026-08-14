package com.gruppo10.servizi_int;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.eccezioni.PermessoNegatoException;

public interface RecensioniServiceInt extends Remote{

    Recensione aggiungiRecensione(String token,Recensione recensione, Ristorante ristorante)
            throws RemoteException, IllegalArgumentException, SQLException, PermessoNegatoException;

    void rimuoviRecensione(String token,Recensione recensione, Ristorante ristorante) throws RemoteException, PermessoNegatoException;

    boolean modificaRecensione(String token,int idRec, String nuovoTesto, int nuovoVoto) throws RemoteException, PermessoNegatoException;

    boolean esisteRecensione(int idUtente, int idRistorante) throws RemoteException;

    List<Recensione> trovaPerUtenteConRistorante(int idUtente) throws RemoteException;

    boolean aggiungiRisposta(String token, Recensione recensione, String risposta) throws RemoteException, PermessoNegatoException;

}
