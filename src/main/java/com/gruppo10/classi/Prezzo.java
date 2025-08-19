/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

public enum Prezzo {
    TUTTO(0),
    € (1),
    €€ (2),
    €€€ (3),
    €€€€ (4);

    private int prezzo;

    Prezzo(int prezzo){
        this.prezzo = prezzo;
    }

    @Override
    public String toString(){
        if(this.prezzo == 0) return "TUTTO";
        return "€".repeat(this.prezzo);
    }
}