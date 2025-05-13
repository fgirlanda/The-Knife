package com.gruppo10.classi;

import java.util.ArrayList;
import lombok.Data;

@Data
public class ListaRistoranti {
    private ArrayList<Ristorante> ristoranti;

    public void addRistorante(Ristorante ristorante) {
        this.ristoranti.add(ristorante);
    }
    
    public void removeRistorante(Ristorante ristorante) {
        this.ristoranti.remove(ristorante);
    }
}
