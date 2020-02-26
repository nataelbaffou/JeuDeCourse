import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel implements ActionListener {
    private CustomButton solo;
    private CustomButton multi;
    private CustomButton editor;
    private CustomButton settings;
    public MainMenu(int width, int height){

        setPreferredSize(new Dimension(width, height));

        solo = new CustomButton("Solo");
        multi = new CustomButton("Multi");
        editor = new CustomButton("Level Editor");
        settings = new CustomButton("Settings");

        setLayout(new GridLayout(4,1));

        add(solo);
        add(multi);
        add(editor);
        add(settings);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getSource().toString()){
            case "solo":{}
            case "multi":{}
            case "editor":{}
            case "settings":{}
        }
    }
}
