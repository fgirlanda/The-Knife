package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.servizi_int.RecensioniServiceInt;

public class RecensioniServiceImp extends UnicastRemoteObject implements RecensioniServiceInt {

    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;

    public RecensioniServiceImp(ManagerDB managerDB) throws RemoteException {
        super();
        this.managerDB = managerDB;
    }

    @Override
    public void aggiungiRecensione(Recensione recensione, Ristorante ristorante) throws RemoteException {
        try {
            managerDB.getRecensioneDAO().aggiungiRecensione(recensione);
            ristorante.aggiungiRecensione(recensione);
            managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta della recensione\n" + e.getMessage());
        }
    }

    @Override
    public void rimuoviRecensione(Recensione recensione, Ristorante ristorante) throws RemoteException {
        try {
            managerDB.getRecensioneDAO().rimuoviRecensione(recensione);
            ristorante.rimuoviRecensione(recensione);
            managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la rimozione della recensione\n" + e.getMessage());
        }
    }

    @Override
    public boolean modificaRecensione(int idRec, String nuovoTesto, int nuovoVoto)
            throws RemoteException{
        try {
            return managerDB.getRecensioneDAO().modificaRecensione(idRec, nuovoTesto, nuovoVoto);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la modifica della recensione\n" + e.getMessage());
        }
    }

    @Override
    public boolean esisteRecensione(int idUtente, int idRistorante) throws RemoteException {
        try {
            return managerDB.getRecensioneDAO().esisteRecensione(idUtente, idRistorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la verifica dell'esistenza della recensione\n" + e.getMessage());
        }
    }

    @Override
    public List<Recensione> trovaPerUtenteConRistorante(int idUtente) throws RemoteException{
        try {
            return managerDB.getRecensioneDAO().trovaPerUtenteConRistorante(idUtente);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca delle recensioni\n" + e.getMessage());
        }
    }

    @Override
    public boolean aggiungiRisposta(Recensione recensione, String risposta) throws RemoteException {
        try {
            return managerDB.getRecensioneDAO().aggiungiRisposta(recensione, risposta);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta della risposta\n" + e.getMessage());
        }
    }
    
}
