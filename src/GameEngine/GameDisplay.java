package GameEngine;

import GameObjects.Joueur;
import GameObjects.Position;
import LookAndFeel.DesignFont;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;

public class GameDisplay extends JPanel{

    private Game game;
    private Rectangle rBandeau;
    private Rectangle rGame;
    private Rectangle rMap;
    private Dimension size;

    public GameDisplay(Game game){
        this.game = game;
    }

    public void initGame(ArrayList<Joueur> joueurs, String idGame, int w, int h){
        double bandeauRatio = 0.2;
        size = new Dimension(w, h);
        rBandeau = new Rectangle(0, 0, (int)(w*bandeauRatio), h);
        rGame = new Rectangle((int)(w*bandeauRatio), 0, w-(int)(w*bandeauRatio), h);
        game.initGame(joueurs, idGame, rGame.width, rGame.height);
        rMap = game.getMapSize();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        paintBandeau(g);
        paintGame(g);
    }

    private void paintGame(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(rGame.x, rGame.y, rGame.width, rGame.height);
        ((Graphics2D) g).setTransform(AffineTransform.getTranslateInstance(rGame.x+0.5*(rGame.width-rMap.width), rGame.y+0.5*(rGame.height-rMap.height)));
        game.dessineMap(g);
        game.dessineJoueurs(g);
        ((Graphics2D) g).setTransform(AffineTransform.getTranslateInstance(0, 0));
    }

    private void paintBandeau(Graphics g){
        Font titleFont = DesignFont.getTitleFont();
        g.setColor(Color.lightGray);
        g.fillRect(rBandeau.x, rBandeau.y, rBandeau.width, rBandeau.height);

        // ############ General informations ###########
        double heightGeneralRatio = 0.25;
        Rectangle rGene = new Rectangle(0, 0, rBandeau.width, (int)(rBandeau.height*heightGeneralRatio));
        g.setColor(Color.white);
        g.fillRect(rGene.x, rGene.y, rGene.width, rGene.height);
        g.setColor(Color.black);

        // Title
        Rectangle pTitle = getRectangle(rGene, 0.9, 0.4, 0.05, 0);
        String text = game.getTitle();
        g.setFont(scaleFontToFit(text, pTitle.width, pTitle.height, g, titleFont));
        g.drawString(text, pTitle.x, pTitle.y+g.getFontMetrics().getHeight());

        // Time elapsed
        Rectangle pTime = getRectangle(rGene, 0.9, 0.25, 0.05, 0);
        pTime.y += pTitle.y + pTitle.height;
        g.setFont(titleFont.deriveFont((float)pTime.height));
        g.drawString(game.getTimeElapsed(), pTime.x, pTime.y+pTime.height);

        // ############ Info for each player ###########
        // get all infos
        ArrayList<Joueur> js = game.getPlayers();
        HashMap<String, Integer> laps = game.getLaps();
        int maxLaps = game.getLapsGoal();
        HashMap<String, String> timePerLap = game.getMinTimePerLap();

        // calcul for printing
        Rectangle rJoueurs = new Rectangle(rBandeau.x, rGene.y+rGene.height, rBandeau.width, rBandeau.height-rGene.height);
        int nJs = js.size();
        float dy = (float)rJoueurs.height/nJs;
        for(int iJ=0; iJ<nJs; iJ++){
            // Get Info / player
            Joueur j = js.get(iJ);
            String nom = j.getNom();
            Color c = j.getColor();
            Rectangle rJ = getRectangle(rJoueurs, 1., 1./nJs, 0, 0);
            rJ.y += (int)(iJ*dy);
            // background
            g.setColor(c.brighter());
            g.fillRect(rJ.x, rJ.y, rJ.width, rJ.height);
            // name
            g.setColor(c.darker());
            Rectangle rName = getRectangle(rJ, 0.9, 0.3, 0.05, 0);
            g.setFont(scaleFontToFit(nom, rName.width, rName.height, g, titleFont));
            g.drawString(nom, rName.x, rName.y + g.getFontMetrics().getHeight());
            // laps
            Rectangle rLaps = getRectangle(rJ, 0.9, 0.2, 0.05, 0);
            rLaps.y += rName.height;
            String l = String.valueOf(laps.get(nom));
            if(l.equals("-1")){l="_";}
            String txtLaps = "laps : " + l + " / " + maxLaps;
            g.setFont(scaleFontToFit(txtLaps, rLaps.width, rLaps.height, g, titleFont));
            g.drawString(txtLaps, rLaps.x, rLaps.y+g.getFontMetrics().getHeight());
        }
    }

    private Rectangle getRectangle(Rectangle r, double wFact, double hFact, double dxFact, double dyFact){
        return new Rectangle((int)(r.x + dxFact*r.width), (int)(r.y + dyFact*r.height), (int)(r.width*wFact), (int)(r.height*hFact));
    }

    public static Font scaleFontToFit(String text, int width, int height, Graphics g, Font pFont)
    {
        float fontSize = pFont.getSize();
        float fWidth = g.getFontMetrics(pFont).stringWidth(text);
        fontSize = ((float)width / fWidth) * fontSize;
        if(fontSize > height){
            return pFont.deriveFont((float)height);
        }
        return pFont.deriveFont(fontSize);
    }

    public void showCountdown(){
        // TODO Make CountDown
    }


    public void dessine(){
        repaint();
    }

}
