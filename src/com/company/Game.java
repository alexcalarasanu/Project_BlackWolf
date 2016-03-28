package com.company;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.company.Framework.frameHeight;

/**
 * Created by ZaGunny on 22/03/2016.
 */
public class Game {
    private Terrain terrain;
    private ArrayList<Spearman> unitSpearmanList;
    private ArrayList<Rectangle> spearmanRectangleList;
    private ArrayList<Rectangle> enemySpearmanRectangleList;
    private ArrayList<Giant> unitGiantList;
    private ArrayList<Rectangle> giantRectangleList;
    private int playerGold;
    private int enemyGold;
    private long lastGoldTick;
    private Font goldFont = new Font("Futura", Font.BOLD, 20);
    public int playerScore;


    private ArrayList<EnemySpearman> unitEnemySpearmanList;
    private boolean isSpearTraining;
    private boolean isGiantTraining;
    private Rectangle castleRectangle;
    private BuildingOne castle;
    private Rectangle enemyCastleRectangle;
    private BuildingOne enemyCastle;
    private AudioInputStream allyDeathAIS;
    private Clip allyDeathClip;
    private AudioInputStream enemyDeathAIS;
    private Clip enemyDeathClip;

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
        isSpearTraining = false;
        isGiantTraining = false;
        terrain = new Terrain();
        unitSpearmanList = new ArrayList<Spearman>();
        unitGiantList = new ArrayList<Giant>();
        unitEnemySpearmanList = new ArrayList<EnemySpearman>();
        spearmanRectangleList = new ArrayList<Rectangle>();
        enemySpearmanRectangleList = new ArrayList<Rectangle>();
        giantRectangleList = new ArrayList<Rectangle>();
        unitGiantList = new ArrayList<Giant>();
        castle = new BuildingOne();
        castleRectangle = new Rectangle();
        enemyCastle = new BuildingOne();
        enemyCastleRectangle = new Rectangle();
        playerGold = 0;
        enemyGold = 0;
        lastGoldTick = 0;
        playerScore = 0;
        castle.xCoord = -40;
        castle.yCoord = frameHeight / 2 - 150;
        enemyCastle.xCoord = 1680;
        enemyCastle.yCoord = frameHeight / 2 - 150;

    }

    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent() {
        try {
            URL soldierURL = this.getClass().getResource("res/spearmanStill.png");
            Spearman.spearmanImg = ImageIO.read(soldierURL);
            URL giantURL = this.getClass().getResource("res/giant.png");
            Giant.giantImg = ImageIO.read(giantURL);
            URL castleURL = this.getClass().getResource("res/allyBase.png");
            castle.building1Img = ImageIO.read(castleURL);
            URL enemyCastleURL = this.getClass().getResource("res/enemyBase.png");
            enemyCastle.building1Img = ImageIO.read(enemyCastleURL);
            URL enemySpearmanURL = this.getClass().getResource("res/enemy.png");
            EnemySpearman.spearmanImg = ImageIO.read(enemySpearmanURL);
            allyDeathAIS = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("res/sounds/allyDeathSound.wav"));
            allyDeathClip = AudioSystem.getClip();
            allyDeathClip.open(allyDeathAIS);
            enemyDeathAIS = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("res/sounds/enemyDeathSound.wav"));
            enemyDeathClip = AudioSystem.getClip();
            enemyDeathClip.open(enemyDeathAIS);
        } catch (Exception ex) {
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
        enemyCastleRectangle.setBounds(enemyCastle.xCoord, enemyCastle.yCoord, enemyCastle.building1Img.getWidth(), enemyCastle.building1Img.getHeight());
        castleRectangle.setBounds(castle.xCoord, castle.yCoord, castle.building1Img.getWidth(), castle.building1Img.getHeight());
        if (Canvas.keyboardKeyState(KeyEvent.VK_ESCAPE))
            Framework.gameState = Framework.GameState.MAIN_MENU;
        if (Canvas.keyboardKeyState(KeyEvent.VK_S)) {
            isSpearTraining = true;
            System.out.println("Pressed S");
            isGiantTraining = false;
        }
        if (Canvas.keyboardKeyState(KeyEvent.VK_G)) {
            isGiantTraining = true;
            System.out.println("Pressed G");
            isSpearTraining = false;
        }
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            if (isSpearTraining)
                createSpearmanUnit(gameTime, mousePosition);
            if (isGiantTraining)
                createGiantUnit(gameTime, mousePosition);
        }
        tickGold(gameTime);
        createEnemySpearmanUnit(gameTime);
        updateSpearmen(gameTime);
        updateEnemySpearmen(gameTime);
        updateGiant(gameTime);
    }

    /**
     * Draw the game to the screen.
     *
     * @param g2d      Graphics2D
     *
     */
    public void Draw(Graphics2D g2d) {
        g2d.setFont(goldFont);
        g2d.setColor(Color.yellow);
        terrain.Draw(g2d);
        castle.Draw(g2d);
        enemyCastle.Draw(g2d);
        for (Spearman anUnitSpearmanList : unitSpearmanList) anUnitSpearmanList.Draw(g2d);
        for (int i = 0; i < unitEnemySpearmanList.size(); i++) {
            unitEnemySpearmanList.get(i).Draw(g2d);
        }
        for (int i = 0; i < unitGiantList.size(); i++) {
            unitGiantList.get(i).Draw(g2d);
        }
        String playerGoldString = "Gold: " + String.valueOf(playerGold);
        g2d.drawString(playerGoldString, 200, 20);
        String playerScoreString = "Score: " + String.valueOf(playerScore);
        g2d.drawString(playerScoreString, 400, 20);
    }

    public void createEnemySpearmanUnit(long gameTime) {
        if (gameTime - EnemySpearman.timeOfLastCreatedEnemySpearman >= EnemySpearman.timeBetweenNewEnemySpearmen && enemyGold >= EnemySpearman.goldCost) {
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

        if (gameTime - Spearman.timeOfLastCreatedSpearman >= Spearman.timeBetweenNewSpearmen && playerGold >= Spearman.goldCost) {
            Spearman s = new Spearman();
            playerGold -= Spearman.goldCost;
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

    public void createGiantUnit(long gameTime, Point mousePosition) {

        if (gameTime - Giant.timeOfLastCreatedGiant >= Giant.timeBetweenNewGiants && playerGold >= Giant.goldCost) {
            Giant g = new Giant();
            playerGold -= Giant.goldCost;
            int xCoord = (int) mousePosition.getX() - 50;
            int yCoord = (int) mousePosition.getY() - 50;
            if (unitGiantList.size() != 0) {
                boolean isOccupied = true;
                int i = 0;
                while (isOccupied) {
                    if (i < unitGiantList.size()) {
                        System.out.println(String.valueOf(unitGiantList.get(i).xCoord));
                        if (xCoord == unitGiantList.get(i).xCoord && yCoord == unitGiantList.get(i).yCoord) {
                            i++;
                        } else isOccupied = false;
                    } else createSpearmanUnit(gameTime, mousePosition);
                }
            }
            g.Initialise(xCoord, yCoord);
            unitGiantList.add(g);
            Giant.timeOfLastCreatedGiant = gameTime;
        }
    }


    public void updateSpearmen(long gameTime) {
        for (int i = 0; i < unitSpearmanList.size(); i++) {
            Spearman s = unitSpearmanList.get(i);
            Rectangle r = new Rectangle(s.xCoord, s.yCoord, Spearman.spearmanImg.getWidth() / 3, Spearman.spearmanImg.getHeight() / 3);
            spearmanRectangleList.add(i, r);
            if (spearmanRectangleList.get(i).intersects(enemyCastleRectangle)) {
                s.movingXSpeed = 0;
                if (gameTime - s.timeOfLastAttack >= s.attackSpeed) {

                    enemyCastle.health -= s.attackDamage;
                    s.timeOfLastAttack = gameTime;
                }
                if (enemyCastle.health <= 0)
                    Framework.gameState = Framework.GameState.GAMEOVER;
            }
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
                        playerScore -= 50;
                        es.movingXSpeed = 2;
                        allyDeathClip.setFramePosition(0);
                        allyDeathClip.start();

                    } else {
                        unitEnemySpearmanList.remove(j);
                        s.movingXSpeed = 2;
                        playerScore += 100;
                        enemyDeathClip.setFramePosition(0);
                        enemyDeathClip.start();

                    }
                }
            }

            s.Update();
        }
    }

    public void updateGiant(long gameTime) {

        for (int i = 0; i < unitGiantList.size(); i++) {
            Giant g = unitGiantList.get(i);
            Rectangle r = new Rectangle(g.xCoord, g.yCoord, Giant.giantImg.getWidth() / 2, Giant.giantImg.getHeight() / 2);
            giantRectangleList.add(i, r);
            if (giantRectangleList.get(i).intersects(enemyCastleRectangle)) {
                if (gameTime - g.timeOfLastAttack >= g.attackSpeed) {
                    g.movingXSpeed = 0;
                    enemyCastle.health -= g.attackDamage;
                    g.timeOfLastAttack = gameTime;
                }
                if (enemyCastle.health <= 0)
                    Framework.gameState = Framework.GameState.GAMEOVER;
            }
            for (int j = 0; j < unitEnemySpearmanList.size(); j++) {
                EnemySpearman es = unitEnemySpearmanList.get(j);
                Rectangle er = new Rectangle(es.xCoord, es.yCoord, EnemySpearman.spearmanImg.getWidth() / 3, EnemySpearman.spearmanImg.getHeight() / 3);
                enemySpearmanRectangleList.add(j, er);
                if (es.xCoord <= g.xCoord + g.aggroRange && g.xCoord < es.xCoord) {
                    if (es.yCoord < g.yCoord)
                        g.yCoord -= g.movingYSpeed;
                    if (es.yCoord > g.yCoord)
                        g.yCoord += g.movingYSpeed;
                }
                if (giantRectangleList.get(i).intersects(enemySpearmanRectangleList.get(j))) {
                    if (g.heath >= 0 && es.heath >= 0) {
                        g.movingXSpeed = 0;
                        es.movingXSpeed = 0;
                        g.movingYSpeed = 0;
                        es.movingYSpeed = 0;
                        if (gameTime - g.timeOfLastAttack >= g.attackSpeed) {
                            es.heath -= (g.attackDamage - es.armour);
                            g.timeOfLastAttack = gameTime;

                        }
                        if (gameTime - es.timeOfLastAttack >= es.attackSpeed) {
                            g.heath -= (es.attackDamage - g.armour);
                            es.timeOfLastAttack = gameTime;

                        }
                    } else if (g.heath <= 0) {
                        unitGiantList.remove(i);
                        playerScore -= 50;
                        es.movingXSpeed = 2;
                        es.movingYSpeed = 2;
                        allyDeathClip.setFramePosition(0);
                        allyDeathClip.start();

                    } else {
                        unitEnemySpearmanList.remove(j);
                        g.movingXSpeed = 2;
                        g.movingYSpeed = 2;
                        playerScore += 100;
                        enemyDeathClip.setFramePosition(0);
                        enemyDeathClip.start();

                    }
                }
            }
            g.Update();
        }
    }

    public void updateEnemySpearmen(long gameTime) {

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

    protected void tickGold(long gameTime) {
        long timeBetweenGoldTicks = Framework.secInNanosec;
        if (gameTime - lastGoldTick >= timeBetweenGoldTicks) {
            playerGold += 50;
            enemyGold += 50;
            lastGoldTick = gameTime;
        }
    }
}
