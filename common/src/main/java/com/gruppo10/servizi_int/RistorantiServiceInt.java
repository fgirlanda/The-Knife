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

public interface RistorantiServiceInt extends Remote{

    void aggiungiRistorante(String token, Ristorante ristorante) throws RemoteException, PermessoNegatoException;

    void aggiornaMediaRecensioni(String token, Ristorante ristorante) throws RemoteException;

    List<Ristorante> getRistoranti() throws RemoteException;

    List<Ristorante> cercaConFiltri(String text, TipoCucina cucina, Prezzo prezzo, MediaRecensioni media,
            Delivery delivery, Prenotazione prenotazione, Coordinate cords, Distanza distanza) throws RemoteException;

    List<Ristorante> trovaPreferitiPerUtente(String token, int idUtente) throws RemoteException, PermessoNegatoException;

    List<Ristorante> trovaPerProprietario(String token, int id) throws RemoteException, PermessoNegatoException;

}
