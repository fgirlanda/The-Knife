package com.gruppo10.classi;

import com.opencsv.bean.AbstractBeanField;

public class RuoloConverter extends AbstractBeanField<Ruolo, String> {

    @Override
    protected Object convert(String value) {
        try {
            return Ruolo.valueOf(value.toUpperCase()); // Conversione sicura
        } catch (IllegalArgumentException e) {
            return Ruolo.NON_REGISTRATO; // Valore di default se non riconosciuto
        }
    }
}
