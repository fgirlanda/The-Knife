package com.gruppo10.classi;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public interface CardController<T> {
    void setStage(Stage stage);
    void setDati();
    void setItem(T item);
}

