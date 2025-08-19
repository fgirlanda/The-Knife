/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

public enum Distanza {
    DIECI_KM(10.0),

    VENTI_KM(20.0),

    TRENTA_KM(30.0),

    OLTRE(Double.MAX_VALUE);

    private final Double km;

    Distanza(Double km) {
        this.km = km;
    }

    @Override
    public String toString() {
        String chilometri = "";
        switch ((int)this.km.doubleValue()) {
            case 10:
                chilometri = "10 km";
                break;
            case 20:
                chilometri = "20 km";
                break;
            case 30:
                chilometri = "30 km";
                break;
            default:
                chilometri = "50+ km";
        }

        return chilometri;
    }

    public Double getKM() {
        return this.km;
    }

}
