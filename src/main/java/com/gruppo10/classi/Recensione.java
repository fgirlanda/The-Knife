package com.gruppo10.classi;

import lombok.Data;

@Data
public class Recensione {
    private Utente autore;
    private double punteggio;
    private String testo;
}
