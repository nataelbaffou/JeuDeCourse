package Editor;

import LookAndFeel.CustomButton;
import IOEngine.IOFiles;
import Pages.FenetrePrincipale;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class LevelEditor extends JPanel implements MouseListener {

    private Dimension s;

    private GridLayout gl;
    private JPanel buttonPane;
    private CustomButton createMap, editMap, back;
    private JPanel menu;
    private CardLayout c;

    private CoreEditor coreEditor;
    protected FenetrePrincipale f;
    private BufferedImage background;

    public LevelEditor(Dimension size, FenetrePrincipale fenetrePrincipale){
        f = fenetrePrincipale;

        try {
            background = ImageIO.read(new File("res/textures/bg/wpLevelEditor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        c = new CardLayout();
        setLayout(c);
        s = size;

        // Défini la zone où il y a les boutons
        menu = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                int w = background.getWidth();
                int h = background.getHeight();
                float r1 = (float) getWidth() / w;
                float r2 = (float) getHeight() / h;
                float r = Math.max(r1, r2);
                g.drawImage(background, 0, 0, (int) (w * r), (int) (h * r), null);
            }
        };
        menu.setPreferredSize(size);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(0, true));

        // Place les boutons dans cette zone
        buttonPane = new JPanel();
        buttonPane.setPreferredSize(new Dimension(size.width/2,size.height/2));
        buttonPane.setMaximumSize(new Dimension(size.width/2, size.height/2));
        buttonPane.setBackground(new Color(230, 230, 230, 120));
        gl = new GridLayout(3,0);
        gl.setVgap(30);
        buttonPane.setLayout(gl);

        createMap = new CustomButton("Create Map");
        createMap.addMouseListener(this);
        editMap = new CustomButton("Edit Map");
        editMap.addMouseListener(this);
        back = new CustomButton("Back to menu");
        back.addMouseListener(this);

        buttonPane.add(createMap);
        buttonPane.add(editMap);
        buttonPane.add(back);

        menu.add(Box.createVerticalGlue());
        menu.add(buttonPane);
        menu.add(Box.createVerticalGlue());

        add(menu,"menu");
        c.show(this,"menu");
    }

    public CardLayout getC() {
        return c;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ((Graphics2D)(g)).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == back){
            f.getPanelSelection().show(f.getCardContent(), "menu");
        }
        else {
            if (e.getSource() == createMap) {
                coreEditor = new CoreEditor(s, "", this);
            }
            //TODO Dans Editor.CoreEditor Check si le mapName existe déjà et si oui, load la map
            else if (e.getSource() == editMap) {
                Set<String> names = IOFiles.listFilesUsingJavaIO("./res/maps");
                String[] possibleValues = new String[names.size()];
                int i = 0;
                for (String n : names) {
                    possibleValues[i++] = n;
                }
                String selectedValue = (String) JOptionPane.showInputDialog(null,

                        "Select a map to edit", "MapName",

                        JOptionPane.INFORMATION_MESSAGE, null,

                        possibleValues, possibleValues[0]);
                // Si on appuie sur annuler on quitte la fonction
                if(selectedValue == null){
                    return;
                }
                coreEditor = new CoreEditor(s, selectedValue, this);
            }
            add(coreEditor, "editor");
            c.show(this, "editor");
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