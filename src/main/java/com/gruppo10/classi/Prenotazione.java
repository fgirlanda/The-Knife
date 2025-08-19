/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

public enum Prenotazione {
    TUTTO(null),
    PRENOTAZIONE_ONLINE_DISPONIBILE(true),
    PRENOTAZIONE_ONLINE_NON_DISPONIBILE(false);

    private Boolean prenotazione;

    Prenotazione(Boolean prenotazione){
        this.prenotazione = prenotazione;
    }

    @Override
    public String toString(){
        if(this.prenotazione == null) return "TUTTO";
        return this.prenotazione ? "Si" : "No";
    }
}
