package org.example.battleship;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class Game {
    private Player currentPlayer;
    private Player opponentPlayer;
    private int lastComputerMoveCellX = -1;
    private int lastComputerMoveCellY = -1;
    private int lastComputerMoveIsVertical = 0; //0 - не известно, 1 - вертикальный, 2 - горизонтальный
    private boolean inProgress;


    public Game() {
        currentPlayer = new Player("Player");
        opponentPlayer = new Player("Computer");
        inProgress = true;
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                System.out.print(currentPlayer.getGameBoard().getCell(j, i).isOccupied() + "\t");
            }
            System.out.println();
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public void swapMove() {
        Player temp = currentPlayer;
        currentPlayer = opponentPlayer;
        opponentPlayer = temp;
    }

    public void stopGame() {
        inProgress = false;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void paintCell(GridPane grid, int x, int y, int color) {
        Rectangle cell = new Rectangle(30, 30);
        switch (color) {
            case 0: // blue
                cell.setStyle("-fx-fill: #26729f; -fx-stroke: black;");
                break;
            case 1: // orange
                cell.setStyle("-fx-fill: #fd8002; -fx-stroke: black;");
                break;
            case 2: // white
                opponentPlayer.getGameBoard().getCell(x, y).hit();
                cell.setStyle("-fx-fill: #ffffff; -fx-stroke: black;");
                break;
            case 3: // red
                opponentPlayer.getGameBoard().getCell(x, y).hit();
                cell.setStyle("-fx-fill: #FF0000FF; -fx-stroke: black;");
        }
        grid.add(cell, x, y);
    }

    public boolean shoot(GridPane grid, int x, int y) {
        if(!getOpponentPlayer().getGameBoard().getCell(x, y).isHit()) {
            getOpponentPlayer().getGameBoard().getCell(x, y).hit();
            if (getOpponentPlayer().getGameBoard().getCell(x, y).isOccupied()) {
                if(x > 0 && y < 9)
                    paintCell(grid, x - 1, y + 1, 2);
                if(x < 9 && y < 9)
                    paintCell(grid, x + 1, y + 1, 2);
                if(x < 9 && y > 0)
                    paintCell(grid, x + 1, y - 1, 2);
                if(x > 0 && y > 0)
                    paintCell(grid, x - 1, y - 1, 2);
                paintCell(grid, x, y, 3);
                if (!getOpponentPlayer().getGameBoard().isShipAlive(x, y))
                    sinkShip(grid, x, y);
            }
            else {
                paintCell(grid, x, y, 2);
                swapMove();
            }
            if(getCurrentPlayer().getGameBoard().shipsAlive() == 0 || getOpponentPlayer().getGameBoard().shipsAlive() == 0) {
                stopGame();
                return true;
            }
            return true;
        }
        return false;
    }
    private void sinkShip(GridPane grid, int x, int y) {
        int startX = x;
        int startY = y;

        while (startX > 0 && getOpponentPlayer().getGameBoard().getCell(startX - 1, y).isOccupied())
            startX--;

        int endX = x;
        while (endX < 9 && getOpponentPlayer().getGameBoard().getCell(endX + 1, y).isOccupied())
            endX++;

        while (startY > 0 && getOpponentPlayer().getGameBoard().getCell(x, startY - 1).isOccupied())
            startY--;

        int endY = y;
        while (endY < 9 && getOpponentPlayer().getGameBoard().getCell(x, endY + 1).isOccupied())
            endY++;

        for (int i = startX - 1; i <= endX + 1; i++) {
            for (int j = startY - 1; j <= endY + 1; j++) {
                if (i >= 0 && i <= 9 && j >= 0 && j <= 9 && !getOpponentPlayer().getGameBoard().getCell(i, j).isOccupied())
                    paintCell(grid, i, j, 2);
            }
        }
    }

    public void computerMove(GridPane grid) {
        Random rand = new Random();
        Player tempOp = opponentPlayer;
        if(lastComputerMoveCellX == -1) {
            while (true) {
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                if (shoot(grid, x, y)) {
                    if(tempOp.getGameBoard().getCell(x, y).isOccupied()) {
                        lastComputerMoveCellX = x;
                        lastComputerMoveCellY = y;
                    }
                    System.out.println("x: " + x + " y: " + y);
                    break;
                }
            }
        }
        else {
            int x = lastComputerMoveCellX;
            int y = lastComputerMoveCellY;

            sc:
            switch (lastComputerMoveIsVertical) {
                case 0:
                    while (true) {
                        switch (rand.nextInt(4)) {
                            case 0:
                                if (lastComputerMoveCellY > 0 && !getOpponentPlayer().getGameBoard().getCell(lastComputerMoveCellX, lastComputerMoveCellY - 1).isHit()) {
                                    Cell temp = getOpponentPlayer().getGameBoard().getCell(x, y - 1);
                                    shoot(grid, lastComputerMoveCellX, lastComputerMoveCellY - 1);
                                    if (temp.isOccupied())
                                        lastComputerMoveIsVertical = 1;
                                    break sc;
                                }
                            case 1:
                                if (lastComputerMoveCellX > 0 && !getOpponentPlayer().getGameBoard().getCell(lastComputerMoveCellX - 1, lastComputerMoveCellY).isHit()) {
                                    Cell temp = getOpponentPlayer().getGameBoard().getCell(x - 1, y);
                                    shoot(grid, lastComputerMoveCellX - 1, lastComputerMoveCellY);
                                    if (temp.isOccupied())
                                        lastComputerMoveIsVertical = 2;
                                    break sc;
                                }
                            case 2:
                                if (lastComputerMoveCellY < 9 && !getOpponentPlayer().getGameBoard().getCell(lastComputerMoveCellX, lastComputerMoveCellY + 1).isHit()) {
                                    Cell temp = getOpponentPlayer().getGameBoard().getCell(x, y + 1);
                                    shoot(grid, lastComputerMoveCellX, lastComputerMoveCellY + 1);
                                    if (temp.isOccupied())
                                        lastComputerMoveIsVertical = 1;
                                    break sc;
                                }
                            case 3:
                                if (lastComputerMoveCellX < 9 && !getOpponentPlayer().getGameBoard().getCell(lastComputerMoveCellX + 1, lastComputerMoveCellY).isHit()) {
                                    Cell temp = getOpponentPlayer().getGameBoard().getCell(x + 1, y);
                                    shoot(grid, lastComputerMoveCellX + 1, lastComputerMoveCellY);
                                    if (temp.isOccupied())
                                        lastComputerMoveIsVertical = 2;
                                    break sc;
                                }
                        }
                    }
                case 1:
                    if (rand.nextBoolean()) {
                        while (y > 0 && getOpponentPlayer().getGameBoard().getCell(x, y).isOccupied()) {
                            y--;
                            if (!getOpponentPlayer().getGameBoard().getCell(x, y).isHit()) {
                                shoot(grid, x, y);
                                break sc;
                            }
                        }
                        y++;
                        while (y < 9 && getOpponentPlayer().getGameBoard().getCell(x, y).isOccupied()) {
                            y++;
                            if (!getOpponentPlayer().getGameBoard().getCell(x, y).isHit()) {
                                shoot(grid, x, y);
                                break sc;
                            }
                        }
                    } else {
                        while (y < 9 && getOpponentPlayer().getGameBoard().getCell(x, y).isOccupied()) {
                            y++;
                            if (!getOpponentPlayer().getGameBoard().getCell(x, y).isHit()) {
                                shoot(grid, x, y);
                                break sc;
                            }
                        }
                        y--;
                        while (y > 0 && getOpponentPlayer().getGameBoard().getCell(x, y).isOccupied()) {
                            y--;
                            if (!getOpponentPlayer().getGameBoard().getCell(x, y).isHit()) {
                                shoot(grid, x, y);
                                break sc;
                            }
                        }
                    }
                    break;
                case 2:
                    if (rand.nextBoolean()) {
                        while (x > 0 && getOpponentPlayer().getGameBoard().getCell(x, y).isOccupied()) {
                            x--;
                            if (!getOpponentPlayer().getGameBoard().getCell(x, y).isHit()) {
                                shoot(grid, x, y);
                                break sc;
                            }
                        }
                        x++;
                        while (x < 9 && getOpponentPlayer().getGameBoard().getCell(x, y).isOccupied()) {
                            x++;
                            if (!getOpponentPlayer().getGameBoard().getCell(x, y).isHit()) {
                                shoot(grid, x, y);
                                break sc;
                            }
                        }
                    } else {
                        while (x < 9 && getOpponentPlayer().getGameBoard().getCell(x, y).isOccupied()) {
                            x++;
                            if (!getOpponentPlayer().getGameBoard().getCell(x, y).isHit()) {
                                shoot(grid, x, y);
                                break sc;
                            }
                        }
                        x--;
                        while (x > 0 && getOpponentPlayer().getGameBoard().getCell(x, y).isOccupied()) {
                            x--;
                            if (!getOpponentPlayer().getGameBoard().getCell(x, y).isHit()) {
                                shoot(grid, x, y);
                                break sc;
                            }
                        }
                    }
                    break;
            }
        }
        if (lastComputerMoveCellX >= 0 && !tempOp.getGameBoard().isShipAlive(lastComputerMoveCellX, lastComputerMoveCellY)) {
            lastComputerMoveIsVertical = 0;
            lastComputerMoveCellX = -1;
            lastComputerMoveCellY = -1;
        }
    }
}
