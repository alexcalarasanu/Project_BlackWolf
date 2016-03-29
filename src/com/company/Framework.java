package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ZaGunny
 */
public class Framework extends Canvas {
    public static int frameWidth;
    public static int frameHeight;
    public static int levelSelected;

    public static final long milisecInNanosec = 1000000L;
    public static final long secInNanosec = 1000000000L;
    private final int GAME_FPS = 60;
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    public static enum GameState {STARTING, VISUALISING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED, HELP, HIGHSCORE}

    public static GameState gameState;
    private long gameTime;
    private long lastTime;
    private Game game;

    private BufferedImage mainMenuImg;
    private BufferedImage startButtonImg;
    private BufferedImage optionsButtonImg;
    private BufferedImage helpButtonImg;
    private BufferedImage exitButtonImg;
    private BufferedImage titleImg;
    private BufferedImage helpBGImg;
    private BufferedImage optionsBGImg;
    private BufferedImage victoryScreen;
    private BufferedImage defeatyScreen;
    private BufferedImage highscoresBG;
    private BufferedImage highscoresButt;
    private Font goldFont = new Font("Futura", Font.BOLD, 20);
    java.util.List<Highscore> scoresList = new ArrayList<Highscore>();

    public Framework() {
        super();

        gameState = GameState.VISUALISING;

        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run() {
                GameLoop();
            }
        };
        gameThread.start();
    }

    private void Initialize() {

    }

    private void LoadContent() {
        try {
            URL mainMenuBGURL = this.getClass().getResource("res/main_menu_bg.png");
            URL startURL = this.getClass().getResource("res/startButton.png");
            URL optionsURL = this.getClass().getResource("res/optionsButton.png");
            URL helpURL = this.getClass().getResource("res/helpButton.png");
            URL exitURL = this.getClass().getResource("res/exitButton.png");
            URL titleURL = this.getClass().getResource("res/titleImage2.png");
            URL helpBGURL = this.getClass().getResource("res/helpBackground.png");
            URL optionsBG = this.getClass().getResource("res/optionsBackground.png");
            URL victoryScreenURL = this.getClass().getResource("res/victoryScreen.png");
            URL defeatScreenURL = this.getClass().getResource("res/defeatScreen.png");
            URL highScoresbURL = this.getClass().getResource("res/highscoreButton.png");
            URL highscoresURL = this.getClass().getResource("res/interfaceBackground.png");

            mainMenuImg = ImageIO.read(mainMenuBGURL);
            highscoresBG = ImageIO.read(highscoresURL);
            highscoresButt = ImageIO.read(highScoresbURL);
            optionsBGImg = ImageIO.read(optionsBG);
            startButtonImg = ImageIO.read(startURL);
            optionsButtonImg = ImageIO.read(optionsURL);
            helpButtonImg = ImageIO.read(helpURL);
            exitButtonImg = ImageIO.read(exitURL);
            titleImg = ImageIO.read(titleURL);
            helpBGImg = ImageIO.read(helpBGURL);
            victoryScreen = ImageIO.read(victoryScreenURL);
            defeatyScreen = ImageIO.read(defeatScreenURL);


        } catch (Exception ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void GameLoop() {
        long visualisingTime = 0, lastVisualisingTime = System.nanoTime();
        //Variables used to see calculate for how long the thread needs to be put to sleep, to match GAME_FPS
        long beginTime, timeTaken, timeLeft;

        while (true) {
            beginTime = System.nanoTime();
            switch (gameState) {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    game.UpdateGame(gameTime, mousePosition());
                    lastTime = System.nanoTime();
                    break;
                case GAMEOVER:
                    String s;
                    do {
                        s = JOptionPane.showInputDialog("Input player name");
                    } while (s.isEmpty() || s.length() > 10);
                    JDBC j = new JDBC();
                    j.enterScore(s, Game.playerScore);
                    gameState = GameState.MAIN_MENU;
                    break;
                case MAIN_MENU:
                    break;
                case OPTIONS:
                    if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                        if (mousePosition().getX() <= 1080 && mousePosition().getX() >= 820 && mousePosition().getY() <= 960 && mousePosition().getY() >= 920) {
                            gameState = GameState.MAIN_MENU;
                        }
                        if (mousePosition().getX() >= 90 && mousePosition().getX() <= 590 && mousePosition().getY() >= 455 && mousePosition().getY() <= 732) {
                            levelSelected = 0;
                            gameState = GameState.MAIN_MENU;
                        }
                        if (mousePosition().getX() >= 1355 && mousePosition().getX() <= 1850 && mousePosition().getY() >= 455 && mousePosition().getY() <= 732) {
                            levelSelected = 1;
                            gameState = GameState.MAIN_MENU;
                        }

                    }
                    break;
                case GAME_CONTENT_LOADING:
                    break;
                case STARTING:
                    Initialize();
                    LoadContent();
                    gameState = GameState.MAIN_MENU;
                    break;
                case DESTROYED:
                    break;
                case HELP:
                    if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                        if (mousePosition().getX() <= 1080 && mousePosition().getX() >= 820 && mousePosition().getY() <= 960 && mousePosition().getY() >= 920) {
                            gameState = GameState.MAIN_MENU;
                        }
                    }
                    //...
                    break;
                case VISUALISING:
                    if (this.getWidth() > 1 && visualisingTime > secInNanosec) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();
                        gameState = GameState.STARTING;
                    } else {
                        visualisingTime += System.nanoTime() - lastVisualisingTime;
                        lastVisualisingTime = System.nanoTime();
                    }
                    break;
                case HIGHSCORE:
                    if (Canvas.mouseButtonState(MouseEvent.BUTTON1))
                        if (mousePosition().getX() <= 1080 && mousePosition().getX() >= 820 && mousePosition().getY() <= 960 && mousePosition().getY() >= 920) {
                            gameState = GameState.MAIN_MENU;
                        }
                    break;
            }

            repaint();
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec;
            if (timeLeft < 10)
                timeLeft = 10;
            try {
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void Draw(Graphics2D g2d) {
        switch (gameState) {
            case PLAYING:
                game.Draw(g2d);
                break;
            case GAMEOVER:
                if (Game.playerWon)
                    g2d.drawImage(victoryScreen, 0, 0, frameWidth, frameHeight, null);
                else
                    g2d.drawImage(defeatyScreen, 0, 0, frameWidth, frameHeight, null);
                break;
            case MAIN_MENU:
                g2d.drawImage(mainMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.drawImage(titleImg, frameWidth / 2 - 350, 100, 640, 200, null);
                g2d.drawImage(startButtonImg, frameWidth / 2 - 350, 350, 640, 100, null);
                g2d.drawImage(optionsButtonImg, frameWidth / 2 - 350, 500, 640, 100, null);
                g2d.drawImage(helpButtonImg, frameWidth / 2 - 350, 650, 640, 100, null);
                g2d.drawImage(exitButtonImg, frameWidth / 2 - 350, 800, 640, 100, null);
                g2d.drawImage(highscoresButt, 100, 1000, 320, 50, null);
                break;
            case OPTIONS:
                g2d.drawImage(optionsBGImg, 0, 0, frameWidth, frameHeight, null);
                break;
            case STARTING:
                break;
            case VISUALISING:
                break;
            case GAME_CONTENT_LOADING:
                //...
                break;
            case HELP:
                g2d.drawImage(helpBGImg, 0, 0, frameWidth, frameHeight, null);
            case DESTROYED:

                break;
            case HIGHSCORE:
                g2d.setFont(goldFont);
                g2d.setColor(Color.yellow);
                g2d.drawImage(highscoresBG, 0, 0, frameWidth, frameHeight, null);
                for (int i = 0; i < scoresList.size(); i++) {
                    if (i < 15) {
                        g2d.drawString(String.valueOf(i + 1), 50, 75 * (i + 1));
                        g2d.drawString(scoresList.get(i).getUserName(), 100, 75 * (i + 1));
                        g2d.drawString(String.valueOf(scoresList.get(i).getScore()), 500, 75 * (i + 1));
                    }
                }
                break;
        }
    }

    private void newGame() {
        gameTime = 0;
        lastTime = System.nanoTime();
        game = new Game();
        EnemySpearman.restartSpearman();
        Spearman.restartSpearman();
        Giant.restartGiants();
    }

    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();
            if (mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }

    @Override
    public void keyReleasedFramework(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (gameState == Framework.GameState.MAIN_MENU) {
            //Start Button
            if (mx >= frameWidth / 2 - 350 && mx <= frameWidth / 2 + 290)
                if (my >= 350 && my <= 450) {
                    newGame();
                }
            //Options button
            if (mx >= frameWidth / 2 - 350 && mx <= frameWidth / 2 + 290)
                if (my >= 500 && my <= 600)
                    gameState = Framework.GameState.OPTIONS;
//            //Help button
            if (mx >= frameWidth / 2 - 350 && mx <= frameWidth / 2 + 290)
                if (my >= 650 && my <= 750)
                    gameState = GameState.HELP;
//                    //Exit Button
            if (mx >= frameWidth / 2 - 350 && mx <= frameWidth / 2 + 290)
                if (my >= 800 && my <= 900)
                    System.exit(0);
//            high scores
            if (mx >= 100 && mx <= 420 && my >= 1000 && my <= 1050) {
                gameState = GameState.HIGHSCORE;
                JDBC j = new JDBC();
                scoresList = j.getScores();
            }
        }
    }

}
