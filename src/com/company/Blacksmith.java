package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;


class Blacksmith {

    public int xCoord;
    public int yCoord = 30;
    public static int goldCost = 300;

    public static BufferedImage buildingImg;

    public Blacksmith() {
        Initialise(xCoord, yCoord);
    }

    public void Initialise(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public void Draw(Graphics2D g2d, int i) {
        g2d.drawImage(buildingImg, xCoord + (i * 100), yCoord, 100, 100, null);
    }
}
