import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameContent extends JPanel implements KeyListener {
    private GameDisplay gameDisplay;
    public GameContent(int size){
        gameDisplay = new GameDisplay();
        gameDisplay.setPreferredSize(new Dimension(size, size));
        setLayout(new BorderLayout());
        add(gameDisplay,BorderLayout.CENTER);
        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameDisplay.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gameDisplay.keyReleased(e);
    }
}
