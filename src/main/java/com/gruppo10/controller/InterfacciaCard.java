package com.gruppo10.controller;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public interface InterfacciaCard<T> {
    void setStage(Stage stage);

    void setDati();

    void setItem(T item, VBox contenitore);
}
