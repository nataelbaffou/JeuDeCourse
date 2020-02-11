import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class GameDisplay extends JPanel implements KeyListener, ActionListener {
    private Joueur J1;
    private Map map = new Map(400, 400);
    private Timer timer;
    private static int DELTA_T = 50;
    private LinkedList<Integer> listeTouches = new LinkedList<>();

    public GameDisplay(){
        J1 = new Joueur("test");
        timer = new Timer(DELTA_T, this);
        timer.start();
        addKeyListener(this);
    }

    public void paintComponent(Graphics g){
        map.dessine(g);
        J1.dessine(g);
    }

    public void keyPressed(KeyEvent e){
        if(!listeTouches.contains(e.getKeyCode())){
            listeTouches.add(e.getKeyCode());
        }
    }

    public void keyReleased(KeyEvent e){
        if(listeTouches.contains(e.getKeyCode())){
            listeTouches.remove((Object)e.getKeyCode());
        }
    }

    public void keyTyped(KeyEvent e){}

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == timer){
            J1.avancer(listeTouches, map);
        }
        repaint();
    }
}
