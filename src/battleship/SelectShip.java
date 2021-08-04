/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.image.ImageObserver.ABORT;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
/**
 *
 * @author Fred
 */
//use call back to pass in a emit message to the server after conneted, when the call back function in the client is called, the game is ready.
class SelectShip implements ActionListener {
    
    int battle = 0; //the number of buttons needed for each ship
    int cruiser = 0;
    int destroyer = 0;
    int submarine = 0;
    int numBattle = 0;//the number of ships of each type
    int numCruiser = 0;
    int numDestroyer = 0;
    int numSubmarine = 0;
    
    int previousX = 0;
    int previousY = 0;
    
    private HashMap<String, String> enemyBoards = new HashMap<String, String>();
    
    private boolean game_ready= false; //threads can monitor changes
    
    
    public boolean isGame_ready() {
		return game_ready;
	}

	
	private Thread runSpin;; //thread to wait til the player can be ready, ungray the button
    
    private BattleShip[] myBS = new BattleShip[1]; //length based on the number of the type of ship on the board
    private Cruiser[] myCS = new Cruiser[2];     
    private Destroyer[] myDS = new Destroyer[3];
    private Submarine[] mySB = new Submarine[4];
    
    JButton[] buttons = new JButton[100];
    Ocean playerOcean = new Ocean(null);
    Ocean myOcean = new Ocean(null); //this is enemy ocean can be coverted to a list
    
    
    
    JButton myButton;
    JButton randomplace;
    private JFrame gameFrame = new JFrame();
    private FinalGUI finalGui;
    
    
    private List<SelectShip> instanceList;
    private JLabel shipsleftLabel;
    
    
    
    
    //public SelectShip(int chosenOption, Socket newSocket) {
    public SelectShip(List instanceList) {
    	this.instanceList = instanceList;
    	
    	
    	if(instanceList==null){
    		this.instanceList = new ArrayList<SelectShip>(); //keep track of all the objects created and set the first one to be the orginal
    	}
    	
    	
        
        myBS[0] = new BattleShip();
        for(int i = 0; i < myCS.length; i++){
            myCS[i] = new Cruiser();
        }
        for (int i = 0; i< myDS.length;i++){
            myDS[i] = new Destroyer();
        }
        for (int i = 0; i< mySB.length;i++){
            mySB[i] = new Submarine();
        }
        
        
        
        
        
        
        
        
        //gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameFrame.setUndecorated(false);
        gameFrame.setTitle("Choose Battle Ship Locations");
        gameFrame.setPreferredSize(new Dimension(800, 500));
        
        gameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(NetworkNew.socket!=null) {
                	NetworkNew.socket.disconnect();
                }
                new SelectOptions();
            	gameFrame.dispose();
            }
        });
        

        //myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //myFrame.setUndecorated(true);
        JPanel myPanel = new JPanel();
        JPanel selectShip = new JPanel();

        selectShip.setLayout(new GridLayout(10, 10));

        int counter = 0;

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].addActionListener(this);
            buttons[i].setText(Integer.toString(counter) + ", " + Integer.toString(i % 10));
            if (i % 10 == 9) {
                counter += 1;
            }

            selectShip.add(buttons[i]);

        }
        gameFrame.add(selectShip);
        myButton = new JButton("Set Selection");
        randomplace = new JButton("Randomly place");
        JButton reset = new JButton("Reset Board");
        shipsleftLabel = new JLabel("", JLabel.LEFT);
        //adjustLabelSize(); //adjust labelsize, this can be added to listener later
        ActionListener listener = new startListener();
        ActionListener resetListner = new ResetListener();
        
        myButton.addActionListener(listener);
        reset.addActionListener(resetListner);
        randomplace.addActionListener(new RandomListener());
        myPanel.add(shipsleftLabel, BorderLayout.WEST);
        myPanel.add(myButton);
        myPanel.add(randomplace);
        myPanel.add(reset);
        gameFrame.add(myPanel, BorderLayout.SOUTH);

        gameFrame.pack();
        gameFrame.setVisible(true);
        //JOptionPane.showMessageDialog(null, "I only HIRE Professionals to test my flawless program -anonymous Paguine NinjaPandaexpresswtfAmidoingUpat2aminthemorning /n/n/n/n/n WwwherrreismyNewline.... \n ifyouhappentofinishreadingthisijustneed2tellyouthatyoumustbereallybored....\n butatleastwesharesomethingIncommonsinceIwastedmytimetypingthis...... \n", "My FLAWLESS Program", JOptionPane.INFORMATION_MESSAGE);
        //JOptionPane.showMessageDialog(null , "PS. the previous message might contain typos so i dont suggest you finish reading it....", "LALALA", JOptionPane.INFORMATION_MESSAGE);
        //JOptionPane.showMessageDialog(null, "Set '1' BattleShip click 4 spaces! ", "BattleShip", JOptionPane.INFORMATION_MESSAGE);
        shipsleftLabel.setForeground(Color.red);
        shipsleftLabel.setText("Set '1' BattleShip click 4 spaces!");
    }
    
    
    public void adjustLabelSize() {
    	
    	Font labelFont = shipsleftLabel.getFont();
    	String labelText = shipsleftLabel.getText();

    	int stringWidth = shipsleftLabel.getFontMetrics(labelFont).stringWidth(labelText);
    	int componentWidth = shipsleftLabel.getWidth();

    	// Find out how much the font can grow in width.
    	double widthRatio = (double)componentWidth / (double)stringWidth;

    	int newFontSize = (int)(labelFont.getSize() * widthRatio);
    	int componentHeight = shipsleftLabel.getHeight();

    	// Pick a new font size so it will not be larger than the height of label.
    	int fontSizeToUse = Math.min(newFontSize, componentHeight);

    	// Set the label's font size to the newly determined size.
    	shipsleftLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
    }
    
    public void setGame_ready(boolean game_ready) {
		this.game_ready = game_ready;
		if(this.game_ready==true) {
			
		   System.out.println("Games is ready loading board...");
		   //Ocean myocean = new Ocean();    //creates the shipObject
		   if(myOcean.name_getter() == null){
			   myOcean.placeAllShipsRandomly(); //place the ships on the Ocean array
		   }
		   
           
           myOcean.print(); // transform the shipObject array into integer object array
           myOcean.printTest(); //display the integer object array.
           System.out.println();
           playerOcean.print();
           playerOcean.printTest();
           System.out.println();
           
           this.gameFrame.setTitle("Changed title");
           this.gameFrame.dispose();
           for (SelectShip temp : this.instanceList) {
               temp.gameFrame.dispose();
           }
           finalGui = new FinalGUI(myOcean, playerOcean); //pass in the ocean array into the GUI
           
			
		}
	}

    
 
    
    public FinalGUI getFinalGui() {
		return finalGui;
	}

	public void setFinalGui(FinalGUI finalGui) {
		this.finalGui = finalGui;
	}

	public void setReady(boolean value, String notfirst) {
    	System.out.println("test");
    	playerOcean.printTest();
    	System.out.println("test");
    	
    	gameFrame.setTitle("Changed title");
    	
    	setGame_ready(value);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = ((JButton) e.getSource());
        //System.out.println(buttonClicked.getActionCommand());
        
        int BsL = new BattleShip().getLength();
        int CsL = new Cruiser().getLength();
        int DsL = new Destroyer().getLength();
        int SsL = new Submarine().getLength();
        //System.out.println(BsL);
        int x = 0;
        int y = 0;
        int buttonNum = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (buttonClicked.equals(buttons[i])) {
                x = (i / 10);                               // based on the button index creates a x and y coordinate then later passed into the Ocean array
                y = (i % 10);
                buttonNum = i;
                break;
            }

        }
        //System.out.println(battle+"," + playerOcean.battleShipNum);
        
      
     
           //System.out.println("button you clicked is located at: " + x + ", " + y);
        
        
        if (playerOcean.getOceanArray()[x][y] == null) { //checks the array makes sure no shipps there
            if (battle < BsL ||  numBattle > playerOcean.battleShipNum) {       //checks which ship should be placed
                
                if(battle == 0){
                    previousX = x;
                    previousY = y;
                   boolean[] test = buttonDisable(x,y, 4);
                }
                if(battle == 1){
                    
                    if(y > previousY && previousX == x){
                         unWantedChoices(previousX, previousY, 4, "right");
                    }
                    else if(y < previousY && previousX == x){
                        unWantedChoices(previousX, previousY, 4, "left");
                    }
                    else if(x > previousX && previousY == y){
                        unWantedChoices(previousX, previousY, 4, "down");
                    }
                    else if(x < previousX && previousY == y){
                        unWantedChoices(previousX, previousY, 4, "up");
                    }
                }
                buttons[buttonNum].setBackground(Color.red);
                playerOcean.getOceanArray()[x][y] = myBS[numBattle];
                
                battle++;
                if (battle == BsL) {
                    limitOptions(x,y);
                    numBattle++;
                    setVisible();
                    previousX = 0; 
                    previousY = 0;
                    //JOptionPane.showMessageDialog(null, "Set '2' Cruiser NEXT click 3 spaces each! ", "Cruiser", JOptionPane.INFORMATION_MESSAGE);
                    shipsleftLabel.setForeground(new Color(0,102,0));
                    shipsleftLabel.setText("Set '2' Cruisers. Click 3 spaces each");
                }

            } else if (cruiser < CsL ||  numCruiser >playerOcean.cruisers) {
                if(cruiser == 0){
                    previousX = x;
                    previousY = y;
                    //System.out.println("entered in IF");
                   boolean[] test = buttonDisable(x,y, 3);
                }
                if(cruiser == 1){
                    
                    if(y > previousY && previousX == x){
                         unWantedChoices(previousX, previousY, 3, "right");
                    }
                    else if(y < previousY && previousX == x){
                        unWantedChoices(previousX, previousY, 3, "left");
                    }
                    else if(x > previousX && previousY == y){
                        unWantedChoices(previousX, previousY, 3, "down");
                    }
                    else if(x < previousX && previousY == y){
                        unWantedChoices(previousX, previousY, 3, "up");
                    }
                }
                
                buttons[buttonNum].setBackground(new Color(0,102,0));
                playerOcean.getOceanArray()[x][y] = myCS[numCruiser];
                cruiser++;
                if (cruiser == CsL) { //if ship length time pressed move on to the next ship object in the array
                    limitOptions(x,y);
                    
                    numCruiser++;
                    
                    setVisible();
                    if (numCruiser != playerOcean.cruisers) {
                        
                        cruiser = 0;
                        
                    }
                    else{
                        previousX = 0;
                        previousY = 0;
                        //JOptionPane.showMessageDialog(null, "Set '3' Destroyer NEXT click 2 spaces each! ", "Destroyer", JOptionPane.INFORMATION_MESSAGE);
                        shipsleftLabel.setForeground(Color.blue);
                        shipsleftLabel.setText("Set '3' Destroyers. Click 2 spaces each");
                    }
                }
            } else if (destroyer < DsL ||  numDestroyer >playerOcean.destroyers) {
                if(destroyer == 0){
                    previousX = x;
                    previousY = y;
                   boolean[] test = buttonDisable(x,y, 2);
                }
                 if(destroyer == 1){
                    
                    if(y > previousY && previousX == x){
                         unWantedChoices(previousX, previousY, 2, "right");
                    }
                    else if(y < previousY && previousX == x){
                        unWantedChoices(previousX, previousY, 2, "left");
                    }
                    else if(x > previousX && previousY == y){
                        unWantedChoices(previousX, previousY, 2, "down");
                    }
                    else if(x < previousX && previousY == y){
                        unWantedChoices(previousX, previousY, 2, "up");
                    }
                }
                buttons[buttonNum].setBackground(Color.blue);
                playerOcean.getOceanArray()[x][y] = myDS[numDestroyer];
                destroyer++;
                if (destroyer == DsL) {
                    limitOptions(x,y);
                    numDestroyer++;
                    setVisible(); //set the buttons enabled again
                    if (numDestroyer != playerOcean.destroyers) {
                        destroyer = 0;
                    }
                    else{
                        //JOptionPane.showMessageDialog(null, "Set '4' SubMarine NEXT click 1 space each! ", "SubMarine", JOptionPane.INFORMATION_MESSAGE);
                    	shipsleftLabel.setForeground(new Color(102,0,153));
                        shipsleftLabel.setText("Set '4' Submarines. Click 1 space each");
                    }
                }
            } else if (submarine < SsL || numSubmarine >playerOcean.submarines) {
                if(submarine == 0){
                   boolean[] test = buttonDisable(x,y, 1);
                }
                buttons[buttonNum].setBackground(new Color(102,0,153));
                playerOcean.getOceanArray()[x][y] = mySB[numSubmarine];
                submarine++;
                if (submarine == SsL) {
                    limitOptions(x,y);
                    numSubmarine++;
                    setVisible();
                    if (playerOcean.submarines != numSubmarine) {
                        submarine = 0;
                    }
                    else{
                        for(int i = 0 ; i < buttons.length; i++){
                            buttons[i].setEnabled(false);
                        }
                        shipsleftLabel.setForeground(Color.black);
                        shipsleftLabel.setText("Board all set. click 'SET SELECTION' >>> ");
                    }
                }
            }

            //System.out.println(battle + "," + playerOcean.battleShipNum);
        }
        
        
        //System.out.println(playerOcean.getOceanArray()[x][y].getShipType());
        
    }
    public void unWantedChoices(int xvalue, int yvalue, int length, String direction){
        int buttonNum = xvalue *10 + yvalue;
        for(int i = 0; i < buttons.length; i++){
            buttons[i].setEnabled(false);
        }
        if(direction.equals("right")){
            for(int j = buttonNum; j< buttonNum + length; j++){
                buttons[j].setEnabled(true);
            }
            
        }
        else if(direction.equals("left")){
            for(int j = buttonNum - length + 1; j< buttonNum; j++){
                buttons[j].setEnabled(true);
            }
        }
        else if(direction.equals("up")){
             for(int j = xvalue - length + 1 ; j< xvalue; j++){
                buttons[j * 10 + yvalue].setEnabled(true);
            }
        }
        else if(direction.equals("down")){
             for(int j = xvalue; j< xvalue + length; j++){
                buttons[j * 10 + yvalue].setEnabled(true);
            }
        }
        
        
        
        
    }
    public void setVisible(){
        for(int i = 0; i< playerOcean.getOceanArray().length; i++){
            for(int j = 0; j < playerOcean.getOceanArray().length; j++){
                if(playerOcean.getOceanArray()[i][j] == null){
                    buttons[i * 10 + j].setEnabled(true);
                    
                }
            }
            
            
        }
    }
    public boolean[] buttonDisable(int xvalue, int yvalue, int length){
        
        int buttonNum = (xvalue) * 10 + yvalue;
        //System.out.println(buttonNum);
        boolean[] boolarray = new boolean[4];
        boolean right = playerOcean.rightHori(xvalue, yvalue, length);
        boolean left = playerOcean.leftHori(xvalue, yvalue, length);
        boolean up = playerOcean.upWard(xvalue, yvalue, length);
        boolean down = playerOcean.downWard (xvalue, yvalue, length);
//        System.out.println(right);
//        System.out.println(left);
//        System.out.println(up);
//        System.out.println(down);
        boolarray[0] = up;
        boolarray[1] = down;
        boolarray[2] = right;
        boolarray[3] = left;
//        
//        if(right == left == up == down == false){
//            return boolarray;
//        }
        for(int i = 0; i < buttons.length; i++){
            buttons[i].setEnabled(false);
        }
        if(right == true){
       
            for(int j = buttonNum; j< buttonNum + length; j++){
                buttons[j].setEnabled(true);
            }
        }
            
           
        if(left == true){
            for(int j = buttonNum - length + 1; j< buttonNum; j++){
                buttons[j].setEnabled(true);
            }
            
        }
        if(up == true){
            for(int j = xvalue - length + 1 ; j< xvalue; j++){
                buttons[j * 10 + yvalue].setEnabled(true);
            }
        }
        if(down == true){
            for(int j = xvalue; j< xvalue + length; j++){
                buttons[j * 10 + yvalue].setEnabled(true);
            }
        }
        
        
        
        return boolarray;
        
    }
    public void limitOptions(int xvalue, int yvalue){
//        System.out.println("value passed in is: " + xvalue +"," + yvalue);
//        System.out.println("ship type is: " + playerOcean.getOceanArray()[xvalue][yvalue].getShipType());
//        
        
        boolean done = false;
        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;
        int counter = 0;
        Ship shipType = playerOcean.getOceanArray()[xvalue][yvalue];
        if(shipType.getShipType().equals("submarine")){
            startX = xvalue;
            startY = yvalue;
            endX = xvalue;
            endY = yvalue;
        }
        else {
            for (int i = 0; i < playerOcean.getOceanArray().length; i++) {
                for (int j = 0; j < playerOcean.getOceanArray().length; j++) {
                    if (playerOcean.getOceanArray()[i][j] == shipType) {
                        counter++;
                        if (counter == 1) {
                            startX = i;
                            startY = j;
                        } else if (counter == shipType.getLength()) {
                            endX = i;
                            endY = j;
                            done = true;
                            break;
                        }
                    

                    }

                }
                if (done == true) {
                    break;
                }

            }
        }
             
//        
//        System.out.println("start is: " + startX + "," + startY);
//        System.out.println("end is: " + endX + "," + endY);
        
        
        playerOcean.placeEmpty(startX, startY, endX, endY);
        
        
    }
      public void musicPlay(){
        File jam = new File("resources/Ocean.WAV");
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(jam));
            clip.start();
            clip.loop(ABORT);
            
            //Thread.sleep(clip.getMicrosecondLength()/1000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private class ResetListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
        	gameFrame.dispose();
            //new SelectShip(choiceoption, socket);
        	instanceList.add(SelectShip.this);
            new SelectShip(instanceList);
        }
        
        
        
    }
    
    
    private class RandomListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
        	Ocean tempplayerOcean = new Ocean(null);
        	tempplayerOcean.placeAllShipsRandomly();
        	playerOcean = tempplayerOcean;
        	
        	
        	numBattle=1;
        	numCruiser=2;
        	numDestroyer=3;
        	numSubmarine=4;
        	
        	for (int i = 0; i < playerOcean.getOceanArray().length; i++) {
                for (int j = 0; j < playerOcean.getOceanArray().length; j++) {
                	int buttonNum = (i * 10) + j;
                    if (playerOcean.getOceanArray()[i][j].getShipType() != "empty") {
                    	String myType = playerOcean.getOceanArray()[i][j].getShipType();
                    	System.out.println(myType);
                    	if(myType == "battleship") {
                    		buttons[buttonNum].setBackground(Color.red);
                    		
                    	}
                    	else if(myType == "destroyer") {
                    		buttons[buttonNum].setBackground(Color.blue);
                    	}
                    	else if(myType == "cruiser") {
                    		buttons[buttonNum].setBackground(new Color(0,102,0));
                    	}
                    	else if(myType == "submarine") {
                    		buttons[buttonNum].setBackground(new Color(102,0,153));
                    	}
                    	
                    	

                    }
                    else {
                    	buttons[buttonNum].setBackground(null);
                    	
                    }
                    buttons[buttonNum].setEnabled(false);
                    
                    

                }

            }
        	
        	
        }
        
        
        
    }
    /**
    private class Waitingforready extends Thread {

    	   public void run() {
    		   
    		   while(true) {
    			   System.out.println("Games is ready or.." + game_ready);
    			   if(game_ready==true) {
    				   System.out.println("Games is ready loading board...");
    				   //Ocean myocean = new Ocean();    //creates the shipObject
    				   if(myOcean.name_getter() == null){
    					   myOcean.placeAllShipsRandomly(); //place the ships on the Ocean array
    				   }
    		           
    		           myOcean.print(); // transform the shipObject array into integer object array
    		           myOcean.printTest(); //display the integer object array.
    		           FinalGUI myGUI = new FinalGUI(myOcean, playerOcean, choiceoption, socket); //pass in the ocean array into the GUI
    		           gameFrame.dispose();
    				   break;
    			   }
    			   
    		   }
    		   
    	   }
    	
    	
    	
    }
    **/

    private class startListener implements ActionListener {       //this listener for the start button on the buttom.

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Started the array");
        	

        	int total_ships = numBattle + numCruiser + numDestroyer + numSubmarine;
        	System.out.println(total_ships);
        	
        	if(total_ships < 10) {
        		JOptionPane.showMessageDialog(null, "Error: not enough Boat on Board!!!!!!!!!!!!!!!!", "Need correct Amount of boat", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
            
            Ship empty = new EmptySea();
            int totalShip = 0;
            for (int i = 0; i < playerOcean.getOceanArray().length; i++) {
                for (int j = 0; j < playerOcean.getOceanArray().length; j++) {
                    if (playerOcean.getOceanArray()[i][j] == null) {
                        playerOcean.getOceanArray()[i][j] = empty;

                    }
                     if(playerOcean.getOceanArray()[i][j] != null && !playerOcean.getOceanArray()[i][j].getShipType().equals("empty") ){
                        totalShip++;
                    }

                }

            }
             if(totalShip != 20){
            	//gameFrame.dispose();
                JOptionPane.showMessageDialog(null, "Error: not enough Boat on Board!!!!!!!!!!!!!!!!", "Need correct Amount of boat", JOptionPane.ERROR_MESSAGE);
                //new SelectShip(chosenOption, socket);
                
            }
             else{
            	 //put runnable start here, disable button, the rest of the ccode should be called within the thread
            	 
            	 myButton.setEnabled(false);
            	 
            	 if(SelectOptions.choiceoption==1) {
            		 setGame_ready(true);
            		 
            	 }
            	 
            	 
            	 if(!instanceList.isEmpty()){
            		 SelectShip my_instance = instanceList.get(0);
            		 my_instance.playerOcean = playerOcean;
            		 System.out.println("old player ocean");
            		 my_instance.playerOcean.print();
          			my_instance.playerOcean.printTest();
          			instanceList.add(SelectShip.this); //need to add selfinstance or when the ready signal is sent, the action goes back to the orginal object
          			//add it in here and use loop to remove frame later
            	 }
            	 
            	 
            	 sendReady(playerOcean);
            	 
             } 
        }

    }

	public void sendReady(Ocean myPlayerOcean) {
		myPlayerOcean.print();
		
		if(SelectOptions.choiceoption==1) {
			game_ready=true;
		}
		else {
			System.out.println("Im sending board now....");
			myPlayerOcean.printTest();
			this.playerOcean = myPlayerOcean;
			
			JSONObject object = new JSONObject();
			try {
				object.put("location", myPlayerOcean.getprint());
				object.put("objectid", myPlayerOcean.getObjectID());
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
					
			NetworkNew.socket.emit("player_ready", object);
		}
		
	}

	public void processBoard(String myString) {
		System.out.println("Processing board....");
		try {
			JSONObject myObject = new JSONObject(myString);
			JSONArray locationArray = myObject.getJSONArray("location");
			JSONArray objectidArray = myObject.getJSONArray("objectid");
			String id_value = myObject.getString("id");
			
			
			int count = locationArray.length();
	        System.out.println(id_value);
	        
	        myOcean.name_setter(id_value);
	        myOcean.setBoard(locationArray, objectidArray);
	        enemyBoards.put(id_value, myString);
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Process complete!");
		//System.out.println(location);
		
		
		
	}



}
