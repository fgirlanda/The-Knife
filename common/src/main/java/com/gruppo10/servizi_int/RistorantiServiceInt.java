package com.gruppo10.servizi_int;

import java.rmi.Remote;
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
import com.gruppo10.classi.TipoCucina;

public interface RistorantiServiceInt extends Remote{

    void aggiungiRistorante(Ristorante ristorante) throws RemoteException, IllegalArgumentException, SQLException;

    void aggiornaMediaRecensioni(Ristorante ristorante) throws RemoteException, SQLException;

    List<Ristorante> getRistoranti() throws RemoteException, SQLException;

    List<Ristorante> cercaConFiltri(String text, TipoCucina cucina, Prezzo prezzo, MediaRecensioni media,
            Delivery delivery, Prenotazione prenotazione, Coordinate cords, Distanza distanza) throws RemoteException, SQLException;

    List<Ristorante> trovaPreferitiPerUtente(int idUtente) throws RemoteException, SQLException;

    List<Ristorante> trovaPerProprietario(int id) throws RemoteException, SQLException;

}
