/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.image.ImageObserver.ABORT;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Fred
 */
class SelectShip extends JFrame implements ActionListener {
    
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
    private BattleShip[] myBS = new BattleShip[1]; //length based on the number of the type of ship on the board
    private Cruiser[] myCS = new Cruiser[2];     
    private Destroyer[] myDS = new Destroyer[3];
    private Submarine[] mySB = new Submarine[4];
    
    JButton[] buttons = new JButton[100];
    Ocean playerOcean = new Ocean();
    private int chosenOption;
    public SelectShip(int chosenOption) {
        this.chosenOption = chosenOption;
        
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
        
        
        
        
        
        
        
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setTitle("Choose Battle Ship Locations");
        setSize(1000, 1000);

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
        add(selectShip);
        JButton myButton = new JButton("Set Selection");
        JButton reset = new JButton("Reset Board");
        ActionListener listener = new startListener();
        ActionListener resetListner = new ResetListener();
        myButton.addActionListener(listener);
        reset.addActionListener(resetListner);
        myPanel.add(myButton);
        myPanel.add(reset);
        add(myPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        JOptionPane.showMessageDialog(null, "I only HIRE Professionals to test my flawless program -anonymous Paguine NinjaPandaexpresswtfAmidoingUpat2aminthemorning /n/n/n/n/n WwwherrreismyNewline.... \n ifyouhappentofinishreadingthisijustneed2tellyouthatyoumustbereallybored....\n butatleastwesharesomethingIncommonsinceIwastedmytimetypingthis...... \n", "My FLAWLESS Program", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(this, "PS. the previous message might contain typos so i dont suggest you finish reading it....", "LALALA", JOptionPane.WARNING_MESSAGE);
        JOptionPane.showMessageDialog(null, "Set BattleShip click 4 spaces! ", "BattleShip", JOptionPane.INFORMATION_MESSAGE);

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
                    JOptionPane.showMessageDialog(null, "Set Cruiser NEXT click 3 spaces! ", "Cruiser", JOptionPane.INFORMATION_MESSAGE);
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
                
                buttons[buttonNum].setBackground(Color.green);
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
                        JOptionPane.showMessageDialog(null, "Set Destroyer NEXT click 2 spaces! ", "Destroyer", JOptionPane.INFORMATION_MESSAGE);
                    
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
                        JOptionPane.showMessageDialog(null, "Set SubMarine NEXT click 1 spaces! ", "SubMarine", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else if (submarine < SsL || numSubmarine >playerOcean.submarines) {
                if(submarine == 0){
                   boolean[] test = buttonDisable(x,y, 1);
                }
                buttons[buttonNum].setBackground(Color.ORANGE);
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
            dispose();
            SelectShip newGame = new SelectShip(chosenOption);
        }
        
        
        
    }

    private class startListener implements ActionListener {       //this listener for the start button on the buttom.

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Started the array");
            
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
                dispose();
                JOptionPane.showMessageDialog(null, "Error: not enough Boat on Board!!!!!!!!!!!!!!!!", "Need correct Amount of boat", JOptionPane.ERROR_MESSAGE);
                SelectShip newGame = new SelectShip(chosenOption);
                
            }
             else{
            
            
            Ocean myocean = new Ocean();    //creates the shipObject
            myocean.placeAllShipsRandomly(); //place the ships on the Ocean array
//
//            Ocean myocean2 = new Ocean();
//            myocean2 = playerOcean;
//            myocean2.placeAllShipsRandomly();
//            playerOcean = myocean2; //temp here
            myocean.print(); // transform the shipObject array into integer object array
            myocean.printTest(); //display the integer object array.
            FinalGUI myGUI = new FinalGUI(myocean, playerOcean); //pass in the ocean array into the GUI
            dispose();
             } 
        }

    }

}
