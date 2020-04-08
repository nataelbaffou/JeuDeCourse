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

        bPanel = new JPanel();
        int bW = (int)(0.4d*width);
        int bY = (int)(0.4d*height);
        bPanel.setBounds((int)((width-bW)/2),(int)(height/2),bW, bY);

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

        GridLayout gl = new GridLayout(4,1);
        gl.setVgap(10);
        bPanel.setLayout(gl);

        bPanel.add(solo);
        bPanel.add(multi);
        bPanel.add(editor);
        bPanel.add(settings);

        setLayout(null);
        add(bPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==solo) {
            f.getGameContent().setPlayers();
            f.getPanelSelection().show(f.getCardContent(),"levelSelector");
        }
        else if(e.getSource()==multi){
            f.getPanelSelection().show(f.getCardContent(), "playersSelector");
        }
        else if(e.getSource()==editor){}
        else if(e.getSource()==settings){
            f.getPanelSelection().show(f.getCardContent(), "settings");
        }
    }
}
