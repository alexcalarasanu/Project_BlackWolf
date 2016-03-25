package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ZaGunny on 22/03/2016.
 */
public class Game {
    private Terrain terrain;
    private ArrayList<Soldier> unitSoldierList = new ArrayList<Soldier>();

    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                Initialize();
                LoadContent();
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    /**
     * Set variables and objects for the game.
     */
    private void Initialize() {
        terrain = new Terrain();
        unitSoldierList = new ArrayList<Soldier>();
    }

    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent() {
        try {
            URL soldierURL = this.getClass().getResource("res/soldier.png");
            Soldier.soldierImg = ImageIO.read(soldierURL);
        } catch (IOException ex) {
            Logger.getLogger(Soldier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Restart game - reset some variables.
     */
    public void RestartGame() {

    }


    /**
     * Update game logic.
     *
     * @param gameTime      gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition) {
        if (Canvas.keyboardKeyState(KeyEvent.VK_S)) {
            createSoldierUnit(gameTime);
            System.out.println("Pressed S");
        }
        updateSoldiers();
    }

    /**
     * Draw the game to the screen.
     *
     * @param g2d           Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition, long gameTime) {
        terrain.Draw(g2d);
        for (int i = 0; i < unitSoldierList.size(); i++) {
            unitSoldierList.get(i).Draw(g2d);
        }
    }


    public void createSoldierUnit(long gameTime) {

        if (gameTime - Soldier.timeOfLastCreatedSoldier >= Soldier.timeBetweenNewSoldiers) {
            System.out.println("creating soldier");
            Soldier s = new Soldier();
            int xCoord = 100;
            int yCoord = 100;
            if (unitSoldierList.size() == 0) {
                s.Initialise(xCoord, yCoord);
            } else {
                boolean isOccupied = true;
                int i = 0;
                while (isOccupied) {
                    if (xCoord != unitSoldierList.get(i).xCoord) {
                        xCoord += 100;
                        isOccupied = false;
                    } else i++;

                    s.Initialise(xCoord, yCoord);
                }
                unitSoldierList.add(s);
                Soldier.timeOfLastCreatedSoldier = gameTime;
                System.out.println("timeoflastcreatedSoldier" + gameTime / Framework.milisecInNanosec);
            }
        }
    }

    public void updateSoldiers() {
        for (int i = 0; i < unitSoldierList.size(); i++) {
            Soldier s = unitSoldierList.get(i);
            s.Update();
        }
    }
}
