package Pages;

import LookAndFeel.DesignFont;
import LookAndFeel.CustomButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

public class PlayersSelector extends JPanel implements MouseListener, KeyListener {

    private FenetrePrincipale f;
    private CustomButton validate;
    private CustomButton back;
    private JPanel[] panels;
    private JLabel[] names;
    private JLabel[][] binds;
    private int[][] bindsCode;

    private Color[] colors = {Color.RED, Color.GREEN, new Color(183, 0, 255), Color.BLUE, new Color(255, 115, 0), Color.YELLOW};
    private Color[] colorsMat;

    private boolean waitingKey = false;
    private int playerNo;
    private int currentBind;
    private BufferedImage background;

    private static Border spaceBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    private static final HashMap<Integer, String> VK_FIELDS = new HashMap<Integer, String>();

    static {
        Field[] fields = KeyEvent.class.getFields();
        for (Field f : fields) {
            if (f.getName().startsWith("VK_") && f.getType()==Integer.TYPE) {
                try {
                    VK_FIELDS.put(f.getInt(null), f.getName().substring(3));
                } catch (IllegalAccessException ex) {
                    //ignore
                }
            }
        }
    }


    public PlayersSelector(Dimension size, FenetrePrincipale fenetrePrincipale){
        setPreferredSize(size);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        f = fenetrePrincipale;

        try {
            background = ImageIO.read(new File("res/textures/bg/wp8.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Color bgColor = new Color(255, 255, 255, 140);
        Border bgBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        Color transp = new Color(0, true);

        // Definition des couleurs
        colorsMat = new Color[6];
        for(int i = 0; i < 6; i++){
            colorsMat[i] = new Color(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue(), 120);
            //colorsMat[i] = colors[i].darker();
        }


        JPanel choicePanel = new JPanel();
        choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.Y_AXIS));
        choicePanel.setBackground(bgColor);

        Border nameBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border bindBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        // Génération des titres
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(1, 6));
        titlePanel.setBackground(transp);
        JLabel username = new JLabel("name");
        username.setBorder(nameBorder);
        username.setForeground(Color.BLACK);
        username.setFont(DesignFont.getTitleFont());

        titlePanel.add(username);
        titlePanel.add(Box.createGlue());

        JLabel[] titleBind = new JLabel[4];
        for(int iBind=0; iBind<4; iBind++){
            titleBind[iBind] = new JLabel(new String[]{"Up", "Down", "Left", "Right"}[iBind]);
            titleBind[iBind].setBorder(bindBorder);
            titleBind[iBind].setForeground(Color.BLACK);
            titlePanel.add(titleBind[iBind]);
        }
        titlePanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, BorderFactory.createLineBorder(Color.BLACK, 4)));
        choicePanel.add(titlePanel);


        // Génération des 6 joueurs et binds
        panels = new JPanel[6];

        names = new JLabel[6];
        String[] strNames = {"Fred", "Greenlee", "Purploo", "Bluebell", "Willem", "Yellan"};
        for(int i=0;i<6;i++){
            names[i] = new JLabel(strNames[i]);
            names[i].setForeground(colorsMat[i]);
            names[i].setBorder(nameBorder);
            names[i].setFont(DesignFont.getBindsFont());
            names[i].addMouseListener(this);

            panels[i] = new JPanel();
            panels[i].setLayout(new GridLayout(1, 6));
            panels[i].setBackground(transp);
            panels[i].add(names[i]);
            panels[i].add(Box.createGlue());
        }

        binds = new JLabel[6][4];
        bindsCode = new int[6][4];
        for(int iPlayer=0; iPlayer<6; iPlayer++){
            for(int iBind=0; iBind<4; iBind++){
                bindsCode[iPlayer][iBind] = -1;
                binds[iPlayer][iBind] = new JLabel();
                binds[iPlayer][iBind].setForeground(colorsMat[iPlayer]);
                binds[iPlayer][iBind].setBorder(bindBorder);
                binds[iPlayer][iBind].setFont(DesignFont.getBindsFont());
                panels[iPlayer].add(binds[iPlayer][iBind]);
            }
        }

        for(int i=0; i<6; i++){
            Border coloredLineBorder = BorderFactory.createLineBorder(colorsMat[i], 4);
            panels[i].setBorder(BorderFactory.createCompoundBorder(spaceBorder, coloredLineBorder));
            choicePanel.add(panels[i]);
        }

        validate = new CustomButton("Validate", bgColor){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(choicePanel.getWidth()/2, 40);
            }
        };
        validate.setRoundBorder(false);
        validate.setBorder(bgBorder);
        validate.addMouseListener(this);

        JPanel validatePanel = new JPanel();
        validatePanel.setLayout(new BoxLayout(validatePanel, BoxLayout.X_AXIS));
        validatePanel.setBackground(transp);
        validatePanel.add(Box.createHorizontalGlue());
        validatePanel.add(validate);
        validatePanel.add(Box.createHorizontalGlue());

        back = new CustomButton("Back to menu", bgColor){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(choicePanel.getWidth()/2, 40);
            }
        };
        back.setRoundBorder(false);
        back.setBorder(bgBorder);
        back.addMouseListener(this);

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        backPanel.setBackground(transp);
        backPanel.add(Box.createHorizontalGlue());
        backPanel.add(back);
        backPanel.add(Box.createHorizontalGlue());

        JPanel globalPanel = new JPanel();
        globalPanel.setLayout(new BoxLayout(globalPanel, BoxLayout.Y_AXIS));
        globalPanel.setBackground(transp);
        globalPanel.add(Box.createVerticalGlue());
        globalPanel.add(Box.createVerticalGlue());
        globalPanel.add(choicePanel);
        globalPanel.add(Box.createVerticalGlue());
        globalPanel.add(validatePanel);
        globalPanel.add(Box.createVerticalGlue());
        globalPanel.add(backPanel);
        globalPanel.add(Box.createVerticalGlue());

        setBackground(transp);
        add(Box.createHorizontalGlue());
        add(globalPanel);
        add(Box.createHorizontalGlue());
    }

    private void inverseColor(int i){
        Color c;
        if(names[i].getForeground().getAlpha()==colors[i].getAlpha()){
            c = colorsMat[i];
            resetBind(i);
        }
        else{
            c = colors[i];
            initSetBind(i);
        }
        names[i].setForeground(c);
        Border coloredLineBorder = BorderFactory.createLineBorder(c, 4);
        panels[i].setBorder(BorderFactory.createCompoundBorder(spaceBorder, coloredLineBorder));
        for(JLabel b : binds[i]){
            b.setForeground(c);
        }
    }

    private void resetBind(int iPlayer){
        for(int iBind = 0; iBind<4; iBind++){
            binds[iPlayer][iBind].setText("");
            bindsCode[iPlayer][iBind] = -1;
        }
    }

    private void initSetBind(int i){
        waitingKey = true;
        playerNo = i;
        currentBind = 0;
    }

    private void setBind(KeyEvent k){
        System.out.println(getKeyText(k));
        binds[playerNo][currentBind].setText(getKeyText(k));
        bindsCode[playerNo][currentBind] = k.getKeyCode();
        currentBind++;
        if(currentBind==4){
            waitingKey = false;
        }
        repaint();
    }

    private int getNumberOfPlayers(){
        int n = 0;
        for(int[] b : bindsCode){
            if(b[0] != -1){
                n += 1;
            }
        }
        return n;
    }

    public static String getKeyText(KeyEvent evt) {
        char chr = evt.getKeyChar();
        String chrstr = String.valueOf(chr);
        int code = evt.getKeyCode();

        String keyField = VK_FIELDS.get(code);
        String fixed = keyField != null ? fixName(keyField) : KeyEvent.getKeyText(code);
        return ((chr==KeyEvent.CHAR_UNDEFINED || chr < 32) ? fixed : chrstr).toUpperCase();
    }

    private static String fixName(String input) {
        char[] ar = new char[input.length()];
        char last = ' ';
        for (int i=0; i<ar.length; i++) {
            char c = input.charAt(i);
            if (c=='_')
                c = ' ';
            ar[i] = Character.isSpaceChar(last) ? Character.toTitleCase(c) : Character.toLowerCase(c);
            last = c;
        }
        return new String(ar);
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
        if(!waitingKey){
            for(int i = 0; i<6;i++){
                if(e.getSource() == names[i]){
                    inverseColor(i);
                }
            }
            if(e.getSource()==validate && getNumberOfPlayers()>=1){
                f.getGameContent().setPlayers(bindsCode);
                f.getPanelSelection().show(f.getCardContent(), "levelSelector");
            }else if(e.getSource()==back){
                f.getPanelSelection().show(f.getCardContent(), "menu");
            }
        }
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
        if(waitingKey){
            setBind(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}