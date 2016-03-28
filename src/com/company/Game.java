package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ZaGunny on 22/03/2016.
 */
public class Game {
    private Terrain terrain;
    private ArrayList<Spearman> unitSpearmanList;
    private ArrayList<Rectangle> spearmanRectangleList;
    private ArrayList<Rectangle> enemySpearmanRectangleList;


    private ArrayList<EnemySpearman> unitEnemySpearmanList = new ArrayList<EnemySpearman>();
    private BuildingOne buildingOne;
    private boolean isSpearTraining;
    private Rectangle castleRectangle;
    private BuildingOne castle;

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
        unitEnemySpearmanList = new ArrayList<EnemySpearman>();
        spearmanRectangleList = new ArrayList<Rectangle>();
        enemySpearmanRectangleList = new ArrayList<Rectangle>();
        castle = new BuildingOne();
        castleRectangle = new Rectangle();

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
            URL enemySpearmanURL = this.getClass().getResource("res/enemySpearmanStill.png");
            EnemySpearman.spearmanImg = ImageIO.read(enemySpearmanURL);
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

        createEnemySpearmanUnit(gameTime);
        updateSpearmen(gameTime);
        updateEnemySpearmen(gameTime);
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
        for (int i = 0; i < unitEnemySpearmanList.size(); i++) {
            unitEnemySpearmanList.get(i).Draw(g2d);
        }
        buildingOne.Draw(g2d);
    }

    public void createEnemySpearmanUnit(long gameTime) {
        if (gameTime - EnemySpearman.timeOfLastCreatedEnemySpearman >= EnemySpearman.timeBetweenNewEnemySpearmen) {
            System.out.println("creating enemy");
            EnemySpearman es = new EnemySpearman();
            int enemyXCoord = 1600;
            Random rand = new Random();
            int enemyYCoord = rand.nextInt(700 - 300 + 1) + 300;
            if (unitEnemySpearmanList.size() != 0) {
                boolean isOccupied = true;
                int i = 0;
                while (isOccupied) {
                    if (i < unitEnemySpearmanList.size()) {
                        System.out.println(String.valueOf(unitEnemySpearmanList.get(i).xCoord));
                        if (enemyXCoord == unitEnemySpearmanList.get(i).xCoord && enemyYCoord == unitEnemySpearmanList.get(i).yCoord) {
                            i++;
                        } else isOccupied = false;
                    } else createEnemySpearmanUnit(gameTime);
                }
            }
            es.Initialise(enemyXCoord, enemyYCoord);
            unitEnemySpearmanList.add(es);
            EnemySpearman.timeOfLastCreatedEnemySpearman = gameTime;
        }
    }


    public void createSpearmanUnit(long gameTime, Point mousePosition) {

        if (gameTime - Spearman.timeOfLastCreatedSpearman >= Spearman.timeBetweenNewSpearmen) {
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


    public void updateSpearmen(long gameTime) {
        for (int i = 0; i < unitSpearmanList.size(); i++) {
            Spearman s = unitSpearmanList.get(i);
            Rectangle r = new Rectangle(s.xCoord, s.yCoord, Spearman.spearmanImg.getWidth() / 2, Spearman.spearmanImg.getHeight() / 2);
            spearmanRectangleList.add(i, r);
            for (int j = 0; j < unitEnemySpearmanList.size(); j++) {
                EnemySpearman es = unitEnemySpearmanList.get(j);
                Rectangle er = new Rectangle(es.xCoord, es.yCoord, EnemySpearman.spearmanImg.getWidth() / 3, EnemySpearman.spearmanImg.getHeight() / 3);
                enemySpearmanRectangleList.add(j, er);
                if (es.xCoord <= s.xCoord + s.aggroRange && s.xCoord < es.xCoord) {
                    if (es.yCoord < s.yCoord)
                        s.yCoord -= s.movingYSpeed;
                    if (es.yCoord > s.yCoord)
                        s.yCoord += s.movingYSpeed;
                }
                if (spearmanRectangleList.get(i).intersects(enemySpearmanRectangleList.get(j))) {
                    if (s.heath >= 0 && es.heath >= 0) {
                        s.movingXSpeed = 0;
                        es.movingXSpeed = 0;
                        s.movingYSpeed = 0;
                        es.movingYSpeed = 0;
                        if (gameTime - s.timeOfLastAttack >= s.attackSpeed) {
                            es.heath -= (s.attackDamage - es.armour);
                            s.timeOfLastAttack = gameTime;

                        }
                        if (gameTime - es.timeOfLastAttack >= es.attackSpeed) {
                            s.heath -= (es.attackDamage - s.armour);
                            es.timeOfLastAttack = gameTime;

                        }
                    } else if (s.heath <= 0) {
                        unitSpearmanList.remove(i);
                        es.movingXSpeed = 1;

                    } else {
                        unitEnemySpearmanList.remove(j);
                        s.movingXSpeed = 1;

                    }
                }
            }

            s.Update();
        }
    }

    public void updateEnemySpearmen(long gameTime) {
        castleRectangle.setBounds(castle.xCoord, castle.yCoord, BuildingOne.building1Img.getWidth(), BuildingOne.building1Img.getHeight());
        for (int i = 0; i < unitEnemySpearmanList.size(); i++) {
            EnemySpearman es = unitEnemySpearmanList.get(i);
            Rectangle er = new Rectangle(es.xCoord, es.yCoord, EnemySpearman.spearmanImg.getWidth() / 3, EnemySpearman.spearmanImg.getHeight() / 3);
            enemySpearmanRectangleList.add(i, er);
            for (int j = 0; j < unitSpearmanList.size(); j++) {
                Spearman s = unitSpearmanList.get(j);
                if (s.xCoord >= es.xCoord - es.aggroRange && s.xCoord < es.xCoord) {
                    if (es.yCoord > s.yCoord)
                        es.yCoord -= es.movingYSpeed;
                    if (es.yCoord < s.yCoord)
                        es.yCoord += es.movingYSpeed;
                }
            }
            if (enemySpearmanRectangleList.get(i).intersects(castleRectangle)) {
                if (gameTime - es.timeOfLastAttack >= es.attackSpeed) {
                    es.movingXSpeed = 0;
                    castle.health -= es.attackDamage;
                    es.timeOfLastAttack = gameTime;
                }
                if (castle.health <= 0)
                    Framework.gameState = Framework.GameState.GAMEOVER;

            }
            es.Update();
    }

    }
}
