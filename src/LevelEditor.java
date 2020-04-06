import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LevelEditor extends JPanel implements ActionListener {
    private FenetrePrincipale f;
    private GridLayout gl;
    private JPanel buttonPane;
    private CustomButton createMap, editMap;
    private JComboBox mapList;
    private CoreEditor coreEditor;
    private JPanel menu;
    private Dimension s;
    private CardLayout c;

    public LevelEditor(Dimension size, FenetrePrincipale fenetrePrincipale){
        c = new CardLayout();
        setLayout(c);
        s = size;

        menu = new JPanel();
        menu.setPreferredSize(size);
        menu.setLayout(null);

        mapList = new JComboBox();
        Set<String> names = listFilesUsingJavaIO("./res/maps");
        for(String n:names){
            mapList.addItem(n);
        }

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
        buttonPane.add(mapList);
        buttonPane.add(editMap);

        int bW = (int)(0.4d*size.width);
        int bY = (int)(0.4d*size.height);
        buttonPane.setBounds((int)((size.width-bW)/2),(int)(size.height/2),bW, bY);
        menu.add(buttonPane);
        add(menu,"menu");
        c.show(this,"menu");
        //j.setPreferredSize(new Dimension(size.width/2,size.height));
    }

    public Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==createMap){
            coreEditor = new CoreEditor(s,"test");
        }
        else if(e.getSource() == editMap){
            coreEditor = new CoreEditor(s, mapList.getSelectedItem().toString());
        }
        add(coreEditor,"editor");
        c.show(this,"editor");
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ((Graphics2D)(g)).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    }
}
