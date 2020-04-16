import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CoreEditor extends JPanel implements ActionListener, MouseListener{

    JPanel settingsPanel;
    PaintPanel paintPanel;

    JTextField mapName;
    JTextField nbrCaseWidth;
    JTextField nbrCaseHeight;

    JButton save;

    JPanel bPanel;
    GridLayout bLayout;
    ImageButton[] textures;

    public CoreEditor(Dimension s, String name){
        //TODO Gérer la place des éléments dans les settings/tools
        //TODO Refondre la grid du PaintPanel pour sauvegarder le type des tiles dans le tableau
        //TODO Gérer la partie edit map
        //TODO faire le save Map
        setBackground(Color.lightGray);
        setLayout(null);

        System.out.println(s.getHeight());

        settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel,BoxLayout.PAGE_AXIS));
        paintPanel = new PaintPanel((int)(s.getHeight()),20);

        String[] names = Arrays.copyOf(IOFiles.listFilesUsingJavaIO("./res/textures/tiles").toArray(), IOFiles.listFilesUsingJavaIO("./res/textures/tiles").toArray().length, String[].class);
        for(String n:names){
            System.out.print(" "+n+" ");
        }
        textures = new ImageButton[names.length];

        bPanel = new JPanel();

        JLabel nbrTileWidth = new JLabel("Largeur (en case) : ");
        settingsPanel.add(nbrTileWidth);
        nbrCaseWidth = new JTextField("20");
        nbrCaseWidth.setColumns(2);
        nbrCaseWidth.setMaximumSize(new Dimension(100,20));
        nbrCaseWidth.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (Integer.parseInt(nbrCaseWidth.getText())<=5){
                    JOptionPane.showMessageDialog(null,
                            "Error: Please enter number bigger than 5", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        settingsPanel.add(nbrCaseWidth);

        JLabel nbrTileHeight = new JLabel("Hauteur (en case) : ");
        settingsPanel.add(nbrTileHeight);
        nbrCaseHeight = new JTextField("20");
        nbrCaseHeight.setColumns(2);
        nbrCaseHeight.setMaximumSize(new Dimension(100,20));
        nbrCaseHeight.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (Integer.parseInt(nbrCaseHeight.getText())<=5){
                    JOptionPane.showMessageDialog(null,
                            "Error: Please enter number bigger than 5", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        settingsPanel.add(nbrCaseHeight);

        bLayout = new GridLayout((int)Math.sqrt(names.length)+1,(int)Math.sqrt(names.length),20,20);
        bPanel.setLayout(bLayout);

        for(int i = 0; i< textures.length;i++){
            textures[i] = new ImageButton(names[i],"./res/textures/tiles/"+names[i], new Dimension(50,50));
            //textures[i].repaint();
            textures[i].addActionListener(this);
            bPanel.add(textures[i]);
        }

        paintPanel.setTexture(textures[0].getImageBG());

        settingsPanel.add(bPanel);
        settingsPanel.setBounds(0,0,(int)(0.2d*s.width),s.height);
        paintPanel.setLocation((int)(0.2d*s.width),0);
        add(settingsPanel);
        add(paintPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(ImageButton b:textures){
            if(e.getSource() == b){
                paintPanel.setTexture(b.getImageBG());
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
}

class PaintPanel extends JPanel implements MouseListener, MouseMotionListener {

    private boolean repaintRect;
    private boolean pGrid;
    private int[][] grid;
    private int size;
    private int initialSize;
    private int caseSize;
    private double probBlack;
    private Color tileColor;
    private BufferedImage img;
    private BufferedImage texture;
    private File f;
    private int a,b;

    public PaintPanel(int size, int n){
        img = new BufferedImage(n,n,BufferedImage.TYPE_BYTE_BINARY);
        this.size = size;
        this.initialSize = size;
        grid = new int[n][n];
        for(int i = 0;i<n;i++){
            for(int j = 0; j < n; j++){
                grid[i][j] = 0;
            }
        }
        tileColor = Color.black;
        caseSize = size/n;
        if(size != caseSize*n){
            size = caseSize*n;
        }
        setSize(new Dimension(size,size));
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);

        setKeyBindings();

        probBlack = 0.2;
        pGrid = false;

    }

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition);

        String vkS = "VK_S";
        String vkR = "VK_R";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), vkS);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), vkR);

        actionMap.put(vkS, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println('s');
                save();
                f = new File("Image.png");
                try {
                    ImageIO.write(img, "PNG", f);
                }catch(IOException i){
                    System.out.println(i);
                }
            }
        });
        actionMap.put(vkR, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println('r');
                randomize();
                repaint();
            }
        });
    }

    public void changeSize(int n){
        img = new BufferedImage(n,n,BufferedImage.TYPE_BYTE_BINARY);
        grid = new int[n][n];
        for(int i = 0;i<n;i++){
            for(int j = 0; j < n; j++){
                grid[i][j] = 0;
            }
        }
        caseSize = initialSize/n;
        if(size != caseSize*n){
            size = caseSize*n;
        }
        setPreferredSize(new Dimension(size,size));
        repaint();
    }

    public void randomize(){
        for(int i = 0; i<grid.length; i++){
            for(int j = 0; j<grid[0].length;j++){
                grid[i][j] = Math.random() < probBlack ? 1 : 0;
            }
        }
        pGrid = true;
    }

    public void save(){
        for(int i = 0; i<grid.length; i++){
            for(int j = 0; j<grid[0].length;j++){
                img.setRGB(i,j,new Color(grid[i][j]-1).getRGB());
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(Color.white);

        Rectangle r = new Rectangle();

        if(repaintRect){
            r = g.getClipBounds();
            g.drawImage(texture,(int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight(),null);
            repaintRect = false;
        }
        else if(pGrid){
            paintGrid(g);
            pGrid = false;
        }
        /*
        if(repaintRect){
            r = g.getClipBounds();
            g.setColor(tileColor);
            g.fillRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
            repaintRect = false;
        }
        else if(pGrid){
            paintGrid(g);
            pGrid = false;
        }

         */
    }

    public void paintGrid(Graphics g){
        for(int i = 0;i<grid.length;i++){
            for(int j = 0; j < grid[0].length; j++){

                if(grid[i][j] == 1)
                    g.drawImage(texture,i*caseSize,j*caseSize, caseSize,caseSize,null);
                else {
                    g.setColor(Color.white);
                    g.fillRect(i * caseSize, j * caseSize, caseSize, caseSize);
                }
            }
        }
    }

    public void paintTile(Graphics g, int i, int j ){

        if(grid[i][j] == 1) {
            g.setColor(Color.black);
        }
        else{
            g.setColor(Color.white);
        }


        g.fillRect(i*caseSize,j*caseSize,caseSize,caseSize);
    }

    public int constrainInRange(int v, int min, int max){
        if(v< min)
            return min;
        else if(v>max)
            return max;
        return v;
    }

    public void setTexture(BufferedImage text){
        texture = text;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        a = e.getX()/caseSize;
        b = e.getY()/caseSize;
        grid[a][b] ++;
        grid[a][b] %= 2;
        repaintRect = true;
        repaint(a*caseSize,b*caseSize,caseSize,caseSize);
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
    public void mouseDragged(MouseEvent e) {
        int i, j;
        i = constrainInRange(e.getX()/caseSize,0,this.getWidth()/caseSize-1);
        j = constrainInRange(e.getY()/caseSize,0,this.getHeight()/caseSize-1);
        if(a != i || b != j) {
            a = i;
            b = j;
            grid[a][b]++;
            grid[a][b] %= 2;
            repaintRect = true;
            repaint(a * caseSize, b * caseSize, caseSize, caseSize);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
