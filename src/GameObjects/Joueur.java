package GameObjects;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

public class Joueur{
    private Binds binds;
    private Vehicule V;
    private String nom;
    Color color;

    public Joueur(String pNom){
        nom = pNom;
        V = new Karting();
        binds = new Binds();
    }

    public void setVehicule(Position initPos, int widthCase){
        V = new Karting(initPos, widthCase, color);
    }

    public Binds getBinds() {
        return binds;
    }

    public void avancer(LinkedList<Integer> keyPressed, Map map){
        Collection<Integer> c = binds.values();
        boolean[] usefulKeyPressed = new boolean[c.size()];
        int i = 0;
        for(int a: c){
            usefulKeyPressed[i] = keyPressed.contains(a);
            i++;
        }
        V.avancer(usefulKeyPressed, map);
    }

    public void setColor(Color c){
        color = c;
        V.setColor(c);
    }

    public void dessine(Graphics g){
        V.dessine(g);
    }

    public Vehicule getVehicule() {
        return V;
    }

    public String getNom(){
        return nom;
    }

    public Color getColor(){
        return color;
    }
}