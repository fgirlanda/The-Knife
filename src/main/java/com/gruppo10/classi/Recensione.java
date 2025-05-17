package com.gruppo10.classi;

import lombok.Data;

@Data
public class Recensione {
    private int idRecensione;
    private int idCliente;
    private int idRistorante;
    private String titolo;
    private String testo;
    private String risposta;
    private double valutazione;
}
