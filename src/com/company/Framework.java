package com.company;

/**
 * Created by ZaGunny on 22/03/2016.
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
                game.Draw(g2d, mousePosition());
                break;
            case GAMEOVER:
                //...
                break;
            case MAIN_MENU:
                //...
                break;
            case OPTIONS:
                //...
                break;
            case GAME_CONTENT_LOADING:
                //...
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
    public void keyReleasedFramework(KeyEvent e){

    }
    @Override
    public void mouseClicked(MouseEvent e){

    }
}
