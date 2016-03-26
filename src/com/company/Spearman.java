package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ZaGunny on 24/03/2016.
 */
public class Spearman {
    private static final long timeBetweenSpearmenCreatedInit = Framework.secInNanosec * 1;
    public static long timeBetweenNewSpearmen = timeBetweenSpearmenCreatedInit;
    public static long timeOfLastCreatedSpearman = 0;

    private final int healthInit = 100;
    public int heath;

    public int xCoord;
    public int yCoord;
    public boolean isSelected;

    private double movingXSpeed;
    private double movingYSpeed;
    public final int attackRange = 50;
    public final int aggroRange = 200;
    public double attackDamage = 10;
    public int armour = 2;
    public int goldCost = 100;
    public double attackSpeed = Framework.secInNanosec * 0.2;
    public static double timeOfLastAttack = 0;

    public static BufferedImage spearmanImg;


    public void Initialise(int xCoord, int yCoord) {
        this.heath = healthInit;
        this.movingXSpeed = 0.2;
        this.movingYSpeed = 0;
        this.isSelected = false;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public static void restartSpearman() {
        Spearman.timeBetweenNewSpearmen = timeBetweenSpearmenCreatedInit;
        Spearman.timeOfLastCreatedSpearman = 0;
    }

    public void Update() {
        xCoord += movingXSpeed;


    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(spearmanImg, xCoord, yCoord, 89, 105, null);
    }

}
