import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture {
    private BufferedImage img = null;

    public Texture(String path){
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dessine(Graphics g, Position P){
        g.drawImage(img, P.x, P.y, P.width, P.height, null);
    }
}
