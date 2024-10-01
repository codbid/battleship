package org.example.battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BattleShipApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BattleShipApplication.class.getResource("bs-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 420);
        stage.setTitle("Battle Ship case by codbid");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}