package GameEngine;

import GameObjects.Joueur;
import GameObjects.Map;
import GameObjects.Objet;
import GameObjects.Position;
import IOEngine.IOFiles;
import Pages.FenetrePrincipale;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
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

    private Instant initTime;
    private String elapsed = "00:00";

    private ArrayList<Instant> initLap;
    private ArrayList<String> currentLap;
    private ArrayList<String> minTPL;

    private boolean countDown = false;
    private Instant initCountdown;

    public Game(FenetrePrincipale fenetrePrincipale){
        f = fenetrePrincipale;
        map = new Map();
    }

    public void initGame(ArrayList<Joueur> j, String idMap, int w, int h){
        players = j;
        Hashtable<String, String> dico = IOFiles.getInformation("maps/", idMap);
        for(java.util.Map.Entry<String, String> param : dico.entrySet()){
            switch (param.getKey()){
                case "filename":
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

        // itialisation du compteur du temps passé
        initTime = Instant.now();

        initLap = new ArrayList<>(nPlayers);
        currentLap = new ArrayList<>(nPlayers);
        minTPL = new ArrayList<>(nPlayers);

        for(int iPlayer = 0; iPlayer < nPlayers; iPlayer++){
            initLap.add(null);
            currentLap.add( "--:--");
            minTPL.add( "--:--");
        }
    }

    public void tick(LinkedList<Integer> pressedKeys){
        if(!countDown) {
            avancer(pressedKeys);
            refreshLaps();
            refreshTimes();
        }
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
                    // Les conditions ont été remplies le joueur à passé un tour
                    lapsPerPlayer[iPlayer] ++;
                    if(initLap.get(iPlayer)!=null){
                        String lastTPL = getTime(initLap.get(iPlayer), Instant.now());
                        if(minTPL.get(iPlayer).equals("--:--") || minTPL.get(iPlayer).compareTo(lastTPL) > 0){
                            minTPL.set(iPlayer, lastTPL);
                        }
                    }
                    initLap.set(iPlayer, Instant.now());
                }
                if(o&&c&&ie){
                    // Les conditions inverses ont été remplies le joueur est passé dans l'autre sens
                    lapsPerPlayer[iPlayer] --;
                    initLap.set(iPlayer, null);
                }
            }

            playerOnTheEntrySideOfStartLine[iPlayer] = newEntrySide;
            playerInContactWithStartLine[iPlayer] = newContact;
            playerOnTheOutputSideOfStartLine[iPlayer] = newOutputSide;
        }
    }

    private void refreshTimes(){
        // Time elapsed
        elapsed = getTime(initTime, Instant.now());

        // Current laps
        Instant now = Instant.now();
        for(int iPlayer = 0; iPlayer < nPlayers; iPlayer++){
            if(initLap.get(iPlayer) == null){
                currentLap.set(iPlayer, "--:--");
            }else {
                currentLap.set(iPlayer, getTime(initLap.get(iPlayer), now));
            }
        }
    }

    private String getTime(Instant init, Instant end){
        Duration timeLeft = Duration.ofMillis(ChronoUnit.MILLIS.between(init, end));
        String time = String.format("%02d:%02d",
                timeLeft.toMinutesPart(), timeLeft.toSecondsPart());
        return time;
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

    public String getTitle() {
        return title;
    }

    public String getTimeElapsed(){
        return elapsed;
    }

    public HashMap<String, String> getCurrentTimePerLap(){
        HashMap<String, String> h = new HashMap<>();
        for(int iPlayer = 0; iPlayer < nPlayers; iPlayer++){
            h.put(players.get(iPlayer).getNom(), currentLap.get(iPlayer));
        }
        return h;
    }

    public HashMap<String, String> getMinTimePerLap(){
        HashMap<String, String> h = new HashMap<>();
        for(int iPlayer = 0; iPlayer < nPlayers; iPlayer++){
            h.put(players.get(iPlayer).getNom(), minTPL.get(iPlayer));
        }
        return h;
    }

    public HashMap<String, Integer> getLaps(){
        HashMap<String, Integer> h = new HashMap<>();
        for(int iPlayer = 0; iPlayer < nPlayers; iPlayer++){
            h.put(players.get(iPlayer).getNom(), lapsPerPlayer[iPlayer]);
        }
        return h;
    }

    public int getLapsGoal(){
        return nLaps;
    }

    public ArrayList<Joueur> getPlayers() {
        return players;
    }

    public void startCountDown(){
        countDown = true;
        initCountdown = Instant.now();
    }

    public boolean isCountDown() {
        return countDown;
    }

    public void setCountDown(boolean countDown) {
        this.countDown = countDown;
    }

    public Instant getInitCountdown() {
        return initCountdown;
    }
}
