package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ZaGunny on 26/03/2016.
 */
public class EnemySpearman {
    private static final long timeBetweenEnemySpearmenCreatedInit = Framework.secInNanosec * 1;
    public static long timeBetweenNewEnemySpearmen = timeBetweenEnemySpearmenCreatedInit;
    public static long timeOfLastCreatedEnemySpearman = 0;

    private final int healthInit = 50;
    public int heath;

    public int xCoord;
    public int yCoord;
    public boolean isSelected;

    public double movingXSpeed;
    public double movingYSpeed;
    public final int attackRange = 50;
    public final int aggroRange = 200;
    public int attackDamage = 10;
    public int armour = 2;
    public static int goldCost = 100;
    public long attackSpeed = Framework.secInNanosec / 10 * 2;
    public long timeOfLastAttack = 0;

    public static BufferedImage spearmanImg;


    public void Initialise(int xCoord, int yCoord) {
        this.heath = healthInit;
        this.movingXSpeed = 2;
        this.movingYSpeed = 2;
        this.isSelected = false;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public static void restartSpearman() {
        EnemySpearman.timeBetweenNewEnemySpearmen = timeBetweenEnemySpearmenCreatedInit;
        EnemySpearman.timeOfLastCreatedEnemySpearman = 0;
    }

    public void Update() {
        this.xCoord -= movingXSpeed;
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(spearmanImg, xCoord, yCoord, 63, 53, null);

    }

}

