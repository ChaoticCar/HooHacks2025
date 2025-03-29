// package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Sprite {

    private boolean isOverriden = false;
    private String defaultImgPath;
    private String overrideImgPath;

    private Image image;
    private Entity entity;

    public Sprite(String imgPath, Entity entity) {

        this.defaultImgPath = imgPath;
        this.entity = entity;

        // load assets
        loadDefaultImage();
    }

    public int getX() {
        return entity.getX();
    }
    public int getY() {
        return entity.getY();
    }

    public void draw(Graphics g, ImageObserver observer, int x, int y) {
        // with the Point class, note that pos.getX() returns a double, but
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.drawImage(image,x,y,observer);
    }

    private void loadDefaultImage() {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(new File(defaultImgPath));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    public void overrideImg(String overridePath) {
        overrideImgPath = overridePath;
        isOverriden = true;
    }

    public void stopOverride() {
        isOverriden = false;
    }


}
