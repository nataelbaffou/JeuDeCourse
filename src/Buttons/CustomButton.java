package Buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;

public class CustomButton extends JButton{
    Color colorRollover;
    Color colorArmed;
    public CustomButton(String text){
        super(text);
        setRolloverEnabled(true);
        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);

        setBackground(new Color(0,true));

        colorRollover = new Color(100, 100, 100, 220);

        colorArmed = new Color(60, 60, 60, 220);

        setForeground(Color.black);

        repaint();

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
            g.setColor(colorArmed);
            g.fillRoundRect(0, 0, width, height, height, height);
        }
        else if(getModel().isRollover()){
            int width = getWidth();
            int height = getHeight();
            g.setColor(colorRollover);
            g.fillRoundRect(0, 0, width, height, height, height);
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
