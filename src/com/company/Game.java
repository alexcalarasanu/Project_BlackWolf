package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
    private ArrayList<Spearman> unitSpearmanList = new ArrayList<Spearman>();
    private BuildingOne buildingOne;
    private boolean isSpearTraining;

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
        unitSpearmanList = new ArrayList<Spearman>();
        buildingOne = new BuildingOne();

    }

    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent() {
        try {
            URL soldierURL = this.getClass().getResource("res/spearmanStill.png");
            Spearman.spearmanImg = ImageIO.read(soldierURL);
            URL build1URL = this.getClass().getResource("res/baseSprite.png");
            BuildingOne.building1Img = ImageIO.read(build1URL);
        } catch (IOException ex) {
            Logger.getLogger(Spearman.class.getName()).log(Level.SEVERE, null, ex);
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
            isSpearTraining = true;
            System.out.println("Pressed S");
        }
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1))
            if (isSpearTraining)
                createSpearmanUnit(gameTime, mousePosition);
        updateSpearmen();
        updateAlliedUnits();

    }

    /**
     * Draw the game to the screen.
     *
     * @param g2d           Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition, long gameTime) {
        terrain.Draw(g2d);
        for (Spearman anUnitSpearmanList : unitSpearmanList) anUnitSpearmanList.Draw(g2d);
        buildingOne.Draw(g2d);
    }


    public void createSpearmanUnit(long gameTime, Point mousePosition) {

        if (gameTime - Spearman.timeOfLastCreatedSpearman >= Spearman.timeBetweenNewSpearmen) {
            System.out.println("creating soldier");
            Spearman s = new Spearman();
            int xCoord = (int) mousePosition.getX() - 50;
            int yCoord = (int) mousePosition.getY() - 50;
            if (unitSpearmanList.size() != 0) {
                boolean isOccupied = true;
                int i = 0;
                while (isOccupied) {
                    if (i < unitSpearmanList.size()) {
                        System.out.println(String.valueOf(unitSpearmanList.get(i).xCoord));
                        if (xCoord == unitSpearmanList.get(i).xCoord && yCoord == unitSpearmanList.get(i).yCoord) {
                            i++;
                        } else isOccupied = false;
                    } else createSpearmanUnit(gameTime, mousePosition);
                }
            }
            s.Initialise(xCoord, yCoord);
            unitSpearmanList.add(s);
            Spearman.timeOfLastCreatedSpearman = gameTime;
        }
    }


    public void updateSpearmen() {
        for (int i = 0; i < unitSpearmanList.size(); i++) {
            Spearman s = unitSpearmanList.get(i);
            s.Update();
        }
    }

    public void updateAlliedUnits() {
        for (int i = 0; i < unitSpearmanList.size(); i++) {
            Spearman s = unitSpearmanList.get(i);
            Rectangle spearmanRectangle = new Rectangle(s.xCoord, s.yCoord, Spearman.spearmanImg.getWidth(), Spearman.spearmanImg.getHeight());

        }
    }
}
