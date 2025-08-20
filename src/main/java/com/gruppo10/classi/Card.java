/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public interface Card<T> {
    void setStage(Stage stage);

    void setDati();

    void setItem(T item, VBox contenitore);
}
