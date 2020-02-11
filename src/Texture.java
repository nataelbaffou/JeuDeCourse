import javax.imageio.ImageIO;
import java.awt.*;
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
        this(System.getProperty("user.dir")+"/res/textures/grass.png", "default");
    }

    public Texture(String path, String name){
        this.name = name;
        if(name.matches("wall[a-zA-Z_0-9]*")){
            isBlocking = true;
        }
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dessine(Graphics g, Position P){
        g.drawImage(img, P.x, P.y, P.width, P.height, null);
    }

    public boolean isBlocking(){
        return isBlocking;
    }

}
