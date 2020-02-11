import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Joueur{
    private Vehicule V;
    private String nom;
    private int points=0;
    private int[] touchesJoueur;

    public Joueur(String pNom){
        nom = pNom;
        V = new Karting(100, 100);
        touchesJoueur = new int[4];
        touchesJoueur[0] = KeyEvent.VK_DOWN;
        touchesJoueur[1] = KeyEvent.VK_LEFT;
        touchesJoueur[2] = KeyEvent.VK_UP;
        touchesJoueur[3] = KeyEvent.VK_RIGHT;
    }

    public void setTouches(int[] nouvellesTouches) {
        if(nouvellesTouches.length == touchesJoueur.length){
            touchesJoueur = nouvellesTouches.clone();
        } else {
            System.out.println("Taille de liste non conforme");
        }
    }

    public void avancer(LinkedList<Integer> keyPressed, Map map){
        boolean[] usefulKeyPressed = new boolean[touchesJoueur.length];
        for(int i = 0; i < touchesJoueur.length; i++){
            usefulKeyPressed[i] = keyPressed.contains(touchesJoueur[i]);
        }
        V.avancer(usefulKeyPressed, map);
    }

    public void dessine(Graphics g){
        V.dessine(g);
    }
}