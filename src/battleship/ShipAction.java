/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

/**
 *
 * @author xwang2945
 */
public interface ShipAction {
    public String getShipType();
    public boolean okToPlaceShipAt(int row, int column, boolean hori, Ocean ocean);
    public void placeShipAt(int row, int column, boolean hori, Ocean ocean);
    public boolean shotAt(int row, int column);
    public boolean isSunk();
}
