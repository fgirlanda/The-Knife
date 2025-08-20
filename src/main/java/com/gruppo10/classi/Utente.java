/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Utente implements Identificabile{
    private int id;
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private LocalDate dataDiNascita;
    private String indirizzo;
    private Ruolo ruolo;

    private Coordinate cords;

    @Override
    public int getIdUtente(){
        return id;
    }

    @Override
    public int getIdRistorante(){
        return -1;
    }

    public void setCords(double lat, double lon) {
        this.cords = new Coordinate(lat, lon);
    }

    public void setDataDiNascita(String dataDiNascita) {
        String[] parts = dataDiNascita.split("-");
        int giorno = Integer.parseInt(parts[0]);
        int mese = Integer.parseInt(parts[1]);
        int anno = Integer.parseInt(parts[2]);
        this.dataDiNascita = LocalDate.of(anno, mese, giorno);
    }

    public void setRuolo(String ruolo) {
        try {
            this.ruolo = Ruolo.valueOf(ruolo.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.ruolo = Ruolo.NON_REGISTRATO; // Valore di default se non riconosciuto
        }
    }

    public void setRuolo(Ruolo ruolo){
        this.ruolo = ruolo;
    }
}
