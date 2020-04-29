package LookAndFeel;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class DesignFont {
    public static Font getTitleFont(){
        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/fonts/memphis5.ttf")).deriveFont(25F);
        } catch (FontFormatException e) {
            e.printStackTrace();
            f = new Font("Arial",Font.PLAIN,20);
        } catch (IOException e) {
            e.printStackTrace();
            f = new Font("Arial",Font.PLAIN,20);
        }
        return f;
    }

    public static Font getBindsFont(){
        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/fonts/memphis5.ttf")).deriveFont(Font.PLAIN, 30);
        } catch (FontFormatException e) {
            e.printStackTrace();
            f = new Font("Arial",Font.PLAIN,20);
        } catch (IOException e) {
            e.printStackTrace();
            f = new Font("Arial",Font.PLAIN,20);
        }
        return f;
    }
}
