package com.gruppo10.classi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoordinateTest {

    public static void main(String[] args) {
        try {
            Coordinate c1 = new Coordinate("Via santa maria 8, 21020 Villadosia VA");
            Coordinate c2 = new Coordinate("via Roma 1, Gallarate, 21013, Italy");
            System.out.println("Coordinate c1: " + c1.getLat() + ", " + c1.getLon());
            System.out.println("Coordinate c2: " + c2.getLat() + ", " + c2.getLon());
            System.out.println("Distanza: " + c1.calcolaDistanza(c2) + " km");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
