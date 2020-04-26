package Pages;

import IOEngine.Audio;
import IOEngine.Binds;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private BufferedImage background;

    private boolean testingSong = false;

    public Settings(Dimension size, FenetrePrincipale fenetrePrincipale){
        setPreferredSize(size);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try {
            background = ImageIO.read(new File("res/textures/wallpaperSettings2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        f = fenetrePrincipale;

        Color bgColor = new Color(200, 200, 200, 120);
        Border bgBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        Border spaceBorder = BorderFactory.createEmptyBorder(10, 20, 10, 20);
        Color transp = new Color(0, true);

        // TITLE
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(transp);

        JLabel title = new JLabel(" SETTINGS ");
        try {
            title.setFont(Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/fonts/memphis5.ttf")).deriveFont(25F));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        title.setForeground(Color.BLACK);
        title.setOpaque(true);
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setBorder(new EmptyBorder(20, 30, 20, 30));
        title.setBackground(bgColor);
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        // *********************
        // **** Music PANEL ****
        // *********************

        // Panel
        JPanel musicPanel = new JPanel();
        musicPanel.setLayout(new GridLayout(4, 2));
        musicPanel.setPreferredSize(new Dimension(600, 300));
        musicPanel.setMaximumSize(new Dimension(600, 300));
        //TitledBorder musicBorderTitle = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Musique");
        //musicBorderTitle.setTitleJustification(TitledBorder.CENTER);
        musicPanel.setBorder(bgBorder);
        musicPanel.setBackground(bgColor);

        // MENU ET RACE
        JLabel musicMenuLabel = new JLabel("Musique du menu : ");
        JLabel musicRaceLabel = new JLabel("Musique de la course : ");
        musicMenuLabel.setBorder(spaceBorder);
        musicRaceLabel.setBorder(spaceBorder);
        musicMenuBox = new JComboBox<>();
        musicRaceBox = new JComboBox<>();
        musicMenuBox.setBorder(spaceBorder);
        musicRaceBox.setBorder(spaceBorder);
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
        musicMenuBox.addMouseListener(this);
        musicRaceBox.addMouseListener(this);
        musicPanel.add(musicMenuLabel);
        musicPanel.add(musicMenuBox);
        musicPanel.add(musicRaceLabel);
        musicPanel.add(musicRaceBox);

        // RANDOM
        JLabel randomMusicLabel = new JLabel("Musique aléatoire : ");
        randomMusicLabel.setBorder(spaceBorder);
        randomMusic = new JCheckBox();
        randomMusic.setBorder(spaceBorder);
        randomMusic.setSelected(false);
        randomMusic.addMouseListener(this);
        musicPanel.add(randomMusicLabel);
        musicPanel.add(randomMusic);

        // VOLUME
        JLabel musicControlLabel = new JLabel("Volume : ");
        musicControlLabel.setBorder(spaceBorder);
        musicControl = new JSlider(0, 100);
        musicControl.setBorder(spaceBorder);
        musicControl.addChangeListener((ChangeEvent e) -> {
            f.getMusiqueFond().setVolume(musicControl.getValue());
            repaint();
        });
        musicControl.setValue(40);
        musicPanel.add(musicControlLabel);
        musicPanel.add(musicControl);

        JPanel musicPanelCentered = new JPanel();
        musicPanelCentered.setLayout(new BoxLayout(musicPanelCentered, BoxLayout.X_AXIS));
        musicPanelCentered.setBackground(transp);
        musicPanelCentered.add(Box.createHorizontalGlue());
        musicPanelCentered.add(musicPanel);
        musicPanelCentered.add(Box.createHorizontalGlue());

        // ***********************
        // **** OTHER THINGS *****
        // ***********************

        // Back button
        JPanel backPanel = new JPanel();
        backPanel.setBackground(transp);
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        back = new JButton("Back to menu"){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(musicPanel.getWidth()/2, 30);
            }
        };
        back.addMouseListener(this);
        backPanel.add(Box.createHorizontalGlue());
        backPanel.add(back);
        backPanel.add(Box.createHorizontalGlue());

        //Troll
        trollLabel = new JLabel("CLICK HERE !", JLabel.CENTER);
        trollLabel.setPreferredSize(new Dimension((int)size.getWidth(), 50));
        trollLabel.addMouseListener(this);

        JPanel trollLabelCentered = new JPanel();
        trollLabelCentered.setLayout(new BoxLayout(trollLabelCentered, BoxLayout.X_AXIS));
        trollLabelCentered.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        trollLabelCentered.setBackground(bgColor);
        trollLabelCentered.add(Box.createHorizontalGlue());
        trollLabelCentered.add(trollLabel);
        trollLabelCentered.add(Box.createHorizontalGlue());

        //****** PAGE ******
        add(Box.createVerticalGlue());
        add(titlePanel);
        add(Box.createVerticalGlue());
        add(musicPanelCentered);
        add(backPanel);
        add(Box.createVerticalGlue());
        add(trollLabelCentered);


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
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int w = background.getWidth();
        int h = background.getHeight();
        float r1 = (float) getWidth() / w;
        float r2 = (float) getHeight() / h;
        float r = Math.max(r1, r2);
        g.drawImage(background, 0, 0, (int) (w * r), (int) (h * r), null);
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
                case 11:
                    trollLabel.setText("Cool it is over ^^ click here if you want :)");
                    f.getMusiqueFond().stopTroll();
                    f.getMusiqueFond().playTheme("menu");
                    trollState = 0;
            }
        }
        repaint();
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
                    trollLabel.setText("Youhouuu but keep going ! Repeat 'after me'");
                    break;
                }
                else{trollState=2;}
            case 6:
                if(e.getKeyChar()=='a'){trollState=7;}
                break;
            case 7:
                if(e.getKeyChar()=='f'){trollState=8;}
                else{trollState=6;}
                break;
            case 8:
                if(e.getKeyChar()=='t'){trollState=9;}
                else{trollState=6;}
                break;
            case 9:
                if(e.getKeyChar()=='e'){trollState=10;}
                else{trollState=6;}
                break;
            case 10:
                if(e.getKeyChar()=='r'){
                    JOptionPane.showOptionDialog(this,"Would you like to continue ?", "A damn question", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,  new String[]{"Yes", "No", "Maybe"}, null);
                    trollState=11;
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
