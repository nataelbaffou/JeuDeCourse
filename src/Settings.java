import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Settings extends JPanel implements ItemListener, MouseListener, KeyListener {

    private FenetrePrincipale f;
    private Binds soloBind;
    private JComboBox<String> musicMenuBox;
    private JComboBox<String> musicRaceBox;
    private JCheckBox randomMusic;
    private JSlider musicControl;
    private JLabel trollLabel;
    private JButton back;
    private int trollState = 0;

    private boolean testingSong = false;

    public Settings(Dimension size, FenetrePrincipale fenetrePrincipale){
        setSize(size);
        setLayout(null);

        f = fenetrePrincipale;

        JLabel title = new JLabel("SETTINGS");
        title.setBounds(200, 100, 150, 50);
        try {
            title.setFont(Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/fonts/memphis5.ttf")).deriveFont(25F));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        title.setForeground(Color.BLACK);
        add(title);

        // *********************
        // **** Music PANEL ****
        // *********************

        JPanel musicPanel = new JPanel();
        musicPanel.setLayout(null);
        musicPanel.setBounds(200, 150, 600, 300);
        TitledBorder musicBorderTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Musique");
        musicBorderTitle.setTitleJustification(TitledBorder.CENTER);
        musicPanel.setBorder(musicBorderTitle);
        add(musicPanel);

        JLabel musicMenuLabel = new JLabel("Musique du menu : ");
        JLabel musicRaceLabel = new JLabel("Musique de la course : ");

        musicMenuLabel.setBounds(20, 30, 250, 30);
        musicRaceLabel.setBounds(20, 80, 250, 30);
        musicPanel.add(musicMenuLabel);
        musicPanel.add(musicRaceLabel);

        musicMenuBox = new JComboBox<>();
        musicRaceBox = new JComboBox<>();

        ArrayList<String> musics = Audio.getMusics();

        for(String s : musics){
            if(!s.startsWith("troll/")){
                musicMenuBox.addItem(s);
                musicRaceBox.addItem(s);
            }
        }
        musicRaceBox.setSelectedItem("star-realms/nova.wav");
        musicMenuBox.setSelectedItem("star-realms/outer-fields.wav");

        musicMenuBox.addItemListener(this);
        musicRaceBox.addItemListener(this);

        musicMenuBox.setBounds(220, 30, 250, 30);
        musicRaceBox.setBounds(220, 80, 250, 30);
        musicPanel.add(musicMenuBox);
        musicPanel.add(musicRaceBox);

        JLabel randomMusicLabel = new JLabel("Musique aléatoire : ");
        randomMusicLabel.setBounds(20, 130, 250, 30);
        musicPanel.add(randomMusicLabel);

        randomMusic = new JCheckBox();
        randomMusic.setBounds(220, 130, 250, 30);
        randomMusic.setSelected(false);
        randomMusic.addMouseListener(this);
        musicPanel.add(randomMusic);

        musicControl = new JSlider(0, 100);
        musicControl.setBounds(220, 180, 250, 30);
        musicControl.addChangeListener((ChangeEvent e) -> {
            f.getMusiqueFond().setVolume(musicControl.getValue());
        });
        musicControl.setValue(40);
        musicPanel.add(musicControl);

        // ***********************
        // **** OTHER THINGS *****
        // ***********************

        trollLabel = new JLabel("CLICK HERE !", JLabel.CENTER);
        trollLabel.setBounds(0, getSize().height-50, getWidth(), 50);
        trollLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        trollLabel.addMouseListener(this);
        add(trollLabel);

        back = new JButton("Back to menu");
        back.setBounds(100, 600, 200, 30);
        back.addMouseListener(this);
        add(back);

        addMouseListener(this);

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            testingSong = false;
            if(e.getSource() == musicMenuBox){
                f.getMusiqueFond().playMusic(musicMenuBox.getSelectedItem().toString());
            } else if(e.getSource() == musicRaceBox){
                f.getMusiqueFond().playMusic(musicRaceBox.getSelectedItem().toString());
                testingSong = true;
            }
        }

    }

    public JComboBox<String> getMusicMenuBox() {
        return musicMenuBox;
    }

    public JComboBox<String> getMusicRaceBox() {
        return musicRaceBox;
    }

    public boolean isMusicRandom(){
        return randomMusic.isSelected();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if(testingSong){
            f.getMusiqueFond().playTheme("menu");
            testingSong = false;
        }
        if(e.getSource()==back) {
            f.getPanelSelection().show(f.getCardContent(), "menu");
        }else if(e.getSource()==trollLabel){
            switch (trollState){
                case 0:
                    trollLabel.setText("you like the music ? click here to stop");
                    f.getMusiqueFond().playTheme("troll");
                    trollState = 1;
                    break;
                case 1:
                    trollLabel.setText("oh no it doesn't work. Type the name of the red player !");
                    trollState = 2;
                    break;
                case 10:
                    trollLabel.setText("Cool it is over ^^ click here if you want :)");
                    f.getMusiqueFond().stopTroll();
                    f.getMusiqueFond().playTheme("menu");
                    trollState = 0;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (trollState){
            case 2:
                if(e.getKeyChar()=='f'){trollState=3;}
                break;
            case 3:
                if(e.getKeyChar()=='r'){trollState=4;}
                else{trollState=2;}
                break;
            case 4:
                if(e.getKeyChar()=='e'){trollState=5;}
                else{trollState=2;}
                break;
            case 5:
                if(e.getKeyChar()=='d'){
                    trollState=6;
                    trollLabel.setText("Youhouuu but keep going ! Pourquoi je parle anglais dis donc ?");
                    break;
                }
                else{trollState=2;}
            case 6:
                if(e.getKeyChar()=='d'){trollState=7;}
                break;
            case 7:
                if(e.getKeyChar()=='o'){trollState=8;}
                else{trollState=6;}
                break;
            case 8:
                if(e.getKeyChar()=='n'){trollState=9;}
                else{trollState=6;}
                break;
            case 9:
                if(e.getKeyChar()=='c'){
                    trollState=10;
                    trollLabel.setText("C'est presque fini ! Click to stop this f**king music");
                }
                else{trollState=6;}
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
