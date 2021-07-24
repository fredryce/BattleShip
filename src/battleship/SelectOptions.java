/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author xwang2945
 */
public class SelectOptions extends JFrame implements ActionListener{
     public SelectOptions() {
         
        JButton[] options = new JButton[4];
        setTitle("Select an option");
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setSize(1000, 1000);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3,1));
        options[0] = new JButton();
        options[0].setFont(new Font("Arial", Font.BOLD, 30));    
        options[0].setText("Single player Mode");
        options[0].putClientProperty("player_num", 1);
        options[0].addActionListener(this);
        rightPanel.add(options[0]);
        
        
        
        options[1] = new JButton();
        options[1].setFont(new Font("Arial", Font.BOLD, 30));    
        options[1].setText("Multiplayer player Mode");
        options[1].addActionListener(this);
        options[1].putClientProperty("player_num", 2);
        rightPanel.add(options[1]);
        
        
        options[2] = new JButton();
        options[2].setFont(new Font("Arial", Font.BOLD, 30));    
        options[2].setText("Quit Game!");
        options[2].addActionListener(this);
        options[2].putClientProperty("player_num", 3);
        rightPanel.add(options[2]);
        
        add(rightPanel);
        pack();
        setVisible(true);
     }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(((JButton)e.getSource()).getClientProperty("player_num"));
        int optionchoice =(int)((JButton)e.getSource()).getClientProperty("player_num");
        if(optionchoice ==3){
            dispose();
            System.exit(0);
        }
        SelectShip newGame = new SelectShip(optionchoice);
        
    }
    
    
    
}
