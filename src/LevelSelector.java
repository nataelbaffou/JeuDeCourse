import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelSelector extends JPanel implements ActionListener {
    private CustomButton levels[];
    private FenetrePrincipale f;
    private GridLayout gl;
    private JPanel buttonPane;

    public LevelSelector(Dimension size, FenetrePrincipale fenetrePrincipale){
        int n = 4;
        
        setPreferredSize(size);
        setLayout(new BorderLayout());

        f = fenetrePrincipale;
        levels = new CustomButton[n];
        buttonPane = new JPanel();

        //buttonPane.setPreferredSize(new Dimension(size.width/2,size.height));

        //TODO : GridLayout créé dynamiquement à partir du nombre de niveaux
        gl = new GridLayout(2,2);
        gl.setVgap(10);
        gl.setHgap(10);
        buttonPane.setLayout(gl);
        setLayout(gl);

        for(int i = 0; i<levels.length; i++){
            levels[i] = new CustomButton("Level "+i);
            levels[i].addActionListener(this);
            levels[i].setFocusPainted(false);
            buttonPane.add(levels[i]);
        }
        add(buttonPane,BorderLayout.PAGE_END);
        JPanel j = new JPanel();
        j.setBackground(Color.red);
        add(j,BorderLayout.PAGE_START);

        //j.setPreferredSize(new Dimension(size.width/2,size.height));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ((Graphics2D)(g)).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i = 0; i<levels.length;i++){
            if(e.getSource() == levels[i]){
                f.getGameContent().setMap(i);
                f.getPanelSelection().show(f.getCardContent(),"game");
            }
        }
    }
}
