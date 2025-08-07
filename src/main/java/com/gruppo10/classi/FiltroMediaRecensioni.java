package com.gruppo10.classi;

public enum FiltroMediaRecensioni {
    ZERO(0),
    UNO(1),
    DUE(2),
    TRE(3),
    QUATTRO(4),
    CINQUE(5);

    private final int valore;

    FiltroMediaRecensioni(int valore) {
        this.valore = valore;
    }

    @Override
    public String toString() {
        StringBuilder stelle = new StringBuilder();

        int stellePiene = valore;

        for (int i = 0; i < stellePiene; i++) {
            stelle.append("★");
        }

        if (valore == 0.0) {
            return "TUTTO";
        }
        return stelle.toString();
    }
}
