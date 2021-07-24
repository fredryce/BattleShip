/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/**
 *
 * @author Fred
 */
public class FinalGUI extends JFrame implements ActionListener{
    SecureRandom sr = new SecureRandom();
    JButton[] setUp = new JButton[121];
    JButton[] myButtons = new JButton[121];
    JPanel leftPanel;
    JPanel rightPanel;
    private Ocean computerOcean;
    private Ocean playerOcean = new Ocean();
    private Ocean computerArray = new Ocean(); //this allows the computer to manipulate around to win
    int count = 1;
    int xHit;
    int yHit;
    boolean hitAround = false;
    int index;
    
    public FinalGUI(Ocean ocean, Ocean player){          //construtor takes Ocean object
        computerOcean = ocean;
        playerOcean = player;
        setTitle("Conquered By Chinese");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));
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
                myButtons[i].setText("0");
                    
                }
                else{
                
                
                myButtons[i].setFont(new Font("Arial", Font.BOLD, 90));
                
                
                
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
            
            myButtons[i].setFont(new Font("Arial", Font.BOLD, 60));
            myButtons[i].addActionListener(this);
            myButtons[i].setBackground(new Color(61, 68, 164)); // blue color
            //myButtons[i].setBorderPainted(false);

            
            
            num++;
            
            }
            //myButtons[i].setText(Integer.toString(i));

            rightPanel.add(myButtons[i]); 
           
            
            
            
            
            
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
                        setUp[i].setFont(new Font("Arial", Font.BOLD, 60));
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
        
        
        
        
        add(leftPanel);
        add(rightPanel);

        setSize(1000, 1000);
        //pack();
        setVisible(true);

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(((JButton)e.getSource()).getWidth()+","+((JButton)e.getSource()).getHeight());

        
        
        
        
        
        
        
        
        myButtons[0].setText(Integer.toString(count++));
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
                    moneyShot();
                    JOptionPane.showMessageDialog(null,"Your Price/Score is $" + count, "Item Bought" , JOptionPane.INFORMATION_MESSAGE);
                    
                    dispose();
                    

                }
                    
            }
            
            
            
            
            
        }
        ((JButton)e.getSource()).setEnabled(false);
//        boolean hitAround = false;
        int randomx = sr.nextInt(10);
        int randomy = sr.nextInt(10);
        
        //LinkedList ll = new LinkedList(Arrays.asList(testAround(randomx, randomy)));
        
        while(computerArray.isOccupied(randomx, randomy) /*&& !new LinkedList(Arrays.asList(testAround(randomx, randomy))).contains(true)*/){
             randomx = sr.nextInt(10);
             randomy = sr.nextInt(10);
             //System.out.println("inloop");
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
//            else{
//                System.out.println("ship sunk is true");
//                hitAround = false;
//            }
            
            
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
                    dispose();
                    

                }
                
            }
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
            this.repaint();
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
            clip.loop(ABORT);
            
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
  
        
        
        
        
        
        
    }
   
    
    
    

