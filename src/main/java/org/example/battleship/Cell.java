package org.example.battleship;

public class Cell {
    private boolean occupied;
    private boolean isHit;

    public Cell() {
        occupied = false;
        isHit = false;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied() {
        occupied = true;
    }

    public boolean isHit() {
        return isHit;
    }

    public void hit() {
        isHit = true;
    }
}
