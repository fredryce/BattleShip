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
public class GameControl {
    Ocean player1;
    Ocean Player2;
    public GameControl(){
       
    }
     public GameControl(Ocean player1){
        this.player1 = player1;
        Player2 = setAi();
        
    }
    
    public GameControl(Ocean player1, Ocean player2){
        this.player1 = player1;
        this.Player2 = player2;
    }
    public void PlayerVsAi(){
        
        
    }

    public void setPlayer1(Ocean player1) {
        this.player1 = player1;
    }
    public Ocean setAi(){
        Ocean temp = new Ocean();
        
        
        return temp;
        
    }
    
    
    
    
    
}
