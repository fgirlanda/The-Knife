package com.gruppo10.servizi_imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Delivery;
import com.gruppo10.classi.Distanza;
import com.gruppo10.classi.MediaRecensioni;
import com.gruppo10.classi.Prenotazione;
import com.gruppo10.classi.Prezzo;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.TipoCucina;
import com.gruppo10.database.ManagerDB;
import com.gruppo10.permessi.SessionManager;
import com.gruppo10.servizi_int.RistorantiServiceInt;

public class RistorantiServiceImp extends UnicastRemoteObject implements RistorantiServiceInt {

    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;
    SessionManager sessionManager;

    public RistorantiServiceImp(ManagerDB managerDB, SessionManager sessionManager) throws RemoteException {
        super();
        this.managerDB = managerDB;
        this.sessionManager = sessionManager;
    }

    @Override
    public void aggiungiRistorante(String token, Ristorante ristorante) throws RemoteException {
        try {
            managerDB.getRistoranteDAO().aggiungiRistorante(ristorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiunta del ristorante\n" + e.getMessage());
        }
    }

    @Override
    public void aggiornaMediaRecensioni(String token,Ristorante ristorante) throws RemoteException {
        try {
            managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'aggiornamento della media delle recensioni\n" + e.getMessage());
        }
    }

    @Override
    public List<Ristorante> getRistoranti() throws RemoteException{
        try {
            return managerDB.getRistoranteDAO().trovaTutti();
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca dei ristoranti\n" + e.getMessage());
        }
    }

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

    @Override
    public List<Ristorante> trovaPreferitiPerUtente(String token, int idUtente) throws RemoteException{
        try {
            return managerDB.getRistoranteDAO().trovaPreferitiPerUtente(idUtente);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca dei ristoranti preferiti\n" + e.getMessage());
        }
    }

    @Override
    public List<Ristorante> trovaPerProprietario(String token, int idUtente) throws RemoteException{
        try {
            return managerDB.getRistoranteDAO().trovaPerProprietario(idUtente);
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca dei ristoranti del proprietario\n" + e.getMessage());
        }
    }
    
}
