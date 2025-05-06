package com.gruppo10.classi;

import com.opencsv.bean.AbstractBeanField;

public class RuoloConverter extends AbstractBeanField<Utente.Ruolo, String> {

    @Override
    protected Object convert(String value) {
        try {
            return Utente.Ruolo.valueOf(value.toUpperCase()); // Conversione sicura
        } catch (IllegalArgumentException e) {
            return Utente.Ruolo.NON_REGISTRATO; // Valore di default se non riconosciuto
        }
    }
}
