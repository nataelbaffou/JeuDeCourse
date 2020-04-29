package Pages;

import LookAndFeel.CustomButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel implements MouseListener {
    private CustomButton solo;
    private CustomButton multi;
    private CustomButton editor;
    private CustomButton settings;
    private CustomButton quit;
    private JPanel bPanel;
    private FenetrePrincipale f;
    private BufferedImage background;

    public MainMenu(int width, int height, FenetrePrincipale fenetrePrincipale) {

        f = fenetrePrincipale;

        setPreferredSize(new Dimension(width, height));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try {
            background = ImageIO.read(new File("res/textures/bg/wpMainMenu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bPanel = new JPanel();
        GridLayout gl = new GridLayout(5, 0);
        gl.setVgap(30);
        bPanel.setLayout(gl);
        bPanel.setPreferredSize(new Dimension(width / 2, height / 2));
        bPanel.setMaximumSize(new Dimension(width / 2, height / 2));
        bPanel.setBackground(new Color(200, 200, 200, 120));

        solo = new CustomButton("Solo");
        solo.addMouseListener(this);
        solo.setFocusPainted(false);
        multi = new CustomButton("Multi");
        multi.addMouseListener(this);
        multi.setFocusPainted(false);
        editor = new CustomButton("Level Editor");
        editor.addMouseListener(this);
        editor.setFocusPainted(false);
        settings = new CustomButton("Settings");
        settings.addMouseListener(this);
        settings.setFocusPainted(false);
        quit = new CustomButton("Quit");
        quit.addMouseListener(this);
        quit.setFocusPainted(false);

        bPanel.add(solo);
        bPanel.add(multi);
        bPanel.add(editor);
        bPanel.add(settings);
        bPanel.add(quit);

        add(Box.createVerticalGlue());
        add(bPanel);
        add(Box.createVerticalGlue());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = background.getWidth();
        int h = background.getHeight();
        float r1 = (float) getWidth() / w;
        float r2 = (float) getHeight() / h;
        float r = Math.max(r1, r2);
        g.drawImage(background, 0, 0, (int) (w * r), (int) (h * r), null);
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if (e.getSource() == solo) {
            f.getGameContent().setPlayers();
            f.getPanelSelection().show(f.getCardContent(), "levelSelector");
        } else if (e.getSource() == editor) {
            f.getPanelSelection().show(f.getCardContent(), "editor");
        } else if (e.getSource() == multi) {
            f.getPanelSelection().show(f.getCardContent(), "playersSelector");
        } else if (e.getSource() == settings) {
            f.getPanelSelection().show(f.getCardContent(), "settings");
        } else if (e.getSource() == quit) {
            System.exit(1);
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
}
