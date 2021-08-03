/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author xwang2945
 */
public class Ocean{
    private Ship[][] oceanArray;
    private int[][] shipTest; //this one should be passed and processed
    private int[][] objectID; //pass to server to tell which ones are related
    int battleShipNum = 1;
    int cruisers = 2;
    int destroyers = 3;
    int submarines = 4;
    int total = cruisers + battleShipNum + destroyers + submarines;
    
    private BattleShip[] myBS = new BattleShip[1]; //length based on the number of the type of ship on the board
    private Cruiser[] myCS = new Cruiser[2];     
    private Destroyer[] myDS = new Destroyer[3];
    private Submarine[] mySB = new Submarine[4];
    
    SecureRandom mySR = new SecureRandom();
    
    private String oceanName;
    
    public Ocean(String oceanName){
        oceanArray = new Ship[10][10];
        shipTest = new int[10][10];
        objectID = new int[10][10];
        this.oceanName = oceanName;
        
        
    }
  
	public int[][] getObjectID() {
		return objectID;
	}

	public void setObjectID(int[][] objectID) {
		this.objectID = objectID;
	}

	public String name_getter() {
		return(oceanName);
    
    }
    public void name_setter(String oceanName) {
    	this.oceanName = oceanName;
    }
    
   
    
    public void print()
    {
        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                if (isOccupied(i, j)) {
                    if (oceanArray[i][j].getShipType().equals("battleship")) {
                        shipTest[i][j] = 1;     // you can change these numbers to distinguish the ship type         

                    } else if (oceanArray[i][j].getShipType().equals("cruiser")) {
                        shipTest[i][j] = 2;
                    } else if (oceanArray[i][j].getShipType().equals("submarine")) {
                        shipTest[i][j] = 4;
                    } else if (oceanArray[i][j].getShipType().equals("destroyer")) {
                        shipTest[i][j] = 3;

                    } else if (oceanArray[i][j].getShipType().equals("empty")) {
                        shipTest[i][j] = 0;
                    }
                    
                    objectID[i][j] = oceanArray[i][j].hashCode();
                }
                
            }
            
        }
    }
    public void printTest(){
        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                System.out.print(shipTest[i][j]);
            }
            System.out.println();
            
        }
    }
    public int[][] getprint() {
    	return(shipTest);
    }
    
    public void setBoard(JSONArray location, JSONArray objectidarray) {
    	//this is when passing in a board and set the object based on this board
    	HashMap<Integer, Ship> idtoindex = new HashMap<Integer, Ship>();
    	
    	int count = location.length();
        for (int i = 0; i <count ; i++) {
			try {
				JSONArray jsonArr = location.getJSONArray(i);
				for(int j = 0; j<jsonArr.length(); j++) {
					int element_value = jsonArr.getInt(j);
					
					if(element_value != 0) {
						int objectID = objectidarray.getJSONArray(i).getInt(j);
						//System.out.println(objectID);
						if(idtoindex.containsKey(objectID)){
							
							oceanArray[i][j] = idtoindex.get(objectID);
							
						}
						else {
							if(element_value==1) {
								BattleShip enemyBs = new BattleShip();
								idtoindex.put(objectID, enemyBs);
								oceanArray[i][j] = enemyBs;
							}
							else if(element_value==2) {
								Cruiser enemyCs = new Cruiser();
								idtoindex.put(objectID, enemyCs);
								oceanArray[i][j] = enemyCs;
							}
							else if(element_value==3) {
								Destroyer enemyDs = new Destroyer();
								idtoindex.put(objectID, enemyDs);
								oceanArray[i][j] = enemyDs;
							}
							else if(element_value==4) {
								Submarine enemySs = new Submarine();
								idtoindex.put(objectID, enemySs);
								oceanArray[i][j] = enemySs;
							}
							
							
						}
						
						
					}
					else {
						oceanArray[i][j] = new EmptySea();
						
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     
          }
    	
    }

    public void placeAllShipsRandomly(){
        int indexCounter = 0;
        Ship myShip = null;
        
        while (battleShipNum != 0 || cruisers != 0 || destroyers != 0 || submarines != 0) {

            if (battleShipNum != 0) {                //checks which ship doesnt equal to 0
                //System.out.println("BS iN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                myBS[indexCounter] = new BattleShip();
                myShip = myBS[indexCounter];
                

                battleShipNum--;
            } else if (cruisers != 0) {
                //System.out.println("CS iN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                myCS[indexCounter] = new Cruiser();
                myShip = myCS[indexCounter];
                

                cruisers--;
            } else if (destroyers != 0) {
                //System.out.println("DS iN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                myDS[indexCounter] =  new Destroyer();
                myShip = myDS[indexCounter];
               
                destroyers--;
            } else if (submarines != 0) {
                //System.out.println("SS iN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                
                mySB[indexCounter] = new Submarine();
                myShip = mySB[indexCounter];
               
                indexCounter++; // move on to the index of the next object in the array
                submarines--;
            }
            
            int row = mySR.nextInt(9);
            int column = mySR.nextInt(9);
            while (isOccupied(row, column) || (!upWard(row, column, myShip.getLength()) && !downWard(row, column, myShip.getLength()) && !rightHori(row, column, myShip.getLength())&& !leftHori(row, column, myShip.getLength()))) { //checks if the place is occupied if it is generate new value
                //System.out.println(row + "," + column +" entered in the loop");        //generate a new starting point if the place is occupied, or if from the starting place there is no possible placement
                row = mySR.nextInt(9);
                column = mySR.nextInt(9);
            }
            

            boolean[] possChoice = new boolean[4];                          // save the choices to the array and later generate random index to pick from the array.
            possChoice[0] = upWard(row, column, myShip.getLength());
            possChoice[1] = downWard(row, column, myShip.getLength());
            possChoice[2] = rightHori(row, column, myShip.getLength());
            possChoice[3] = leftHori(row, column, myShip.getLength());
            int choice = mySR.nextInt(3);
            while(possChoice[choice] == false){
                choice = mySR.nextInt(3);
                
            }

            int finalX;
            int finalY;
            if(choice == 0){
                for (int i = row - (myShip.getLength() - 1); i <= row; i++){
                    oceanArray[i][column] = myShip;
                }
                finalX = row - (myShip.getLength() - 1);
                finalY = column;
                placeEmpty(finalX, finalY, row, column);
                
                
            }
            else if(choice == 1){
                for (int i = row; i < row + (myShip.getLength()); i++){
                    oceanArray[i][column] = myShip;
                }
                finalX = row + (myShip.getLength() - 1);
                finalY = column;
                placeEmpty(row, column, finalX, finalY);
                
                
            }
            else if(choice == 2){
                for(int i = column; i < column + (myShip.getLength()); i++){
                    oceanArray[row][i] = myShip;
                    
                }
                finalX = row;
                finalY = column + (myShip.getLength() - 1);
                placeEmpty(row, column, finalX, finalY );
            }
            else{
                for(int i = column - (myShip.getLength() - 1); i < column; i++){
                    oceanArray[row][i] = myShip;
                    
                }
                finalX = row;
                finalY = column - (myShip.getLength() - 1);
                placeEmpty(finalX, finalY, row, column);
                
                
            }
            

        }
        for(int i = 0; i< 10; i++){             //fill the rest of the empty spots with empty sea
            for(int j = 0; j < 10; j++){
                if(!isOccupied(i,j)){
                    oceanArray[i][j] = new EmptySea();
                }
                
                
            }
        }
        

    }
    public void placeEmpty(int rowStart, int columnStart, int rowEnd, int columnEnd){  // this function place the emptySea after each boat is sucessfually placed
        
        //System.out.println(rowStart + "," +columnStart+","+ rowEnd+","+ columnEnd);
        
        if(rowStart == rowEnd){
            //System.out.println("Horizontal Alignemnt");
            //horizontal allignment
            
            if(rowStart > 0){
                //System.out.println("not in the first row");
                for(int i = columnStart; i< columnEnd + 1; i++){
                    if(!isOccupied(rowStart - 1, i))//this
                    oceanArray[rowStart - 1][i] = new EmptySea();
                    int test = rowStart -1;
                    //System.out.println("Placed at " + test + "," + i);
                }
                //after checking rows checks columns
                if(columnStart > 0){
                    if(!isOccupied(rowStart, columnStart - 1))//this
                    oceanArray[rowStart][columnStart - 1] = new EmptySea();
                    if(!isOccupied(rowStart - 1, columnStart - 1))// this
                    oceanArray[rowStart - 1][columnStart - 1] = new EmptySea();
                    
                    
                }
                if(columnEnd < 9){
                    if(!isOccupied(rowStart, columnEnd + 1))//this
                    oceanArray[rowStart][columnEnd + 1] = new EmptySea();
                    if(!isOccupied(rowStart - 1, columnEnd + 1))
                    oceanArray[rowStart -1 ][ columnEnd + 1] = new EmptySea();
                    
                }
               
            }
            if(rowStart < 9){
              
                for(int i = columnStart; i< columnEnd + 1; i++){
                    if(!isOccupied(rowStart + 1, i))
                    oceanArray[rowStart + 1][i] = new EmptySea();
                    int test = rowStart + 1;
                  
                }
                if(columnStart > 0){
                    if(!isOccupied(rowStart, columnStart - 1))
                     oceanArray[rowStart][columnStart - 1] = new EmptySea();
                    if(!isOccupied(rowStart + 1, columnStart - 1))
                     oceanArray[rowStart + 1][columnStart -1] = new EmptySea();
                    
                }
                if(columnEnd < 9){
                    if(!isOccupied(rowStart, columnEnd + 1))
                     oceanArray[rowStart][columnEnd + 1] = new EmptySea();
                    if(!isOccupied(rowStart + 1, columnEnd + 1))
                     oceanArray[rowStart + 1][columnEnd + 1] = new EmptySea();
                    
                    
                }
                
                
            }
           
            
        }
        else if(columnStart == columnEnd){
            //System.out.println("Verticle alignment");
            //verticle allignment
            if(columnStart > 0){
                for(int i = rowStart; i< rowEnd + 1; i++){
                    if(!isOccupied(i, columnStart - 1 )){
                        oceanArray[i][columnStart - 1] = new EmptySea();
                    }
        
                }
                if(rowStart > 0){
                    if(!isOccupied(rowStart - 1, columnStart))//this
                    oceanArray[rowStart - 1][columnStart] = new EmptySea();
                    if(!isOccupied(rowStart - 1, columnStart - 1))// this
                    oceanArray[rowStart - 1][columnStart - 1] = new EmptySea();
                    
                    
                    
                    
                    
                }
                if(rowEnd < 9){
                    if(!isOccupied(rowEnd + 1, columnStart))//this
                    oceanArray[rowEnd + 1][columnStart] = new EmptySea();
                    if(!isOccupied(rowEnd + 1, columnStart - 1))// this
                    oceanArray[rowEnd + 1][columnStart - 1] = new EmptySea();
                    
                }
            }
            
            
            
            
            
            
            
            
            
            if(columnStart < 9){
                 for(int i = rowStart; i< rowEnd + 1; i++){
                    if(!isOccupied(i, columnStart + 1 )){
                        oceanArray[i][columnStart + 1] = new EmptySea();
                    }
        
                }
                if(rowStart > 0){
                    if(!isOccupied(rowStart -1 , columnStart))//this
                    oceanArray[rowStart - 1][columnStart] = new EmptySea();
                    if(!isOccupied(rowStart - 1, columnStart + 1))// this
                    oceanArray[rowStart - 1][columnStart + 1] = new EmptySea();
                    
                }
                if(rowEnd < 9){
                    if(!isOccupied(rowEnd+1, columnStart))//this
                    oceanArray[rowEnd + 1][columnStart] = new EmptySea();
                    if(!isOccupied(rowEnd + 1, columnStart + 1))// this
                    oceanArray[rowEnd + 1][columnStart + 1] = new EmptySea();
                    
                }
                
            
            
            
            
            
            }
        
        
        
        
        
        
        } 
    }
    public boolean rightHori(int row, int column, int sizeBoat){      //see if the placement is possible from the starting point to the 4 possible orientations
        if(10 - sizeBoat < column){
            return false;
        }
        for(int i = column; i < column + (sizeBoat); i++){
            try {
                if (isOccupied(row, i)) {
                    return false;
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
        
    }
    public boolean leftHori(int row, int column, int sizeBoat){ 
           for(int i = column - (sizeBoat - 1); i <= column; i++){
            try {
                if (isOccupied(row, i)) {
                    return false;
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
        
        
    }
    public boolean upWard(int row, int column, int sizeBoat) {
        for (int i = row - (sizeBoat - 1); i <= row; i++) {
            try {
                if (isOccupied(i, column)) {
                    return false;
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }

        }
        return true;
    }
    public boolean downWard(int row, int column, int sizeBoat){
        for(int i = row; i <= row + (sizeBoat -1); i++)
        {
            try{
                if(isOccupied(i, column)){
                    return false;
                }
                
            }
            catch(IndexOutOfBoundsException e){
                return false;
            }
            
        }
        return true;
        
    }
    
    public boolean isOccupied(int row, int column){    //checks if the row column is accupied by a ship type
        boolean occup = false;
        if(oceanArray[row][column] != null){
            return true;
        }
    
        return occup;
        
        
    }

    public Ship[][] getOceanArray() {
        return oceanArray;
    }

    public void setOceanArray(Ship[][] oceanArray) {
        this.oceanArray = oceanArray;
    }
    
    
    
    
    
    public boolean shotAt(int row, int column){
        
        
        return true;
    }
    public int getShotsFired(){
        return 0;
    }
    public int getHitCount(){
        
     return 0;   
    }
    public int getShipsSunk(){
        return 0;
    }
    public boolean isGameOver(){
        
        return false;
    }
     
    
   
    
        
}
