package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Utente;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.eccezioni.PermessoNegatoException;
import com.gruppo10.permessi.SessionManager;
import com.gruppo10.servizi_int.RecensioniServiceInt;

public class RecensioniServiceImp extends BasicServiceImp implements RecensioniServiceInt {

    public RecensioniServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super(managerDB, sessionManager);
    }

    @Override
    public void aggiungiRecensione(String token, Recensione recensione, Ristorante ristorante) throws RemoteException, PermessoNegatoException {

        verificaPermessi(token, "CLIENTE");
        
        try {
            managerDB.getRecensioneDAO().aggiungiRecensione(recensione);
            ristorante.aggiungiRecensione(recensione);
            managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta della recensione\n" + e.getMessage());
        }
    }

    @Override
    public void rimuoviRecensione(String token, Recensione recensione, Ristorante ristorante) throws RemoteException, PermessoNegatoException {
        verificaPermessi(token, "CLIENTE");
        try {
            managerDB.getRecensioneDAO().rimuoviRecensione(recensione);
            ristorante.rimuoviRecensione(recensione);
            managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la rimozione della recensione\n" + e.getMessage());
        }
    }

    @Override
    public boolean modificaRecensione(String token, int idRec, String nuovoTesto, int nuovoVoto)
            throws RemoteException, PermessoNegatoException{

        try {
            Utente utenteLoggato = sessionManager.utenteDiSessione(token).orElseThrow(() -> new PermessoNegatoException("Sessione non valida"));

            if (!utenteLoggato.getRuolo().equals("CLIENTE") || !managerDB.getRecensioneDAO().isRecensioneOwner(idRec, utenteLoggato.getId())) {
                throw new PermessoNegatoException("Permesso negato: non hai i permessi per modificare questa recensione");
            }

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
    public boolean aggiungiRisposta(String token, Recensione recensione, String risposta) throws RemoteException, PermessoNegatoException {
        
        Utente utenteLoggato = sessionManager.utenteDiSessione(token).orElseThrow(() -> new PermessoNegatoException("Sessione non valida"));

        if (!utenteLoggato.getRuolo().equals("RISTORATORE") ||
            !managerDB.getRistoranteDAO().isRistoranteOwner(recensione.getRistorante().getId(), utenteLoggato.getId())) {
            throw new PermessoNegatoException("Permesso negato: non hai i permessi per rispondere a questa recensione");
        }

        try {
            return managerDB.getRecensioneDAO().aggiungiRisposta(recensione, risposta);

        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta della risposta\n" + e.getMessage());
        }
    }
    
}
