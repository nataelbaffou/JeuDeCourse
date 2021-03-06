package LookAndFeel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageButton extends JButton {

    BufferedImage imageBG;
    String path;
    public ImageButton(String title, String toolTip,String path, Dimension size){
        super(title);
        setPreferredSize(size);
        setBorder(null);
        setBorderPainted(false);
        setToolTipText(toolTip);
        this.path = path;
        try {
            imageBG = ImageIO.read(new File(path));
        }catch(Exception e){
            e.printStackTrace();
        }
        setBackground(Color.white);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(imageBG,0,0,getWidth(),getHeight(),null);
    }

    public BufferedImage getImageBG() {
        return imageBG;
    }
}
