/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import lombok.Data;

@Data
public class Recensione implements InterfacciaIdentificabile{
    private int idRec;
    private String username;
    private int idUtente;
    private int idRistorante;
    private int stelle;
    private String testo;
    private String risposta;

    @Override
    public int getIdUtente(){
        return idUtente;
    }

    @Override
    public int getIdRistorante(){
        return idRistorante;
    }
}
