package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.company.Framework.frameHeight;
import static com.company.Framework.frameWidth;

/**
 * Created by ZaGunny on 24/03/2016.
 */
public class Terrain {
    private BufferedImage terrainImg;

    public Terrain() {
        LoadContent();
    }

    private void LoadContent() {
        try {
            URL terrainURL = this.getClass().getResource("res/terrain.png");
            terrainImg = ImageIO.read(terrainURL);
        } catch (IOException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(terrainImg, 0, 0, frameWidth, frameHeight, null);
    }
}
