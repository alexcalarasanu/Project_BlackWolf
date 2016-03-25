package com.company;

/**
 * Created by ZaGunny on 22/03/2016.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ZaGunny
 */
public class Framework extends Canvas {
    public static int frameWidth;
    public static int frameHeight;

    public static final long milisecInNanosec = 1000000L;
    public static final long secInNanosec = 1000000000L;
    private final int GAME_FPS = 60;
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    public static enum GameState {STARTING, VISUALISING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}

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
            mainMenuImg = ImageIO.read(mainMenuBGURL);

            URL startURL = this.getClass().getResource("res/startButton.png");
            URL optionsURL = this.getClass().getResource("res/optionsButton.png");
            URL helpURL = this.getClass().getResource("res/helpButton.png");
            URL exitURL = this.getClass().getResource("res/exitButton.png");
            URL titleURL = this.getClass().getResource("res/titleImage2.png");

            startButtonImg = ImageIO.read(startURL);
            optionsButtonImg = ImageIO.read(optionsURL);
            helpButtonImg = ImageIO.read(helpURL);
            exitButtonImg = ImageIO.read(exitURL);
            titleImg = ImageIO.read(titleURL);

        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void GameLoop() {
        long visualisingTime = 0, lastVisualisingTime = System.nanoTime();
        //Variables used to see calculate for how long the thread needts to be put to sleep, to match GAME_FPS
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
                    break;
                case MAIN_MENU:
                    break;
                case OPTIONS:
                    break;
                case GAME_CONTENT_LOADING:
                    break;
                case STARTING:
                    Initialize();
                    LoadContent();
                    gameState = GameState.MAIN_MENU;
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
                game.Draw(g2d, mousePosition(), gameTime);
                break;
            case GAMEOVER:
                //...
                break;
            case MAIN_MENU:
                g2d.drawImage(mainMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.drawImage(titleImg, frameWidth / 2 - 350, 100, 640, 200, null);
                g2d.drawImage(startButtonImg, frameWidth / 2 - 350, 350, 640, 100, null);
                g2d.drawImage(optionsButtonImg, frameWidth / 2 - 350, 500, 640, 100, null);
                g2d.drawImage(helpButtonImg, frameWidth / 2 - 350, 650, 640, 100, null);
                g2d.drawImage(exitButtonImg, frameWidth / 2 - 350, 800, 640, 100, null);
                break;
            case OPTIONS:
                //...
                break;
            case STARTING:
                break;
            case VISUALISING:
                break;
            case GAME_CONTENT_LOADING:
                //...
                break;
            case DESTROYED:

                break;
        }
    }

    private void newGame() {
        gameTime = 0;
        lastTime = System.nanoTime();
        game = new Game();
    }

    private void restartGame() {
        gameTime = 0;
        lastTime = System.nanoTime();
        game.RestartGame();
        gameState = GameState.PLAYING;
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

    /**
     * This method is called when keyboard key is released.
     *
     * @param e KeyEvent
     */
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
                    System.out.printf("playing");
                }
            //Options button
            if (mx >= frameWidth / 2 - 350 && mx <= frameWidth / 2 + 290)
                if (my >= 500 && my <= 600)
                    gameState = Framework.GameState.OPTIONS;
            //Help button
            if (mx >= frameWidth / 2 - 350 && mx <= frameWidth / 2 + 290)
                if (my >= 650 && my <= 750)
//                    gameState = Framework.GameState.PLAYING;
                    //Exit Button
                    if (mx >= frameWidth / 2 - 350 && mx <= frameWidth / 2 + 290)
                        if (my >= 800 && my <= 900)
                            System.exit(0);
    }
    }
}
