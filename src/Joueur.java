import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.LinkedList;

public class Joueur{
    private Binds binds;
    private Vehicule V;
    private String nom;
    Color color;
    private int points=0;
    private int[] touchesJoueur;

    public Joueur(String pNom){
        nom = pNom;
        V = new Karting(100, 100);
        binds = new Binds();
        Object[] test = binds.values().toArray();
    }

    public Binds getBinds() {
        return binds;
    }

    //TODO setTouches();

    public void avancer(LinkedList<Integer> keyPressed){
        Collection<Integer> c = binds.values();
        System.out.println(c);
        boolean[] usefulKeyPressed = new boolean[c.size()];
        int i = 0;
        for(int a: c){
            usefulKeyPressed[i] = keyPressed.contains(a);
            i++;
        }
        V.avancer(usefulKeyPressed);
    }

    public void setColor(Color c){
        color = c;
        V.setColor(c);
    }

    public void dessine(Graphics g){

        V.dessine(g);
    }
}