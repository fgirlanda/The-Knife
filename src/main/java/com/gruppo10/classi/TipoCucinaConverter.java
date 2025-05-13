package com.gruppo10.classi;
import com.opencsv.bean.AbstractBeanField;

public class TipoCucinaConverter extends AbstractBeanField<TipoCucina, String> {

    @Override
    protected Object convert(String value) {
        try {
            return TipoCucina.valueOf(value.toUpperCase()); // Conversione sicura
        } catch (IllegalArgumentException e) {
            return TipoCucina.INTERNAZIONALE; // Valore di default se non riconosciuto
        }
    }

}
