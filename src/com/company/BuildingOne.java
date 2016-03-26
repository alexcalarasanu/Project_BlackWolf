package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.company.Framework.frameHeight;

/**
 * Created by ZaGunny on 25/03/2016.
 */
public class BuildingOne {


    private final int healthInit = 500;

    public int health;

    public int xCoord = 50;
    public int yCoord = frameHeight / 2 - 150;

    public boolean isSelected = false;

    public static BufferedImage building1Img;

    public BuildingOne() {
        Initialise(xCoord, yCoord);
    }

    public void Initialise(int xCoord, int yCoord) {
        this.health = healthInit;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.isSelected = false;
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(building1Img, xCoord, yCoord, null);
    }
}
