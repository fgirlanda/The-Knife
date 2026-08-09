package com.gruppo10;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientTK extends Application {

    ClientContext clientContext;

    @Override
    public void start(Stage stage) throws Exception {
        clientContext = new ClientContext();
        clientContext.connetti();
    }  
}
