package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Giant {
    private static final long timeBetweenGiantsCreatedInit = Framework.secInNanosec * 1;
    public static long timeBetweenNewGiants = timeBetweenGiantsCreatedInit;
    public static long timeOfLastCreatedGiant = 0;

    private final int healthInit = 100;
    public int heath;

    public int xCoord;
    public int yCoord;

    public int movingXSpeed;
    public int movingYSpeed;
    public final int aggroRange = 200;

    public int attackDamage = 25;
    public int armour = 4;
    public static int goldCost = 200;
    public long attackSpeed = Framework.secInNanosec / 10 * 5;
    public long timeOfLastAttack = 0;

    public static BufferedImage giantImg;


    public void Initialise(int xCoord, int yCoord) {
        this.heath = healthInit;
        this.movingXSpeed = 1;
        this.movingYSpeed = 1;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public static void restartGiants() {
        Giant.timeBetweenNewGiants = timeBetweenGiantsCreatedInit;
        Giant.timeOfLastCreatedGiant = 0;
    }

    public void Update() {
        xCoord += movingXSpeed;
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(giantImg, xCoord, yCoord, 105, 89, null);
    }

}
