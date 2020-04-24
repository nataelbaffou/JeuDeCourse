import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class CustomButton extends JButton {
    Color c;
    Color r;
    public CustomButton(String text){
        super(text);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);

        setBackground(new Color(255,255,255,0));

        c = new Color(100, 100, 100, 200);

        r = new Color(0xC8FF0000, true);

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

    public void paintComponent(Graphics g) {
        g.setPaintMode();
        if(getModel().isArmed()){
            int width = getWidth();
            int height = getHeight();
            g.setColor(r);
            g.fillRoundRect(0, 0, width, height, height, height);
        }
        else if(getModel().isRollover()){
            int width = getWidth();
            int height = getHeight();
            g.setColor(c);
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
