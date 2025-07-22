package com.gruppo10.classi;

import lombok.Data;

@Data
public class Recensione {
    private int idRec;
    private int idUtente;
    private int idRis; 
    private double voto;
    private String testo;
    private String risposta;
}
