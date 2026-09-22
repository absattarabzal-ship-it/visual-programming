package com.example.lab4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LayoutApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LayoutApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        stage.setTitle("Каталог фильмов — Лабораторная работа №4");
        stage.setScene(scene);

        // Ограничения минимального размера окна по пункту 13 методички
        stage.setMinWidth(760);
        stage.setMinHeight(520);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}