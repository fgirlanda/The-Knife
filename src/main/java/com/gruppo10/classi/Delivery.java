/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;
/**
 * Enumerazione che rappresenta il filtro per l'opzione di {@code delivery}.
 *
 * <p>L'enum fornisce tre possibili valori:</p>
 * <ul>
 *   <li>{@link #TUTTO} → nessun filtro applicato (accetta sia disponibile che non disponibile).</li>
 *   <li>{@link #DELIVERY_DISPONIBILE} → filtra solo le entità con delivery disponibile.</li>
 *   <li>{@link #DELIVERY_NON_DISPONIBILE} → filtra solo le entità con delivery non disponibile.</li>
 * </ul>
 *
 * <p>Il campo {@code delivery} interno utilizza un {@link Boolean} per distinguere i casi:</p>
 * <ul>
 *   <li>{@code null} → nessun filtro (corrisponde a {@code TUTTO}).</li>
 *   <li>{@code true} → delivery disponibile.</li>
 *   <li>{@code false} → delivery non disponibile.</li>
 * </ul>
 *
 * <p>Il metodo {@link #toString()} restituisce una rappresentazione testuale
 * leggibile per l'utente, utile per settare la combo box nell'interfaccia grafica:</p>
 * <ul>
 *   <li>{@code "TUTTO"} se il valore è {@code null}.</li>
 *   <li>{@code "Si"} se il delivery è disponibile.</li>
 *   <li>{@code "No"} se il delivery non è disponibile.</li>
 * </ul>
 *
 * @author Francesco Girlanda, Mattia Lambertoni, Gabriele Gallon 
 * @version 1.0
 */
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
