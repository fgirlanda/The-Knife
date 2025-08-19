/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

public enum MediaRecensioni {
    TUTTO(0),
    UNO(1),
    DUE(2),
    TRE(3),
    QUATTRO(4),
    CINQUE(5);

    private final int valore;

    MediaRecensioni(int valore) {
        this.valore = valore;
    }

    @Override
    public String toString() {
        if(this.valore == 0) return "TUTTO";
        return "★".repeat(this.valore) + "+";
    }

    public int getSoglia(){
        return this.valore;
    }
}
