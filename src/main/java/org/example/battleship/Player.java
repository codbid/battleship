package org.example.battleship;

public class Player {
    private final GameBoard gameBoard;
    private final String playerName;

    public Player(String playerName) {
        gameBoard = new GameBoard();
        gameBoard.placeShips();
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
