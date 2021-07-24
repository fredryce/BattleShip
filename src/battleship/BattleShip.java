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
public class BattleShip extends Ship {
    

    public BattleShip() {
        
        setLength(4);
        setHealthPoint(4);
        setIsSunk(false);
        
    }
    

    @Override
    public String getShipType() {
        return "battleship";
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
