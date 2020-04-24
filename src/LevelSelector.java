import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class LevelSelector extends JPanel implements ActionListener {
    private CustomButton levels[];
    private FenetrePrincipale f;
    private GridLayout gl;
    private JPanel buttonPane;

    public LevelSelector(Dimension size, FenetrePrincipale fenetrePrincipale){
        f = fenetrePrincipale;
        setPreferredSize(size);

        Set<String> maps = IOFiles.listFilesUsingJavaIO("res/maps");

        int buttonHeight = (int)(0.3*size.height);
        int spaceBetweenButton = 30;
        int nButton = maps.size();

        buttonPane = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension((int)(0.8*size.width), (buttonHeight+spaceBetweenButton)*nButton);
            }
        };
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
        buttonPane.setPreferredSize(new Dimension(size.width, size.height));


        levels = new CustomButton[nButton];

        int idMap = 0;
        for(String mapName : maps){
            levels[idMap] = new CustomButton(mapName);
            levels[idMap].setPreferredSize(new Dimension((int)(0.8*size.width), (int)(0.3*size.height)));
            levels[idMap].setMaximumSize(new Dimension((int)(0.8*size.width), (int)(0.3*size.height)));
            levels[idMap].setBorder(new EmptyBorder(spaceBetweenButton/2, 0, spaceBetweenButton/2, 0));
            levels[idMap].addActionListener(this);
            levels[idMap].setFocusPainted(false);
            // Pour centrer les boutons
            JPanel jp = new JPanel();
            jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
            jp.add(Box.createHorizontalGlue());
            jp.add(levels[idMap]);
            jp.add(Box.createHorizontalGlue());
            buttonPane.add(jp);
            //buttonPane.add(levels[idMap]);
            idMap++;
        }

        JScrollPane buttonScrollPane = new JScrollPane(buttonPane);
        buttonScrollPane.setFocusable(true);
        buttonScrollPane.setPreferredSize(size);
        buttonScrollPane.setColumnHeader(null);
        buttonScrollPane.setRowHeader(null);
        buttonScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(buttonScrollPane);
        JPanel j = new JPanel();
        j.setBackground(Color.yellow);
        add(j);

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
                f.getGameContent().setGame(i);
                f.getGameContent().launchGame();
                f.getPanelSelection().show(f.getCardContent(),"game");
            }
        }
    }
}