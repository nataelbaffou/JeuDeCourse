package GameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class GameDisplay extends JPanel{

    private Game game;

    public GameDisplay(Game game){
        this.game = game;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        int width = g.getClipBounds().width;
        int height = g.getClipBounds().height;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        Rectangle rect = game.getMapSize();
        ((Graphics2D) g).setTransform(AffineTransform.getTranslateInstance((width-rect.width)/2, (height-rect.height)/2));
        game.dessineMap(g);
        game.dessineJoueurs(g);
    }

    public void dessine(){
        repaint();
    }

}
