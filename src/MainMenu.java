import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel implements ActionListener {
    private CustomButton solo;
    private CustomButton multi;
    private CustomButton editor;
    private CustomButton settings;
    private JPanel bPanel;
    private FenetrePrincipale f;
    public MainMenu(int width, int height, FenetrePrincipale fenetrePrincipale){

        f = fenetrePrincipale;

        setPreferredSize(new Dimension(width, height));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        bPanel = new JPanel();
        GridLayout gl = new GridLayout(4,0);
        gl.setVgap(30);
        bPanel.setLayout(gl);
        bPanel.setPreferredSize(new Dimension(width/2, height/2));
        bPanel.setMaximumSize(new Dimension(width/2, height/2));

        solo = new CustomButton("Solo");
        solo.addActionListener(this);
        solo.setFocusPainted(false);
        multi = new CustomButton("Multi");
        multi.addActionListener(this);
        multi.setFocusPainted(false);
        editor = new CustomButton("Level Editor");
        editor.addActionListener(this);
        editor.setFocusPainted(false);
        settings = new CustomButton("Settings");
        settings.addActionListener(this);
        settings.setFocusPainted(false);

        bPanel.add(solo);
        bPanel.add(multi);
        bPanel.add(editor);
        bPanel.add(settings);

        add(Box.createVerticalGlue());
        add(bPanel);
        add(Box.createVerticalGlue());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==solo) {
            f.getGameContent().setPlayers();
            f.getPanelSelection().show(f.getCardContent(),"levelSelector");
        }
        else if(e.getSource()==editor){
            f.getPanelSelection().show(f.getCardContent(),"editor");
        }
        else if(e.getSource()==multi){
            f.getPanelSelection().show(f.getCardContent(), "playersSelector");
        }
        else if(e.getSource()==settings){
            f.getPanelSelection().show(f.getCardContent(), "settings");
        }
    }
}
