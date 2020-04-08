import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;

public class PlayersSelector extends JPanel implements MouseListener, KeyListener {
    //Puis amener vers level selector

    private FenetrePrincipale f;
    private JButton validate;
    private JButton back;
    private JPanel[] panels;
    private JLabel[] names;
    private JLabel[][] binds;
    private int[][] bindsCode;

    private Color[] colors = {Color.RED, Color.GREEN, Color.PINK, Color.BLUE, Color.ORANGE, Color.GRAY};
    private Color[] colorsMat;

    private boolean waitingKey = false;
    private int playerNo;
    private int currentBind;


    public PlayersSelector(Dimension size, FenetrePrincipale fenetrePrincipale){
        setPreferredSize(size);
        setLayout(null);

        f = fenetrePrincipale;

        // Definition des couleurs
        colorsMat = new Color[6];
        for(int i = 0; i < 6; i++){
            colorsMat[i] = new Color(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue(), 60);
        }

        // Génération des titres
        JPanel titlePanel = new JPanel();
        JLabel username = new JLabel("name", 10);

        username.setBounds(20, 0, 150, 50);
        username.setForeground(Color.BLACK);
        try {
            username.setFont(Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/fonts/memphis5.ttf")).deriveFont(25F));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        titlePanel.setLayout(null);
        titlePanel.add(username);

        JLabel[] titleBind = new JLabel[4];
        for(int iBind=0; iBind<4; iBind++){
            titleBind[iBind] = new JLabel(new String[]{"Up", "Down", "Left", "Right"}[iBind]);
            titleBind[iBind].setBounds(150+(iBind+1)*80, 0, 80, 60);
            titleBind[iBind].setForeground(Color.BLACK);
            titlePanel.add(titleBind[iBind]);
        }

        titlePanel.setBounds(150, 40, 550, 50);
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(titlePanel);


        // Génération des 6 joueurs et binds
        panels = new JPanel[6];

        names = new JLabel[6];
        String[] strNames = {"Fred", "Greenlee", "Pinkney", "Bluebell", "Willem", "Greydon"};
        for(int i=0;i<6;i++){
            names[i] = new JLabel(strNames[i], 10);
            names[i].setBounds(20, 0, 150, 50);
            names[i].setForeground(colorsMat[i]);
            try {
                names[i].setFont(Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/fonts/memphis5.ttf")).deriveFont(25F));
            } catch (FontFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            names[i].addMouseListener(this);
            panels[i] = new JPanel();
            panels[i].setLayout(null);
            panels[i].add(names[i]);
        }

        binds = new JLabel[6][4];
        bindsCode = new int[6][4];
        for(int iPlayer=0; iPlayer<6; iPlayer++){
            for(int iBind=0; iBind<4; iBind++){
                bindsCode[iPlayer][iBind] = -1;
                binds[iPlayer][iBind] = new JLabel();
                binds[iPlayer][iBind].setBounds(150+(iBind+1)*80, 0, 80, 60);
                binds[iPlayer][iBind].setForeground(colorsMat[iPlayer]);
                panels[iPlayer].add(binds[iPlayer][iBind]);
            }
        }

        for(int i=0; i<6; i++){
            panels[i].setBounds(150, 100+60*i, 550, 50);
            panels[i].setBorder(BorderFactory.createLineBorder(colorsMat[i], 3));
            add(panels[i]);
        }

        validate = new JButton("Validate");
        validate.setBounds(400, 500, 70, 30);
        validate.addMouseListener(this);

        back = new JButton("Back to menu");
        back.setBounds(100, 600, 200, 30);
        back.addMouseListener(this);

        add(validate);
        add(back);
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
        panels[i].setBorder(BorderFactory.createLineBorder(c, 3));
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

    private void setBind(int c){
        binds[playerNo][currentBind].setText(KeyEvent.getKeyText(c));
        bindsCode[playerNo][currentBind] = c;
        currentBind++;
        if(currentBind==4){
            waitingKey = false;
        }
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
        if(waitingKey){
            setBind(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}