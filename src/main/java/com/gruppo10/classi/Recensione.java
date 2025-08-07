package com.gruppo10.classi;

import lombok.Data;

@Data
public class Recensione {
    // private int idRec;
    private String username;
    private int idUtente;
    private int idRis;
    private int stelle;
    private String testo;
    private String risposta;
}
