package com.gruppo10.classi;

import javafx.stage.Stage;

public interface CardController<T> {
    void setStage(Stage stage);
    void setDati();
    void setItem(T item);
}

