package GameEngine;

import GameObjects.Joueur;
import Pages.FenetrePrincipale;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GameContent extends JPanel implements ActionListener {
    private GameDisplay gameDisplay;
    private FenetrePrincipale f;

    private ArrayList<Joueur> joueurs;
    private Game game;
    private String idGame = "default";

    private static String PRESSED = " pressed";
    private static String RELEASED = " released";
    private static int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private LinkedList<Integer> pressedKeys;

    private Timer timer;
    private static int DELTA_T = 50;


    private int width;
    private int height;


    public GameContent(int width, int height, FenetrePrincipale f){
        this.width = width;
        this.height = height;
        this.f = f;

        setPreferredSize(new Dimension(width,height));

        // Définition des joueurs
        joueurs = new ArrayList<>();
        setPlayers();


        // liste gérant l'appuie des touches
        pressedKeys = new LinkedList<>();

        // init du timer
        timer = new Timer(DELTA_T, this);

        // initialisation de la partie
        game = new Game(f);

        // init du panel affichant la partie
        gameDisplay = new GameDisplay(game);
        gameDisplay.setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        add(gameDisplay,BorderLayout.CENTER);
    }

    public void launchGame(){
        setKeyBindings();
        pressedKeys.clear();
        gameDisplay.initGame(joueurs, idGame, width, height);
        f.getMusiqueFond().playTheme("race");
        gameDisplay.showCountdown();
        timer.start();
    }

    public void endGame(){
        timer.stop();
        f.getMusiqueFond().playTheme("menu");
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
            endGame();
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

    public void setGame(String name){
        idGame = name;
    }

    public void setPlayers(int[][] binds){
        Color[] colors = {Color.RED, Color.GREEN, new Color(183, 0, 255), Color.BLUE, new Color(255, 115, 0), Color.YELLOW};
        String[] names = {"Fred", "Greenlee", "Purploo", "Bluebell", "Willem", "Yellan"};

        joueurs.clear();
        for(int iBind = 0; iBind < binds.length; iBind++){
            if(binds[iBind][0] != -1){
                Joueur j = new Joueur(names[iBind]);
                j.setColor(colors[iBind]);
                j.getBinds().setBind(binds[iBind]);
                joueurs.add(j);
            }
        }
    }

    public void setPlayers(){
        joueurs.clear();
        Joueur j = new Joueur("Fred");
        j.setColor(Color.RED);
        joueurs.add(j);
    }
}
