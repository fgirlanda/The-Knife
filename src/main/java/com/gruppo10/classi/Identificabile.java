package com.gruppo10.classi;

/**
 * Interfaccia che definisce i metodi per ottenere gli identificatori univoci
 * di entità coinvolte nel sistema.
 * 
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 1.0
 */
public interface Identificabile {

    /**
     * Restituisce l'identificatore univoco dell'utente associato all'oggetto.
     *
     * @return ID dell'utente
     */
    int getIdUtente();

    /**
     * Restituisce l'identificatore univoco del ristorante associato all'oggetto.
     *
     * @return ID del ristorante
     */
    int getIdRistorante();
}