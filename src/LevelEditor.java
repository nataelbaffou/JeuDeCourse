import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class LevelEditor extends JPanel implements ActionListener {

    private Dimension s;

    private GridLayout gl;
    private JPanel buttonPane;
    private CustomButton createMap, editMap, back;
    private JPanel menu;
    private CardLayout c;

    private CoreEditor coreEditor;
    private FenetrePrincipale f;

    public LevelEditor(Dimension size, FenetrePrincipale fenetrePrincipale){
        f = fenetrePrincipale;
        c = new CardLayout();
        setLayout(c);
        s = size;

        // Défini la zone où il y a les boutons
        menu = new JPanel();
        menu.setPreferredSize(size);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        // Place les boutons dans cette zone
        buttonPane = new JPanel();
        buttonPane.setPreferredSize(new Dimension(size.width/2,size.height/2));
        buttonPane.setMaximumSize(new Dimension(size.width/2, size.height/2));
        gl = new GridLayout(3,0);
        gl.setVgap(30);
        buttonPane.setLayout(gl);

        createMap = new CustomButton("Create Map");
        createMap.addActionListener(this);
        editMap = new CustomButton("Edit Map");
        editMap.addActionListener(this);
        back = new CustomButton("Back to menu");
        back.addActionListener(this);

        buttonPane.add(createMap);
        buttonPane.add(editMap);
        buttonPane.add(back);

        menu.add(Box.createVerticalGlue());
        menu.add(buttonPane);
        menu.add(Box.createVerticalGlue());

        add(menu,"menu");
        c.show(this,"menu");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back){
            f.getPanelSelection().show(f.getCardContent(), "menu");
        }
        else {
            if (e.getSource() == createMap) {
                coreEditor = new CoreEditor(s, "", this);
            }
            //TODO Dans CoreEditor Check si le mapName existe déjà et si oui, load la map
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

    public CardLayout getC() {
        return c;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ((Graphics2D)(g)).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    }
}