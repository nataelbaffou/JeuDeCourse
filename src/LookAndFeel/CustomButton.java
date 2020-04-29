package LookAndFeel;

import javax.swing.*;
import javax.swing.border.Border;
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
    Color bgColor = new Color(0,true);
    boolean isRound = true;
    public CustomButton(String text){
        super(text);
        setRolloverEnabled(true);
        setBorder(null);
        setBorderPainted(true);
        setContentAreaFilled(true);

        setBackground(bgColor);

        colorRollover = new Color(100, 100, 100, 220);

        colorArmed = new Color(60, 60, 60, 220);

        setForeground(Color.black);

        setFont(DesignFont.getTitleFont());
    }

    public CustomButton(String text, Color bg){
        this(text);
        setBackground(bg);
        bgColor = bg;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setPaintMode();
        //Paint bg
        int width = getWidth();
        int height = getHeight();
        g.setColor(bgColor);
        g.fillRect(0, 0, width, height);

        if(getModel().isArmed()){
            g.setColor(colorArmed);
            if(isRound){
                g.fillRoundRect(0, 0, width, height, height, height);
            }else{
                g.fillRect(0, 0, width, height);
            }
        }
        else if(getModel().isRollover()){
            g.setColor(colorRollover);
            if(isRound){
                g.fillRoundRect(0, 0, width, height, height, height);
            }else{
                g.fillRect(0, 0, width, height);
            }
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

    public void setRoundBorder(boolean val){
        isRound = val;
    }
}
