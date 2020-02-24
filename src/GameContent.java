import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameContent extends JPanel {
    private GameDisplay gameDisplay;
    public GameContent(int size){
        gameDisplay = new GameDisplay();
        gameDisplay.setPreferredSize(new Dimension(size, size));
        setLayout(new BorderLayout());
        add(gameDisplay,BorderLayout.CENTER);
    }
}
