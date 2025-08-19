/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

public enum Delivery {
    TUTTO (null),
    DELIVERY_DISPONIBILE(true),
    DELIVERY_NON_DISPONIBILE(false);

    private Boolean delivery;

    Delivery(Boolean delivery){
        this.delivery = delivery;
    }

    @Override
    public String toString(){
        if(this.delivery == null) return "TUTTO";
        return this.delivery ? "Si" : "No";
    }
}
