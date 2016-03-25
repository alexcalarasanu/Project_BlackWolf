package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ZaGunny on 24/03/2016.
 */
public class Soldier {
    private static final long timeBetweenSoldiersCreatedInit = Framework.secInNanosec * 10;
    public static long timeBetweenNewSoldiers = timeBetweenSoldiersCreatedInit;
    public static long timeOfLastCreatedSoldier = 0;

    private final int healthInit = 100;
    public int heath;

    public int xCoord;
    public int yCoord;
    public boolean isSelected;

    private double movingXSpeed;
    private double movingYSpeed;

    public static BufferedImage soldierImg;


    public void Initialise(int xCoord, int yCoord) {
        this.heath = healthInit;
        this.movingXSpeed = 0;
        this.movingYSpeed = 0;
        this.isSelected = false;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public static void restartSoldier() {
        Soldier.timeBetweenNewSoldiers = timeBetweenSoldiersCreatedInit;
        Soldier.timeOfLastCreatedSoldier = 0;
    }

    public void Update() {
        xCoord += movingXSpeed;
        yCoord += movingYSpeed;
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(soldierImg, xCoord, yCoord, null);
    }

}
