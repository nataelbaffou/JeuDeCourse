import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class LevelEditor extends JPanel implements ActionListener {

    private Dimension s;

    private GridLayout gl;
    private JPanel buttonPane;
    private CustomButton createMap, editMap;
    private JPanel menu;
    private CardLayout c;

    private CoreEditor coreEditor;
    private FenetrePrincipale f;

    public LevelEditor(Dimension size, FenetrePrincipale fenetrePrincipale){
        c = new CardLayout();
        setLayout(c);
        s = size;

        menu = new JPanel();
        menu.setSize(size);
        menu.setLayout(null);
        f = fenetrePrincipale;
        buttonPane = new JPanel();
        createMap = new CustomButton("Create Map");
        createMap.addActionListener(this);
        editMap = new CustomButton("Edit Map");
        editMap.addActionListener(this);

        buttonPane.setSize(new Dimension(size.width/2,size.height));
        gl = new GridLayout(3,0);
        gl.setVgap(10);
        gl.setHgap(10);
        buttonPane.setLayout(gl);
        buttonPane.add(createMap);
        buttonPane.add(editMap);

        int bW = (int)(0.4d*size.width);
        int bY = (int)(0.4d*size.height);
        buttonPane.setBounds((int)((size.width-bW)/2),(int)(size.height/2),bW, bY);
        menu.add(buttonPane);
        add(menu,"menu");
        c.show(this,"menu");
        //j.setPreferredSize(new Dimension(size.width/2,size.height));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==createMap){
            String mapName = JOptionPane.showInputDialog("Please enter the map name");
            System.out.println(mapName);
            coreEditor = new CoreEditor(s,mapName);
        }
        else if(e.getSource() == editMap){
            Set<String> names = IOFiles.listFilesUsingJavaIO("./res/maps");
            String[] possibleValues = new String[names.size()];
            int i = 0;
            for(String n:names){
                possibleValues[i++] = n;
            }
            String selectedValue = (String)JOptionPane.showInputDialog(null,

                    "Select a map to edit", "MapName",

                    JOptionPane.INFORMATION_MESSAGE, null,

                    possibleValues, possibleValues[0]);
            System.out.println(selectedValue);
            coreEditor = new CoreEditor(s, selectedValue);
        }
        add(coreEditor,"editor");
        c.show(this,"editor");
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ((Graphics2D)(g)).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    }
}