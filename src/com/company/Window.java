package com.company;


import javax.swing.*;

/**
 *
 * @author ZaGunny
 */
public class Window extends JFrame {
    private Window(){
        //Sets the title for the game
        this.setTitle("Project BlackWolf");

        //Sets the size of the frame
        if(false)//fullscreen
        {
            this.setUndecorated(true);
            this.setExtendedState(this.MAXIMIZED_BOTH);
        }
        else //Window mode
        {
            //size of frame
            this.setSize(1024,768);
            // puts the frame in the centre of the screen
            this.setLocationRelativeTo(null);
            //the frame can't be resized by the user
            this.setResizable(false);
        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new Framework());
        this.setVisible(true);
    }

    public static void main (String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){new Window();}
        });
    }
}