import javax.swing.*;
import java.awt.*;

public class GameContent extends JPanel {
    private GameDisplay gameDisplay;



    public GameContent(int width, int height){
        gameDisplay = new GameDisplay(width, height);
        gameDisplay.setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        add(gameDisplay,BorderLayout.CENTER);
    }
}
