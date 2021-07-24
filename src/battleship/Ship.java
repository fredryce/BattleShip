/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import java.awt.Color;

/**
 *
 * @author xwang2945
 */
public abstract class Ship implements ShipAction {
    private String name;
    private int bowRow;
    private int bowColumn;
    private int length;
    private boolean horizontal;
    private boolean[] hit  = new boolean[4];
    private int healthPoint;
    private boolean isSunk = false;

    public void setIsSunk(boolean isSunk) {
        this.isSunk = isSunk;
    }

    public boolean getIsSunk() {
        return isSunk;
    }
    
    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    

    public void setLength(int length) {
        this.length = length;
    }

    public void setBowColumn(int bowColumn) {
        this.bowColumn = bowColumn;
    }

    public void setBowRow(int bowRow) {
        this.bowRow = bowRow;
    }

    public void setHit(boolean[] hit) {
        this.hit = hit;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }
    public int getLength(){
        return length;
    }
    
    
    
}
