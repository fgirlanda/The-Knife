package com.gruppo10.classi;

public class Preferito implements Identificabile{
    private int idUtente;
    private int idRistorante;

    public Preferito(int idUtente, int idRistorante) {
        this.idUtente = idUtente;
        this.idRistorante = idRistorante;
    }

    @Override
    public int getIdUtente() {
        return idUtente;
    }

    @Override
    public int getIdRistorante() {
        return idRistorante;
    }
}
