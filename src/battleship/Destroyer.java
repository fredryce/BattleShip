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
public class Destroyer extends Ship{

    public Destroyer() {
        setLength(2);
        setHealthPoint(2);
        setIsSunk(false);
        
    }
    

    @Override
    public String getShipType() {
        return "destroyer";
    }

    @Override
    public boolean okToPlaceShipAt(int row, int column, boolean hori, Ocean ocean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void placeShipAt(int row, int column, boolean hori, Ocean ocean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean shotAt(int row, int column) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSunk() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
