package com.gruppo10.classi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoordinateTest {

    public static void main(String[] args) {
        try {
            Coordinate c1 = new Coordinate("Via santa maria 8, 21020 Villadosia VA");
            Coordinate c2 = new Coordinate("Via Santi Pietro e Paolo, 4, 21040 Cascine Maggio VA");
            System.out.println("Coordinate c1: " + c1.getLat() + ", " + c1.getLon());
            System.out.println("Coordinate c2: " + c2.getLat() + ", " + c2.getLon());
            System.out.println("Distanza: " + c1.calcolaDistanza(c2) + " km");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // @Test
    // void testCalcolaDistanza() throws Exception {
    //     Coordinate c1 = new Coordinate("Via santa maria 8, 21020 Villadosia VA"); //45.75194892750532, 8.727628424291378
    //     Coordinate c2 = new Coordinate("Via Santi Pietro e Paolo, 4, 21040 Cascine Maggio VA"); //45.757262061327076, 8.810769918006985
    //     assertTrue(c1.getLat() == 45.75194892750532 && c1.getLon() == 8.727628424291378);
    // }
}
