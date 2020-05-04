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
    private double k = 0.8; // coefficient de frottement des roues sur la route (perpendiculaire)
    private double F = 1.05; // force de frottement des roues (parallèle)
    private double kderape = 1;

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

                        case "type":
                            if(valSetting.equals("classic")){
                                k = 2.5;
                                F = 1.05;
                                kderape = 1;
                            }else if(valSetting.equals("grass")){
                                k = 0.5;
                                F = 1.1;
                                kderape = 3;
                            }else if(valSetting.equals("snow")){
                                k = 0.4;
                                F = 1.15;
                                kderape = 5;
                            }else if(valSetting.equals("sand")){
                                k = 0.6;
                                F = 1.25;
                                kderape = 1;
                            }
                            break;
                    }
                }
            }
        }

        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Can't read file " + path);
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

    public double getk(){
        return k;
    }

    public double getkderape(){
        return kderape;
    }
    public double getF(){
        return F;
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
