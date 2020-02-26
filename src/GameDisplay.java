import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.LinkedList;

public class GameDisplay extends JPanel implements ActionListener {
    private Joueur[] joueurs;
    private Map map;
    private Timer timer;
    private int DELTA_T = 50;
    private String PRESSED = " pressed";
    private String RELEASED = " released";
    private int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private LinkedList<Integer> pressedKeys;

    public GameDisplay(int width, int height){

        map = new Map(width, height, "0");

        joueurs = new Joueur[2];

        joueurs[0] = new Joueur("1");
        joueurs[0].setColor(Color.BLACK);

        joueurs[1] = new Joueur("test");
        joueurs[1].getBinds().setArrowBind();
        joueurs[1].setColor(Color.GREEN);

        setKeyBindings();

        pressedKeys = new LinkedList<>();

        timer = new Timer(DELTA_T, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        map.dessine(g);
        for(Joueur j: joueurs){
            j.dessine(g);
        }
    }

    private void setKeyBindings() {
        HashMap<String,Integer> h;
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(IFW);
        int key;

        for(Joueur j: joueurs){
            h = j.getBinds();
            for(String s: h.keySet()){
                key = h.get(s);
                inputMap.put(KeyStroke.getKeyStroke(key, 0, false),key+PRESSED);
                Action pressedAction = new KeyAction(key, false);
                actionMap.put(key+PRESSED, pressedAction);
                inputMap.put(KeyStroke.getKeyStroke(key, 0, true),key+RELEASED);
                Action releasedAction = new KeyAction(key, true);
                actionMap.put(key+RELEASED, releasedAction);
            }
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == timer){
            for(Joueur j: joueurs){
                j.avancer(pressedKeys, map);
            }
        }
        repaint();
    }



    private void handleKeyEvent(int key, boolean onKeyReleased) {
        if (onKeyReleased) {
            if (pressedKeys.contains(key)){
                pressedKeys.remove((Object) key);
            }
        }
        else {
            if (!pressedKeys.contains(key)) {
                pressedKeys.add(key);
            }
        }
    }

    private class KeyAction extends AbstractAction implements ActionListener {
        private int key;
        private boolean onKeyReleased;

        public KeyAction(int key, boolean onKeyReleased) {
            this.key = key;
            this.onKeyReleased = onKeyReleased;
        }

        public void actionPerformed(ActionEvent e)
        {
            handleKeyEvent(key, onKeyReleased);
        }
    }
}
