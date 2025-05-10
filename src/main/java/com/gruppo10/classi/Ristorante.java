package com.gruppo10.classi;

import lombok.Data;

@Data
public class Ristorante {
    private String nomeRistorante;
    private String indirizzo;
    private boolean delivery;
    private boolean prenotazioneOnline;
    private String tipoCucina;

    private Coordinate cords;
}
