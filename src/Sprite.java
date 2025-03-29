 package src;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Sprite {

    private boolean isOverriden = false;
    private String defaultImgPath;
    private String overrideImgPath;

    private Image image;

    public Sprite(String imgPath) {

        this.defaultImgPath = imgPath;
    }

    public void draw(int x, int y) {

    }

    private void loadImage() {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(new File("java_2d_game\\images\\player.png"));
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
