package Pages;

import IOEngine.IOFiles;
import LookAndFeel.LevelButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LevelSelector extends JPanel implements MouseListener, AdjustmentListener {
    private ArrayList<LevelButton> levels;
    private FenetrePrincipale f;
    private JScrollPane buttonScrollPane;
    private BufferedImage background;

    public LevelSelector(Dimension size, FenetrePrincipale fenetrePrincipale){
        f = fenetrePrincipale;
        setPreferredSize(size);

        try {
            background = ImageIO.read(new File("res/textures/bg/wpMapSelector.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        charge(size);
    }

    public void charge(Dimension s){
        removeAll();


        // On récupère les maps et on ajoute "Back to menu"
        ArrayList<String> maps = new ArrayList<>(IOFiles.listFilesUsingJavaIO("res/maps"));
        Collections.sort(maps);
        maps.add("Back to menu");

        // Calculs préliminaires et stockage des données utiles
        Dimension size;
        if(s == null){
            size = getSize();
        }else{
            size = (Dimension)s.clone();
        }
        int buttonHeight = (int)(0.3*size.height);
        int spaceBetweenButton = 30;
        int nButton = maps.size();

        // Panel qui stock l'ensemble des boutons
        JPanel buttonPane = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension((int)(0.8*size.width), (buttonHeight+spaceBetweenButton)*(nButton + 2));
            }
        };
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
        buttonPane.setBackground(new Color(0, true));

        buttonPane.add(Box.createVerticalGlue());

        levels = new ArrayList<>(nButton);

        // On créé l'ensembles des boutons
        for(String mapName : maps){
            LevelButton lb = new LevelButton(mapName);
            lb.setPreferredSize(new Dimension((int)(0.8*size.width), (int)(0.3*size.height)));
            lb.setMaximumSize(new Dimension((int)(0.8*size.width), (int)(0.3*size.height)));
            lb.addMouseListener(this);
            // Pour centrer les boutons
            JPanel jp = new JPanel();
            jp.setBorder(new EmptyBorder(spaceBetweenButton/2, 0, spaceBetweenButton/2, 0));
            jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
            jp.add(Box.createHorizontalGlue());
            jp.add(lb);
            jp.add(Box.createHorizontalGlue());
            jp.setBackground(new Color(0, true));
            levels.add(lb);
            buttonPane.add(jp);
        }

        buttonPane.add(Box.createVerticalGlue());

        // On ajoute la scrollbar
        buttonScrollPane = new JScrollPane(buttonPane);
        buttonScrollPane.setFocusable(true);
        buttonScrollPane.setPreferredSize(size);
        buttonScrollPane.setAutoscrolls(true);
        buttonScrollPane.setColumnHeader(null);
        buttonScrollPane.setRowHeader(null);
        buttonScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        buttonScrollPane.getVerticalScrollBar().addAdjustmentListener(this);
        buttonScrollPane.getVerticalScrollBar().setBackground(new Color(0, true));
        buttonScrollPane.setBackground(new Color(0, true));

        add(buttonScrollPane);
        repaint();
    }

    public void paint(Graphics g){
        Color uppercolor = new Color(200, 30, 215, 255);
        Color lowercolor = new Color(200, 30, 215, 255);
        Color transp = new Color(0, true);

        super.paint(g);

        Graphics2D g2 = (Graphics2D)g;
        float exteriorSpace = 0f;
        float middleSpace = 0.1f;
        g2.setColor(uppercolor);
        g2.fillRect(0,0, getWidth(), (int)(exteriorSpace*getHeight()));
        g2.setPaint(new GradientPaint(0, 0, uppercolor, 0, (0.5f-0.5f*middleSpace)*getHeight(), transp));
        g2.fillRect(0, (int)(exteriorSpace*getHeight()), getWidth(), getHeight()/2);
        g2.setPaint(new GradientPaint(0, getHeight(), lowercolor, 0, (0.5f+0.5f*middleSpace)*getHeight(), transp));
        g2.fillRect(0, getHeight()/2, getWidth(), (int)((1f-exteriorSpace)*(getHeight()/2)));
        g2.setColor(lowercolor);
        g2.fillRect(0, (int)((1f-exteriorSpace)*getHeight()), getWidth(), (int)(exteriorSpace*getHeight()));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        /*  ## COLOR ##
        Color middlecolor = new Color(11, 30, 225, 255);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(middlecolor);
        g2.fillRect(0, 0, getWidth(), getHeight());
         */

        // ## IMAGE ##
        int w = background.getWidth();
        int h = background.getHeight();
        float r1 = (float) getWidth() / w;
        float r2 = (float) getHeight() / h;
        float r = Math.max(r1, r2);
        g.drawImage(background, 0, 0, (int) (w * r), (int) (h * r), null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for(LevelButton button : levels){
            if(e.getSource() == button){
                if(button.getText().equals("Back to menu")){
                    f.getPanelSelection().show(f.getCardContent(), "menu");
                    buttonScrollPane.getVerticalScrollBar().setValue(buttonScrollPane.getVerticalScrollBar().getMinimum());
                }else{
                    f.getGameContent().setGame(button.getText());
                    f.getGameContent().launchGame();
                    f.getPanelSelection().show(f.getCardContent(),"game");
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        repaint();
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        repaint();
    }
}