 package com.gruppo10.classi;

public enum FiltroMediaRecensioni {
    ZERO(0.0),
    //ZERO_MEZZO(0.5),
    UNO(1.0),
    //UNO_MEZZO(1.5),
    DUE(2.0),
    //DUE_MEZZO(2.5),
    TRE(3.0),
    //TRE_MEZZO(3.5),
    QUATTRO(4.0),
    //QUATTRO_MEZZO(4.5),
    CINQUE(5.0);

    private final double valore;

    FiltroMediaRecensioni(double valore) {
        this.valore = valore;
    }

    public String toStelle() {
    StringBuilder stelle = new StringBuilder();

    int stellePiene = (int) valore;
    boolean mezzo = (valore - stellePiene) >= 0.5;

    // Stelle piene
    for (int i = 0; i < stellePiene; i++) {
        stelle.append("★");
    }

    if(valore == 0.0) {
        return "TUTTO";
    }

    // // mezzastella
    // if (mezzo) {
    //     stelle.append("⯨");  // simbolo mezzastella
    // }

    // // stelle vuote
    // int totale = stellePiene + (mezzo ? 1 : 0);
    // for (int i = totale; i < 5; i++) {
    //     stelle.append("☆");
    // }

    return stelle.toString();
    
    }

    @Override
    public String toString() {
        return toStelle();
    }
 }
