package GameEngine;

import GameObjects.Joueur;
import GameObjects.Map;
import GameObjects.Objet;
import GameObjects.Position;
import IOEngine.IOFiles;
import Pages.FenetrePrincipale;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class Game {
    private ArrayList<Joueur> players;
    private FenetrePrincipale f;
    private int nPlayers;
    private int[] lapsPerPlayer;
    private boolean[] playerInContactWithStartLine;
    private boolean[] playerOnTheEntrySideOfStartLine;
    private boolean[] playerOnTheOutputSideOfStartLine;
    private int nLaps = 1;
    private Map map;
    private String title = "default";

    public Game(FenetrePrincipale fenetrePrincipale){
        f = fenetrePrincipale;
        map = new Map();
    }

    public void initGame(ArrayList<Joueur> j, String idMap, int w, int h){
        players = j;
        Hashtable<String, String> dico = IOFiles.getInformation("maps/", idMap);
        for(java.util.Map.Entry<String, String> param : dico.entrySet()){
            switch (param.getKey()){
                case "title":
                    title = param.getValue();
                    break;
                case "laps":
                    nLaps = Integer.parseInt(param.getValue());
                    break;
            }
        }

        map.initMap(w, h, dico);

        // initialise le nombre de tours des joueurs
        nPlayers = j.size();
        playerInContactWithStartLine = new boolean[nPlayers];
        playerOnTheEntrySideOfStartLine = new boolean[nPlayers];
        playerOnTheOutputSideOfStartLine = new boolean[nPlayers];

        for(int i = 0; i< nPlayers; i++){
            playerInContactWithStartLine[i] = false;
            playerOnTheEntrySideOfStartLine[i] = false;
            playerOnTheOutputSideOfStartLine[i] = false;
        }
        lapsPerPlayer = new int[nPlayers];
        for(int i = 0; i< nPlayers; i++){
            lapsPerPlayer[i] = -1;
        }

        // initialise les véhicules de chaque joueurs
        for(int i = 0; i< nPlayers; i++){
            players.get(i).setVehicule(map.getStartPos(), map.getWidthCase());
        }
    }

    public void tick(LinkedList<Integer> pressedKeys){
        avancer(pressedKeys);
        refreshLaps();
    }

    public void avancer(LinkedList<Integer> pressedKeys){
        for(int i = 0; i< nPlayers; i++){
            players.get(i).avancer(pressedKeys, map);
        }
    }

    private void refreshLaps(){
        for(int iPlayer=0; iPlayer<nPlayers; iPlayer++){
            boolean isContact;
            boolean isEntrySide;
            boolean isOutputSide;

            Polygon poly = players.get(iPlayer).getVehicule().getPolygon();
            String startLineType = map.getStartLineType();
            Position startLinePos = map.getStartLinePos();

            isContact = Collision.isColliding(poly, Objet.getPolygon(startLinePos));

            isEntrySide = true;
            isOutputSide = true;
            switch (startLineType){
                case "left":
                    for(int x : poly.xpoints){
                        if (x >= startLinePos.x) {
                            isEntrySide = false;
                        }
                        if (x <= startLinePos.x+startLinePos.width) {
                            isOutputSide = false;
                        }
                    }
                    break;
                case "right":
                    for(int x : poly.xpoints){
                        if (x <= startLinePos.x+startLinePos.width) {
                            isEntrySide = false;
                        }
                        if (x >= startLinePos.x) {
                            isOutputSide = false;
                        }
                    }
                    break;
                case "up":
                    for(int y : poly.ypoints){
                        if (y >= startLinePos.y) {
                            isEntrySide = false;
                        }
                        if (y <= startLinePos.y+startLinePos.height) {
                            isOutputSide = false;
                        }
                    }
                    break;
                case "down":
                    for(int y : poly.ypoints){
                        if (y <= startLinePos.y+startLinePos.height) {
                            isEntrySide = false;
                        }
                        if (y >= startLinePos.y) {
                            isOutputSide = false;
                        }
                    }
                    break;
                default:
                    System.out.println("StartLineType n'est pas connu : " + startLineType);
                    System.out.println("up type choisi à la place");
                    for(int y : poly.ypoints){
                        if (y >= startLinePos.y) {
                            isEntrySide = false;
                        }
                        if (y <= startLinePos.y+startLinePos.height) {
                            isOutputSide = false;
                        }
                    }
            }
            boolean e = playerOnTheEntrySideOfStartLine[iPlayer];
            boolean c = playerInContactWithStartLine[iPlayer];
            boolean o = playerOnTheOutputSideOfStartLine[iPlayer];
            boolean ie = isEntrySide;
            boolean ic = isContact;
            boolean io = isOutputSide;

            // On réalise l'ensemble des calculs avec certaines hypothèses
            boolean newEntrySide = (!e && ie && (!c || o))  ||  (e && !io && !o);
            boolean newOutputSide = (!o && io && (!c || e))  ||  (o && !ie && !e);
            boolean newContact = (c && (ic || (!io && o) || (!ie && e)))  ||  (!c&&ic);

            // Si les hypothèses sont fausses on remet les variables à zéro (normalement il n'y aura jamais de problème)
            if( (ie&&io) || (e&&o) || (!e&&c&!o)){
                System.err.println("Un problème à été détecté, le véhicule doit être mal placé");
                newEntrySide = false;
                newContact = false;
                newOutputSide = false;
            } else {
                // Sinon on peut potentiellement modifier le nombre de tours par joueur
                if(e&&c&&io){
                    lapsPerPlayer[iPlayer] ++;
                }
                if(o&&c&&ie){
                    lapsPerPlayer[iPlayer] --;
                }
            }

            playerOnTheEntrySideOfStartLine[iPlayer] = newEntrySide;
            playerInContactWithStartLine[iPlayer] = newContact;
            playerOnTheOutputSideOfStartLine[iPlayer] = newOutputSide;
        }
    }

    public int isOver(){
        for(int i = 0; i< nPlayers; i++){
            if(lapsPerPlayer[i] == nLaps){
                return i;
            }
        }
        return -1;
    }

    public void dessineMap(Graphics g){
        map.dessine(g);

        if(false){
            // To print the area of the starting line
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(4));
            g2.drawPolygon(Objet.getPolygon(map.getStartLinePos()));
            g2.setStroke(new BasicStroke(1));
        }

    }

    public void dessineJoueurs(Graphics g){
        for(Joueur j: players){
            j.dessine(g);
        }
    }

    public Rectangle getMapSize(){
        return new Rectangle(map.getNbCaseX()*map.getWidthCase(), map.getNbCaseY()*map.getWidthCase());
    }
}