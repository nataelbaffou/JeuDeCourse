package Buttons;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class LevelButton extends JButton {
    public LevelButton(String text){
        super(text);
        setBorder(null);
        setRolloverEnabled(true);
        setBackground(new Color(0, true));
        setForeground(Color.black);

        try {
            setFont(Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/fonts/memphis5.ttf")).deriveFont(25F));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            setFont(new Font("Arial",Font.PLAIN,20));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setPaintMode();
        if(getModel().isArmed()){
            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(140, 140, 140, 200));
            g.fillRoundRect(0, 0, width, height, height/2, height/2);
        }
        else if(!getModel().isRollover()){
            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(200, 200, 200, 90));
            g.fillRoundRect(0, 0, width, height, height/2, height/2);
        }
        else if(getModel().isRollover()){
            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(200, 200, 200, 200));
            g.fillRoundRect(0, 0, width, height, height/2, height/2);
        }
        super.paintComponent(g);
        betterRender(g);
    }

    public void betterRender(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

}
