package org.example.battleship;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private final Cell[][] cells;

    private final List<Integer> ships;

    public GameBoard() {
        cells = new Cell[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                cells[x][y] = new Cell();
            }
        }
        ships = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 2, 2, 2, 3, 3, 4));
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void placeShips() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int tempIndex = rand.nextInt(ships.size());
            int shipSize = ships.get(tempIndex);
            ships.remove(tempIndex);
            int max = 0;
            while (max < 100) {
                max++;
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                boolean isVertical = rand.nextBoolean();
                if (canPlaceShip(x, y, shipSize, isVertical)) {
                    for(int j = 0; j < shipSize; j++) {
                        if (isVertical)
                            cells[x][y + j].setOccupied();
                        else
                            cells[x + j][y].setOccupied();
                    }
                    System.out.println("Ship Placed");
                    break;
                }
            }
        }
    }

    public boolean isShipAlive(int x, int y) {
            for (int i = x; i >= 0 && cells[i][y].isOccupied(); i--) {
                if (!cells[i][y].isHit())
                    return true;
            }
            for (int i = x; i < 10 && cells[i][y].isOccupied(); i++) {
                if (!cells[i][y].isHit())
                    return true;
            }
            for (int i = y; i >= 0 && cells[x][i].isOccupied(); i--) {
                if (!cells[x][i].isHit())
                    return true;
            }
            for (int i = y; i < 10 && cells[x][i].isOccupied(); i++) {
                if (!cells[x][i].isHit())
                    return true;
            }
        return false;
    }


    public int shipsAlive() {
        int count = 0;
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(cells[i][j].isOccupied() && !cells[i][j].isHit())
                    count++;
            }
        }
        return count;
    }

    private boolean canPlaceShip(int x, int y, int shipSize, boolean isVertical) {
        for(int i = 0; i < shipSize; i++) {
            if (isVertical) {
                if (y + shipSize > 10 || (cells[x][y + i].isOccupied() || isNearbyCellsOccupied(x, y + i)))
                    return false;
            }
            else if (x + shipSize > 10 || (cells[x + i][y].isOccupied() || isNearbyCellsOccupied(x + i, y)))
                return false;
            }
        return true;
    }

    private boolean isNearbyCellsOccupied(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newX = x + i;
                int newY = y + j;
                if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10)
                    if(cells[newX][newY].isOccupied())
                            return true;
            }
        }
        return false;
    }
}
