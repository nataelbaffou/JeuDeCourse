import javax.swing.*;
import java.awt.*;

public class GameDisplay extends JPanel{

    private Game game;

    public GameDisplay(Game game){
        this.game = game;
    }

    public void setMap(int i){
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        game.dessineMap(g);
        game.dessineJoueurs(g);
    }

    public void dessine(){
        repaint();
    }

}
