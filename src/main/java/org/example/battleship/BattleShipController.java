package org.example.battleship;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.util.Objects;

public class BattleShipController {
    private Game game;

    @FXML
    private Button startButton;

    @FXML
    private GridPane playerGrid;

    @FXML
    private GridPane opponentGrid;

    @FXML
    private Label turnLabel;

    @FXML
    private Label winnerLabel;

    @FXML
    protected void start() {
        game = new Game();
        winnerLabel.textProperty().set("");
        turnLabel.textProperty().set("Player");
        startButton.textProperty().set("restart");
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (game.getCurrentPlayer().getGameBoard().getCell(col, row).isOccupied())
                    game.paintCell(playerGrid, col, row, 1);
                else
                    game.paintCell(playerGrid, col, row, 0);
                game.paintCell(opponentGrid, col, row, 0);
                opponentGrid.setOnMouseClicked(this::handlePlayerClick);
            }
        }
    }

    private void handlePlayerClick(MouseEvent eventM) {
        int x = (int) eventM.getX() / 31 < 10 ? (int) eventM.getX() / 31 : 9;
        int y = (int) eventM.getY() / 31 < 10 ? (int) eventM.getY() / 31 : 9;
        if (Objects.equals(game.getCurrentPlayer().getPlayerName(), "Player") && game.isInProgress()) {
            game.shoot(opponentGrid, x, y);
            turnLabel.textProperty().set(game.getCurrentPlayer().getPlayerName());
            processNextTurn();
        }
        if(game.getCurrentPlayer().getGameBoard().shipsAlive() == 0 && !game.isInProgress())
            winnerLabel.textProperty().set("Winner: " + game.getOpponentPlayer().getPlayerName());
        else if(game.getOpponentPlayer().getGameBoard().shipsAlive() == 0 && !game.isInProgress())
            winnerLabel.textProperty().set("Winner: " + game.getCurrentPlayer().getPlayerName());
    }

    private void processNextTurn() {
        if (Objects.equals(game.getCurrentPlayer().getPlayerName(), "Computer")) {
            turnLabel.textProperty().set("Computer");
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(1000),
                    event -> {
                        game.computerMove(playerGrid);
                        Platform.runLater(() -> turnLabel.textProperty().set(game.getCurrentPlayer().getPlayerName()));
                        if(game.isInProgress())
                            processNextTurn();
                    }
            ));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }
}