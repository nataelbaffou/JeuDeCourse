import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Set;

public class LevelSelector extends JPanel implements MouseListener, AdjustmentListener {
    private ArrayList<CustomButton> levels;
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
                return new Dimension((int)(0.8*size.width), (buttonHeight+spaceBetweenButton)*(nButton + 1));
            }
        };
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
        buttonPane.setPreferredSize(new Dimension(size.width, size.height));


        levels = new ArrayList<>(nButton);

        for(String mapName : maps){
            CustomButton cb = new CustomButton(mapName);
            cb.setPreferredSize(new Dimension((int)(0.8*size.width), (int)(0.3*size.height)));
            cb.setMaximumSize(new Dimension((int)(0.8*size.width), (int)(0.3*size.height)));
            cb.addMouseListener(this);
            // Pour centrer les boutons
            JPanel jp = new JPanel();
            jp.setBorder(new EmptyBorder(spaceBetweenButton/2, 0, spaceBetweenButton/2, 0));
            jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
            jp.add(Box.createHorizontalGlue());
            jp.add(cb);
            jp.add(Box.createHorizontalGlue());
            levels.add(cb);
            buttonPane.add(jp);
        }

        JScrollPane buttonScrollPane = new JScrollPane(buttonPane);
        buttonScrollPane.setFocusable(true);
        buttonScrollPane.setPreferredSize(size);
        buttonScrollPane.setAutoscrolls(true);
        buttonScrollPane.setColumnHeader(null);
        buttonScrollPane.setRowHeader(null);
        buttonScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        buttonScrollPane.getHorizontalScrollBar().addAdjustmentListener(this);
        buttonScrollPane.getVerticalScrollBar().addAdjustmentListener(this);

        add(buttonScrollPane);
    }

    public void update(){
        // TODO update list of button when charging
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(new GradientPaint(0, 0, Color.RED, 0, 0.4f*getHeight(), new Color(1f, 1f, 1f, 0f)));
        g2.fillRect(0, 0, getWidth(), getHeight()/2);
        g2.setPaint(new GradientPaint(0, getHeight(), Color.RED, 0, 0.6f*getHeight(), new Color(1f, 1f, 1f, 0f)));
        g2.fillRect(0, getHeight()/2, getWidth(), getHeight());
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for(CustomButton button : levels){
            if(e.getSource() == button){
                f.getGameContent().setGame(button.getText());
                f.getGameContent().launchGame();
                f.getPanelSelection().show(f.getCardContent(),"game");
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
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        repaint();
    }
}