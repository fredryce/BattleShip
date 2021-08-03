/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;


/**
 *
 * @author Fred
 */
public class FinalGUI implements ActionListener{
    SecureRandom sr = new SecureRandom();
    JButton[] setUp = new JButton[121];
    JButton[] myButtons = new JButton[121];
    JPanel leftPanel;
    JPanel rightPanel;
    private Ocean computerOcean;
    private Ocean playerOcean = new Ocean(null);
    private Ocean computerArray = new Ocean(null); //this allows the computer to manipulate around to win
    int count = 1;
    int xHit;
    int yHit;
    boolean hitAround = false;
    int index;
    private boolean myTurn = true;
    
    private JFrame myFrame = new JFrame(); 
    
    public FinalGUI(Ocean ocean, Ocean player){
    	//super(chosenOption, socket);
    	
        computerOcean = ocean;
        playerOcean = player;
        myFrame.setTitle(computerOcean.name_getter() +  " V.S " + playerOcean.name_getter());
        myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        myFrame.setUndecorated(false);
        myFrame.setResizable(true);
        //myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        myFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	if(NetworkNew.socket!=null) {
            		NetworkNew.socket.disconnect();
            	}
            	myFrame.dispose();
            	new SelectOptions();
            }
        });
        
        myFrame.setLayout(new GridLayout(1, 2));
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(11,11));
        leftPanel.setLayout(new GridLayout(11,11));
        int num = 0;
        for (int i = 0; i < myButtons.length; i++) {
        	
        	
           
            if (i <= 10 || i % 11 == 0) {
                myButtons[i] = new JButton();
                myButtons[i].setEnabled(false);//unclickable buttons
                myButtons[i].setBorderPainted(false);
                myButtons[i].setBackground(new Color(152, 51,51)); // red color
                if(i == 0){
                    
                myButtons[i].setFont(new Font("Arial", Font.BOLD, 30));    
                //myButtons[i].setText("0");
                
                
                    
                }
                else{
                
                
                myButtons[i].setFont(new Font("Arial", Font.BOLD, 30));
                
                
                
                }
                myButtons[i].setOpaque(true);
                if(i <= 10 && i>0){
                    myButtons[i].setText(Integer.toString(i - 1));
                }
                if(i % 11 == 0 && i>0){
                    myButtons[i].setText(Integer.toString(i/11 - 1));
                }
               

            }
            else{ //clickable buttons
                
                ImageIcon waterIcon = new ImageIcon("resources/water/waterimg" + num + ".jpg");
                Image image = waterIcon.getImage();
                image = image.getScaledInstance(87, 109, java.awt.Image.SCALE_SMOOTH);
                waterIcon = new ImageIcon(image);
                myButtons[i] = new JButton(waterIcon);
                myButtons[i].setDisabledIcon(waterIcon);
            
            myButtons[i].setFont(new Font("Arial", Font.BOLD, 30));
            myButtons[i].addActionListener(this);
            myButtons[i].setBackground(new Color(61, 68, 164)); // blue color
            //myButtons[i].setBorderPainted(false);

            
            
            num++;
            
            }
            //myButtons[i].setText(Integer.toString(i));

            rightPanel.add(myButtons[i]); 
           
            
            //myButtons[i].setBorder(null);
            myButtons[i].setMargin(new Insets(1, 1, 1, 1)); 
            myButtons[i].setPreferredSize(new Dimension(50, 26));
            
            
            
        }
        int x; 
        int y;
        for(int i = 0; i< 121; i++){
                
                x = (i / 11) - 1;                              
                y = (i % 11) - 1;
                
                setUp[i] = new JButton();
                if (!(i <= 10) && !(i % 11 == 0)) {
                    //System.out.println(x + "," + y + playerOcean.getOceanArray()[x][y].getShipType());
                    if (playerOcean.getOceanArray()[x][y].getShipType().equals("empty")) {
                        //System.out.println("Entered loop");
                        setUp[i].setBackground(Color.blue);
                        setUp[i].setFont(new Font("Arial", Font.BOLD, 30));
                    } 
                    else{
                        setUp[i].setBackground(Color.green);
                        
                    }
                        
                }
                else{
                    setUp[i].setBackground(new Color(152, 51,51));
                    setUp[i].setBorderPainted(false);
                }
                
                
                
                
                setUp[i].setEnabled(false);
                
                
                
                
                leftPanel.add(setUp[i]);
                
                
            
        
        
        
        
        }
        
        
        
        
        myFrame.add(leftPanel);
        myFrame.add(rightPanel);

        myFrame.setSize(1000, 1000);
        //pack();
        myFrame.setVisible(true);

    }
    
    
    
    
    
    
    
    
    
    
    public void performAttackMove(int randomx, int randomy) {
    	
    	if(computerOcean.name_getter()!=null){
    		hitAround=false; //disable smart robot when players are involved
    		
    	}
    	
    	
       //printArray(testAround(randomx, randomy));
       Ship friendShip;
       if (hitAround == true) {
           friendShip = playerOcean.getOceanArray()[xHit][yHit];
       } else {
           friendShip = playerOcean.getOceanArray()[randomx][randomy];
       }
       if (hitAround == true && friendShip.getIsSunk() == false) {
           if (friendShip.getIsSunk() == false) {
               //System.out.println("entered in if");
               boolean[] options = testAround(xHit, yHit);
               
               if (contains(options)) {
                   //System.out.println("Entered second If");;
                   int random = sr.nextInt(4);

                   while (options[random] != true) {
                       random = sr.nextInt(4);
                   }
                   if (random == 0) {
                       randomx = xHit - 1; //test up 
                       randomy = yHit;

                   } else if (random == 1) {
                       randomx = xHit + 1;
                       randomy = yHit;

                   } else if (random == 2) {
                       randomy = yHit + 1; //test right
                       randomx = xHit;
                   } else if (random == 3) {
                       randomy = yHit - 1;
                       randomx = xHit;
                   }

                   friendShip = playerOcean.getOceanArray()[randomx][randomy];
               }
               else{
                       randomx = sr.nextInt(10);
                       randomy = sr.nextInt(10);
       
                   while (computerArray.isOccupied(randomx, randomy) /*&& !new LinkedList(Arrays.asList(testAround(randomx, randomy))).contains(true)*/) {
                       randomx = sr.nextInt(10);
                       randomy = sr.nextInt(10);
                       //System.out.println("inloop");
                   }
                   friendShip = playerOcean.getOceanArray()[randomx][randomy];
                   hitAround = false;
                   
               }
           }
//           else{
//               System.out.println("ship sunk is true");
//               hitAround = false;
//           }
           
           
       }
       //System.out.println(randomx+","+randomy);
       computerArray.getOceanArray()[randomx][randomy] = new EmptySea();
       if(playerOcean.getOceanArray()[randomx][randomy].getShipType().equals("empty")){
           setUp[((randomx + 1) * 11) + randomy + 1].setText("X");
       }
       else{
           setUp[((randomx + 1) * 11) + randomy + 1].setBackground(Color.red );
           
           
           
           if(friendShip.getHealthPoint() > 1){
               //JOptionPane.showMessageDialog(null,friendShip.getShipType() + friendShip.getHealthPoint(), "health" , JOptionPane.INFORMATION_MESSAGE);
               friendShip.setHealthPoint(friendShip.getHealthPoint() - 1);
               hitAround = true;
               xHit = randomx;
               yHit = randomy;
               
           }
           else{
               friendShip.setIsSunk(true);
               hitAround = false;
               //JOptionPane.showMessageDialog(null,friendShip.getShipType() + friendShip.getHealthPoint(), "health" , JOptionPane.INFORMATION_MESSAGE);
               //JOptionPane.showMessageDialog(null,friendShip.getShipType() + " is Sunk", "sunk" , JOptionPane.INFORMATION_MESSAGE);
               playerOcean.total -= 1;
               eliminateOptions(randomx, randomy);
               
               
               if(playerOcean.total == 0){
                   JOptionPane.showMessageDialog(null,"ALL SHIPS HAS BEEN SUNK!! Computer Won", "GAME OVER" , JOptionPane.INFORMATION_MESSAGE); 
                   myFrame.dispose();
                   

               }
               
           }
       }
    	
    }
    
    
    
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(((JButton)e.getSource()).getWidth()+","+((JButton)e.getSource()).getHeight());

        if(myTurn==false) { //making sure its my turn in a multiplayer game
        	return;
        }
        
        
        //myButtons[0].setText(Integer.toString(count++));
        int x = 0;
        int y = 0;
        int buttonNum = 0;
        for(int i = 0; i < myButtons.length; i++){
            if(((JButton)e.getSource()).equals(myButtons[i])){
                x = (i / 11) - 1;                               // based on the button index creates a x and y coordinate then later passed into the Ocean array
                y = (i % 11) - 1;
                buttonNum = i;
                break;
            }
            
            
            
        }
        if(computerOcean.getOceanArray()[x][y].getShipType().equals("empty")){
            
           ImageIcon myIcon = new ImageIcon("resources/fish.jpg");
           Image madFish = myIcon.getImage();
           madFish = madFish.getScaledInstance(((JButton)e.getSource()).getWidth(), ((JButton)e.getSource()).getHeight(), java.awt.Image.SCALE_SMOOTH);
           myIcon = new ImageIcon(madFish);
           
           ((JButton)e.getSource()).setIcon(myIcon);
           ((JButton)e.getSource()).setDisabledIcon(myIcon);
          
           
           
           
           
        }
        else{
            ImageIcon chinIcon = new ImageIcon("resources/China.jpg");
            Image bushey = chinIcon.getImage();
            bushey = bushey.getScaledInstance( ((JButton)e.getSource()).getWidth(), ((JButton)e.getSource()).getHeight(), java.awt.Image.SCALE_SMOOTH);
            chinIcon = new ImageIcon(bushey);
            ((JButton)e.getSource()).setIcon(chinIcon);
            ((JButton)e.getSource()).setDisabledIcon(chinIcon);
            Ship myship = computerOcean.getOceanArray()[x][y];
            if(myship.getHealthPoint() > 1){
            //System.out.println(myship.getHealthPoint());    
            myship.setHealthPoint(myship.getHealthPoint() - 1);
            }
            else{
                
                myship.setIsSunk(true); // when the ship is suck check if its game over
                for(int i = 0; i< computerOcean.getOceanArray().length;i++){
                    
                    for(int j = 0; j < computerOcean.getOceanArray().length; j++){
                        if(computerOcean.getOceanArray()[i][j] == myship){
                            myButtons[((i + 1) * 11) + j + 1].setIcon(null);
                            myButtons[((i + 1) * 11) + j + 1].setText(Character.toString(myship.getShipType().charAt(0)).toUpperCase());
                            
                        }
                        
                    }
                }
                
                
                
                computerOcean.total -= 1;
                //System.out.println(myOcean.total);
                //JOptionPane.showMessageDialog(null,computerOcean.getOceanArray()[x][y].getShipType() + " is SUNK!!!!" , x+","+y + " Ship SUNK", JOptionPane.INFORMATION_MESSAGE);
                if(computerOcean.total == 0){
                    JOptionPane.showMessageDialog(null,"ALL SHIPS HAS BEEN SUNK!!", "GAME OVER" , JOptionPane.INFORMATION_MESSAGE);
                    chinaTakesOver();
                    count = 120 - count +1;
                    //moneyShot();
                    JOptionPane.showMessageDialog(null,"Your Price/Score is $" + count, "Item Bought" , JOptionPane.INFORMATION_MESSAGE);
                    checkDC();
                    

                }
                    
            }
            
            
            
            
            
        }
        ((JButton)e.getSource()).setEnabled(false);
//        boolean hitAround = false;
        
        
        if(computerOcean.name_getter() == null) {
        	int randomx = sr.nextInt(10);
            int randomy = sr.nextInt(10);
            
            while(computerArray.isOccupied(randomx, randomy) /*&& !new LinkedList(Arrays.asList(testAround(randomx, randomy))).contains(true)*/){
                randomx = sr.nextInt(10);
                randomy = sr.nextInt(10);
                //System.out.println("inloop");
           }
            
            performAttackMove(randomx, randomy);
        	
        }
        else {
        	sendAttackMove(x, y);
        }
        
        
        
        
        
        
    }
    public void sendAttackMove(int x, int y) {
		//emits event with call back to send attack move
    	JSONObject object = new JSONObject();
    	try {
			object.put("x", x);
			object.put("y", y);
			NetworkNew.socket.emit("attack", object);
	    	myTurn=false;
	    	System.out.println("sending an attack at location " + x + ", " + y + " my turn to attack " + myTurn);
	    	
	    	
	    	ImageIcon leftIcon = new ImageIcon("resources/left_arrow.png");
	        Image leftArray = leftIcon.getImage();
	        leftArray = leftArray.getScaledInstance((myButtons[0]).getWidth(), (myButtons[0]).getHeight(), java.awt.Image.SCALE_SMOOTH);
	        leftIcon = new ImageIcon(leftArray);
	    	myButtons[0].setIcon(leftIcon);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
    	
    	
    	
    	
		
	}
    public void recieveAttackMove(JSONObject myObject) {
		//emits event with call back to send attack move
		try {
			int x = myObject.getInt("x");
			int y =  myObject.getInt("y");
			hitAround = false;
	    	performAttackMove(x, y); //called to show attmove on the board of myself
	    	myTurn =true;
	    	
	    	ImageIcon rightIcon = new ImageIcon("resources/right_arrow.png");
	        Image rightArray = rightIcon.getImage();
	        rightArray = rightArray.getScaledInstance((myButtons[0]).getWidth(), (myButtons[0]).getHeight(), java.awt.Image.SCALE_SMOOTH);
	        rightIcon = new ImageIcon(rightArray);
	    	myButtons[0].setIcon(rightIcon);
	    	
	    	
	    	System.out.println("Recieved an attack at location " + x + ", " + y + " my turn to attack " + myTurn);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    	
	}
    




	public void eliminateOptions(int xvalue, int yvalue){
//        System.out.println("value passed in is: " + xvalue +"," + yvalue);
//        System.out.println("ship type is: " + playerOcean.getOceanArray()[xvalue][yvalue].getShipType());
        
        
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
             
        
//        System.out.println("start is: " + startX + "," + startY);
//        System.out.println("end is: " + endX + "," + endY);
        
        
        computerArray.placeEmpty(startX, startY, endX, endY);
        
        
        
    }
    
    
    
    public boolean[] testAround(int xvalue, int yvalue){
        boolean[] testArray = new boolean[4];
        try{ //testabove
            if(computerArray.getOceanArray()[xvalue - 1][yvalue] == null){
                testArray[0] = true;
            }
            else{
                testArray[0] = false;
            }
            
        }
        catch(IndexOutOfBoundsException e){
            testArray[0] = false;
        }
        
        try{ //testBelow
            if(computerArray.getOceanArray()[xvalue + 1][yvalue] == null){
                testArray[1] = true;
            }
            else{
                testArray[1] = false;
            }
            
        }
        catch(IndexOutOfBoundsException e){
            testArray[1] = false;
        }
        try{
            if(computerArray.getOceanArray()[xvalue][yvalue + 1] == null){
                testArray[2] = true;
            }
            else{
                testArray[2] = false;
            }
            
        }
        catch(IndexOutOfBoundsException e){
            testArray[2] = false;
        }
        try{
            if(computerArray.getOceanArray()[xvalue][yvalue - 1] == null){
                testArray[3] = true;
            }
            else{
                testArray[3] = false;
            }
            
        }
        catch(IndexOutOfBoundsException e){
            testArray[3] = false;
        }
        
        
        
        
        
        
        
        return testArray;
    }
 
    public void chinaTakesOver(){
        int pic = 0;
        for(int i = 0; i < myButtons.length;i++){
            if(!(i <=10) && !(i%11 == 0)){
            myButtons[i].setText(null);
            myButtons[i].setIcon(null);
            ImageIcon myII = new ImageIcon("resources/china/fromChina" + pic + ".jpg" );
            Image myIG = myII.getImage();
            myIG = myIG.getScaledInstance(myButtons[i].getWidth(),myButtons[i].getHeight() , java.awt.Image.SCALE_SMOOTH);
            myII = new ImageIcon(myIG);
            myButtons[i].setIcon(myII);
            myButtons[i].setDisabledIcon(myII);
            myButtons[i].setEnabled(false);   
            myFrame.repaint();
            pic++;
            
            }
            
        }
        
        
        
        
    }
   
   
    public void moneyShot(){
        File jam = new File("resources/money.WAV");
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(jam));
            clip.start();
            clip.loop(myFrame.ABORT);
            
            //Thread.sleep(clip.getMicrosecondLength()/1000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        
        
    }
    public void printArray(boolean[] options){
        for(int i = 0; i < options.length; i++){
            System.out.print(options[i]);
        }
        System.out.println();
        
        
    }
    public boolean contains(boolean[] options){
        for(int i = 0; i < options.length; i++){
            if(options[i] == true){
                return true;
            }
        }
        return false;
        
    }
//    

    public void checkDC() {
    	myFrame.dispose();
    	if(NetworkNew.socket!=null) {
    		NetworkNew.socket.disconnect();
    	}
    	new SelectOptions();
    }


	public void disconnect(String userdc) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null , "Roomates has left the room userid: " + userdc, "LALALA", JOptionPane.INFORMATION_MESSAGE);
		checkDC();
	}




















        
        
        
        
    }
   
    
    
    

