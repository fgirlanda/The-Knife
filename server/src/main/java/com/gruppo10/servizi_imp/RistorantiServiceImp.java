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
import com.gruppo10.servizi_int.RistorantiServiceInt;

public class RistorantiServiceImp extends UnicastRemoteObject implements RistorantiServiceInt {

    private static final long serialVersionUID = 1L;

    ManagerDB managerDB;

    public RistorantiServiceImp(ManagerDB managerDB) throws RemoteException {
        super();
        this.managerDB = managerDB;
    }

    @Override
    public void aggiungiRistorante(Ristorante ristorante) throws RemoteException, IllegalArgumentException, SQLException {
        managerDB.getRistoranteDAO().aggiungiRistorante(ristorante);
    }

    @Override
    public void aggiornaMediaRecensioni(Ristorante ristorante) throws RemoteException, SQLException {
        managerDB.getRistoranteDAO().aggiornaMediaRecensioni(ristorante);
    }

    @Override
    public List<Ristorante> getRistoranti() throws RemoteException, SQLException {
        return managerDB.getRistoranteDAO().trovaTutti();
    }

    @Override
    public List<Ristorante> cercaConFiltri(String text, TipoCucina cucina, Prezzo prezzo, MediaRecensioni media,
            Delivery delivery, Prenotazione prenotazione, Coordinate cords, Distanza distanza)
            throws RemoteException, SQLException {
        return managerDB.getRistoranteDAO().cercaConFiltri(text, cucina, prezzo, media, delivery, prenotazione, cords, distanza);
    }

    @Override
    public List<Ristorante> trovaPreferitiPerUtente(int idUtente) throws RemoteException, SQLException {
        return managerDB.getRistoranteDAO().trovaPreferitiPerUtente(idUtente);
    }

    @Override
    public List<Ristorante> trovaPerProprietario(int idUtente) throws RemoteException, SQLException {
        return managerDB.getRistoranteDAO().trovaPerProprietario(idUtente);
    }
    
}
