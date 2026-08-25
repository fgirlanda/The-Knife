package com.gruppo10.database;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Delivery;
import com.gruppo10.classi.Distanza;
import com.gruppo10.classi.MediaRecensioni;
import com.gruppo10.classi.Prenotazione;
import com.gruppo10.classi.Prezzo;
import com.gruppo10.classi.TipoCucina;

/** Semplice verifica in sola lettura della connessione e dei DAO. */
public class TestConnection {

        /**
         * Esegue la verifica manuale della connessione e dei DAO disponibili.
         *
         * @param args argomenti della riga di comando, non utilizzati
         * @throws Exception se la connessione o una lettura fallisce
         */
    public static void main(String[] args) throws Exception {
        RistoranteDAO ristoranteDAO = new RistoranteDAO();
        System.out.println("Utenti: " + new UtenteDAO().trovaTutti().size());
        System.out.println("Ristoranti: " + ristoranteDAO.trovaTutti().size());
        System.out.println("Recensioni: " + new RecensioneDAO().trovaTutte().size());
        System.out.println("Recensione cliente 2/ristorante 2: "
                + new RecensioneDAO().esisteRecensione(2, 2));
        System.out.println("Recensioni cliente 2 con ristorante: "
                + new RecensioneDAO().trovaPerUtenteConRistorante(2).size());
        System.out.println("Preferiti: " + new PreferitoDAO().trovaTutti().size());
        System.out.println("Ristoranti preferiti cliente 2: "
                + ristoranteDAO.trovaPreferitiPerUtente(2).size());
        System.out.println("Ricerca senza filtri: " + ristoranteDAO.cercaConFiltri(
                "", TipoCucina.TUTTO, Prezzo.TUTTO, MediaRecensioni.TUTTO,
                Delivery.TUTTO, Prenotazione.TUTTO, null, Distanza.OLTRE).size());
        System.out.println("Prezzo massimo livello 2: " + ristoranteDAO.cercaConFiltri(
                "", TipoCucina.TUTTO, Prezzo.€€, MediaRecensioni.TUTTO,
                Delivery.TUTTO, Prenotazione.TUTTO, null, Distanza.OLTRE).size());
        System.out.println("Entro 10 km da Milano: " + ristoranteDAO.cercaConFiltri(
                "", TipoCucina.TUTTO, Prezzo.TUTTO, MediaRecensioni.TUTTO,
                Delivery.TUTTO, Prenotazione.TUTTO,
                new Coordinate(45.4642, 9.1900), Distanza.DIECI_KM).size());
    }
}
