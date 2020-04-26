package GameObjects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import IOEngine.IOFiles;

public class Texture{
    private BufferedImage img = null;
    private String name;
    private boolean isBlocking = false;

    public Texture(){
        this(System.getProperty("user.dir")+"/res/textures/grass.png", "default");
    }

    public Texture(BufferedImage img, String name, boolean isBlocking){
        this.img = img;
        this.name = name;
        this.isBlocking = isBlocking;
    }

    public Texture(BufferedImage img){
        this(img, "noName", false);
    }

    public Texture(String path, String name){
        this(path, name, IOFiles.getInformation("textures", "textures_settings"));
    }

    public Texture(String path, String name, Hashtable<String, String> settings){
        // TODO modifier pour mettre les paramètre au niveau de GameObjects.Case et non de GameObjects.Texture ?
        this.name = name;
        for(String keyName : settings.keySet()){
            if(name.equals(keyName)){
                String[] vals = settings.get(keyName).split("\n");
                for(String val : vals){
                    String keySetting = val.split(" : ")[0];
                    String valSetting = val.split(" : ")[1];
                    switch (keySetting){
                        case "block":
                            if(valSetting.equals("true")){
                                isBlocking = true;
                            } else if(valSetting.equals("false")){
                                isBlocking = false;
                            } else{
                                printSettingError(settings.get("filename"), keyName, keySetting);
                            }
                            break;
                            // TODO Ajouter des settings en fonction des textures
                    }
                }
            }
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
        g2d.drawImage(img, (int)(P.x-P.centerX), (int)(P.y-P.centerY), (int)(P.width), (int)(P.height), null);
        g2d.setTransform(old);
    }

    public boolean isBlocking(){
        return isBlocking;
    }

    public BufferedImage getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    private void printSettingError(String filename, String keyName, String keySetting){
        System.err.println("A wrong param was written in file : " + filename + " key : " + keyName + " keySetting : " + keySetting);
    }
}
