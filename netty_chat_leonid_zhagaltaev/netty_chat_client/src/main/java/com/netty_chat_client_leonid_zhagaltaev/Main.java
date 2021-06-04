package com.netty_chat_client_leonid_zhagaltaev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/client_gui.fxml")); // Загрузка разметки из файла
        primaryStage.setTitle("Чат");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
