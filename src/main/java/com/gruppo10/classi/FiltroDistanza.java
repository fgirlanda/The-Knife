package com.gruppo10.classi;

public enum FiltroDistanza {
    DIECI_KM(10.0),

    VENTI_KM(20.0),

    TRENTA_KM(30.0),

    OLTRE(1000.0);

    private final double km;

    FiltroDistanza(double km) {
        this.km = km;
    }

    @Override
    public String toString() {
        String chilometri = "";
        switch ((int) this.km) {
            case 10:
                chilometri = "10 km";
                break;
            case 20:
                chilometri = "20 km";
                break;
            case 30:
                chilometri = "30 km";
                break;
            case 1000:
                chilometri = "50+ km";
        }

        return chilometri;
    }

    public Double getKM() {
        return this.km;
    }

}
