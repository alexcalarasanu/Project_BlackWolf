package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ZaGunny on 25/03/2016.
 */
public class BuildingOne {


    private final int healthInit = 1000;

    public int health;

    public int xCoord;
    public int yCoord;

    public BufferedImage building1Img;

    public BuildingOne() {
        Initialise(xCoord, yCoord);
    }

    public void Initialise(int xCoord, int yCoord) {
        this.health = healthInit;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(building1Img, xCoord, yCoord, null);
    }
}
