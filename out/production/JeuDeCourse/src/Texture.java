import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Texture{
    private BufferedImage img = null;
    private String name;
    private boolean isBlocking = false;

    public Texture(){
        this(System.getProperty("user.dir")+"/res/textures/0grass.png", "default");
    }

    public Texture(String path, String name){
        this.name = name;
        if(name.matches("0wall.png")){
            isBlocking = true;
        }
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dessine(Graphics g, Position P){
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(P.getRad(), P.x, P.y);
        g2d.drawImage(img, P.x-P.centerX, P.y-P.centerY, P.width, P.height, null);
        g2d.setTransform(old);
    }

    public boolean isBlocking(){
        return isBlocking;
    }

}