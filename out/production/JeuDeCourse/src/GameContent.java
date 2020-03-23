import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;

public class GameContent extends JPanel implements ActionListener {
    private GameDisplay gameDisplay;
    private FenetrePrincipale f;

    private Joueur[] joueurs;
    private Game game;

    private String PRESSED = " pressed";
    private String RELEASED = " released";
    private int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private LinkedList<Integer> pressedKeys;

    private Timer timer;
    private int DELTA_T = 50;

    private int width;
    private int height;


    public GameContent(int width, int height, FenetrePrincipale f){
        this.width = width;
        this.height = height;
        this.f = f;

        setPreferredSize(new Dimension(width,height));
        // Définition des joueurs
        joueurs = new Joueur[2];

        joueurs[0] = new Joueur("1");
        joueurs[0].setColor(Color.BLACK);

        joueurs[1] = new Joueur("test");
        joueurs[1].getBinds().setArrowBind();
        joueurs[1].setColor(Color.GREEN);

        // definition des binds
        setKeyBindings();

        // initialisation de la partie
        launchGame("circuit1");

        // liste gérant l'appuie des touches
        pressedKeys = new LinkedList<>();

        // init du panel affichant la partie
        gameDisplay = new GameDisplay(game);
        gameDisplay.setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        add(gameDisplay,BorderLayout.CENTER);

        // init du timer
        timer = new Timer(DELTA_T, this);
        timer.start();
    }

    public void launchGame(String idGame){
        game = new Game(joueurs, idGame, width, height);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
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

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == timer){
            game.tick(pressedKeys);
        }
        gameDisplay.dessine();
        if(game.isOver()>-1){
            launchGame("circuit1");
            f.getPanelSelection().show(f.getCardContent(), "menu");
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

    public void setMap(int i){

    }
}
