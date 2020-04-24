import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;

public class CoreEditor extends JPanel implements ActionListener, MouseListener{

    JPanel settingsPanel;
    PaintPanel paintPanel;
    JScrollPane scrollPaintPanel;

    String mapName;
    JSlider caseSizeSlider;
    JTextField caseWidth;
    JTextField nbrCaseWidth;
    JTextField nbrCaseHeight;
    JLabel themeLabel;
    JComboBox<String> theme;

    JButton change;
    JButton clear;
    JButton save;
    JButton back;

    JPanel bPanel;
    GridLayout bLayout;
    ImageButton[] textures;

    LevelEditor levelEditor;

    public CoreEditor(Dimension s, String name, LevelEditor l){
        levelEditor = l;
        //TODO Gérer la place des éléments dans les settings/tools
        //TODO Gérer la partie edit map
        Dimension d = new Dimension(20,20);
        mapName = name;
        int[][] grid = null;
        if(mapName != ""){
            // Loading map
            Hashtable<String,String> dico = IOFiles.getInformation("maps",name);
            d = new Dimension(Integer.parseInt(dico.get("size").split(" ")[0]),Integer.parseInt(dico.get("size").split(" ")[1]));
            grid = stringToGrid(dico.get("board"),d);
        }


        Hashtable<String, String> mapTexturesTable = IOFiles.getInformation("textures", "map_conversion_textures");
        mapTexturesTable.remove("filename");

        setBackground(Color.lightGray);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));


        settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel,BoxLayout.PAGE_AXIS));

        textures = new ImageButton[mapTexturesTable.size()];

        bPanel = new JPanel();

        caseSizeSlider = new JSlider(3, 100);
        caseSizeSlider.addChangeListener((ChangeEvent e) -> {
            try{
                paintPanel.setCaseSize(caseSizeSlider.getValue());
                scrollPaintPanel.repaint();
                scrollPaintPanel.revalidate();
            } catch (Exception except){
                // Do nothing this is just for
            }

        });
        caseSizeSlider.setValue(40);
        settingsPanel.add(caseSizeSlider);

        JLabel nbrTileWidth = new JLabel("Largeur (en case) : ");
        settingsPanel.add(nbrTileWidth);

        nbrCaseWidth = new JTextField(d.width+"");
        nbrCaseWidth.setColumns(2);
        nbrCaseWidth.setMaximumSize(new Dimension(100,20));
        /*
        nbrCaseWidth.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
                System.out.println("1");

            }
            public void removeUpdate(DocumentEvent e) {
                //warn();
            }
            public void insertUpdate(DocumentEvent e) {
                //warn();
            }

            public void warn() {
                if (Integer.parseInt(nbrCaseWidth.getText())<=5){
                    JOptionPane.showMessageDialog(null,
                            "Error: Please enter number bigger than 5", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

         */
        settingsPanel.add(nbrCaseWidth);

        JLabel nbrTileHeight = new JLabel("Hauteur (en case) : ");
        settingsPanel.add(nbrTileHeight);
        nbrCaseHeight = new JTextField(d.height+"");
        nbrCaseHeight.setColumns(2);
        nbrCaseHeight.setMaximumSize(new Dimension(100,20));
        /*
        nbrCaseHeight.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();

            }
            public void removeUpdate(DocumentEvent e) {
                //warn();
            }
            public void insertUpdate(DocumentEvent e) {
                //warn();
            }

            public void warn() {
                if (Integer.parseInt(nbrCaseHeight.getText())<=5){
                    JOptionPane.showMessageDialog(null,
                            "Error: Please enter number bigger than 5", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

         */

        settingsPanel.add(nbrCaseHeight);
        change = new JButton("Apply changes");
        change.addActionListener(this);
        settingsPanel.add(change);

        bLayout = new GridLayout((int)Math.sqrt(mapTexturesTable.size())+1,(int)Math.sqrt(mapTexturesTable.size()),20,20);
        bPanel.setLayout(bLayout);

        for(String key: mapTexturesTable.keySet()){
            textures[Integer.parseInt(key)] = new ImageButton(key,"./res/textures/"+mapTexturesTable.get(key), new Dimension(50,50));
            //textures[i].repaint();
            textures[Integer.parseInt(key)].addActionListener(this);
            bPanel.add(textures[Integer.parseInt(key)]);
        }

        settingsPanel.add(bPanel);

        themeLabel = new JLabel("Theme");
        settingsPanel.add(themeLabel);

        theme = new JComboBox<String>();
        theme.addItem("classic");
        theme.addItem("neon");
        theme.setSelectedIndex(0);
        theme.addActionListener(this);
        settingsPanel.add(theme);


        clear = new JButton("Clear map");
        clear.addActionListener(this);
        settingsPanel.add(clear);

        save = new JButton("Save map");
        save.addActionListener(this);
        settingsPanel.add(save);

        back = new JButton("Back");
        back.addActionListener(this);

        settingsPanel.add(back);
        //settingsPanel.setPreferredSize(new Dimension((int)(0.2d*s.width),s.height));
        //paintPanel.setLocation((int)(0.2d*s.width),0);
        add(settingsPanel);

        paintPanel = new PaintPanel(new Dimension((int)(s.width*0.66), s.height), d, caseSizeSlider.getValue(), mapTexturesTable, grid);

        scrollPaintPanel = new JScrollPane(paintPanel);
        scrollPaintPanel.setFocusable(true);
        scrollPaintPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaintPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPaintPanel);

        //TODO Améliorer l'affichage graphique
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == save){
            save();
        }
        else if(e.getSource() == theme)
            paintPanel.setDefaultNum(theme.getSelectedIndex() == 0 ? 1:3);
        else if(e.getSource() == clear)
            paintPanel.clear();
        else if(e.getSource()== back)
            levelEditor.getC().show(levelEditor, "menu");
        else if(e.getSource() == change){
            int w = Integer.parseInt(nbrCaseWidth.getText());
            int h = Integer.parseInt(nbrCaseHeight.getText());
            if(!(paintPanel.getGrid().length == h && paintPanel.getGrid()[0].length == w)) {
                paintPanel.setCaseHeight(h);
                paintPanel.setCaseWidth(w);
                scrollPaintPanel.repaint();
            }
        }

        for(ImageButton b:textures){
            if(e.getSource() == b){
                paintPanel.setTexture(Integer.parseInt(b.getText()));
            }
        }
    }

    public void save(){
        try {
            Hashtable<String, String> data = new Hashtable<>();
            String name = JOptionPane.showInputDialog("Nom de la map");
            String path = "maps"; //TODO Possibilité de choisir le sous-dossier (mais impossibilité de modifier les maps enregistrées dans maps/campaign)
            //TODO Vérifier que la map n'existe pas déjà + Vérification à chaque niveau que les données rentrées sont conformes
            data.put("title", JOptionPane.showInputDialog("Description de la map"));
            data.put("size", nbrCaseWidth.getText() + " " + nbrCaseHeight.getText());
            data.put("board", formatBoard(paintPanel.getGrid()));
            data.put("start-line", getStartLine(paintPanel.getGrid()));
            data.put("start-position", getStartPosition());
            data.put("laps", JOptionPane.showInputDialog("Nombre de tour à faire"));

            IOFiles.setInformation(data, path, name);

            levelEditor.getC().show(levelEditor, "menu");
        }catch (Exception e){
            JOptionPane.showMessageDialog(this,"Erreur :"+e);
        }
    }

    public String getStartLine(int[][] board) throws Exception {
        String res = "";
        res += JOptionPane.showInputDialog(null,"Franchissement de la ligne par quel côté ?","Side",
                JOptionPane.INFORMATION_MESSAGE, null , new String[]{"up","down","left","right"},"left") + "\n";


        // Detection de la plus grande surface recouverte par les cases départ
        int cmin = -1, cmax = -1, lmin = -1, lmax = -1;
        for(int i = 0;i<board.length;i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == 0) {
                    if(i>lmax){
                        lmax = i;
                    }
                    if(lmin == -1 || j < lmin){
                        lmin = i;
                    }
                    if(j>cmax){
                        cmax = j;
                    }
                    if(cmin == -1 || j < cmin){
                        cmin = j;
                    }
                }
            }
        }

        // Vérification qu'il n'y a que des cases départ sur cette zone
        for(int i = lmin; i <= lmax;i++){
            for(int j = cmin; j <= cmax; j++){
                if(board[i][j]!=0){
                    throw new Exception("La ligne de départ créée n'est pas rectangulaire");
                }
            }
        }

        HashSet<String> startCoord = new HashSet<>();
        startCoord.add(lmin + " " + cmin);
        startCoord.add(lmax + " " + cmax);
        if(startCoord.size()==0)
            throw new Exception("No starting-line detected");
        for(String co: startCoord)
            res += co+"\n";
        res = res.substring(0,res.length()-1);
        return res;
    }

    public String getStartPosition(){
        String res = "";
        res += JOptionPane.showInputDialog("Case de départ : ligne") +" ";
        res += JOptionPane.showInputDialog("Case de départ : colonne") + "\n";
        res += JOptionPane.showInputDialog(null,"Orientation de la voiture au départ","Orientation",
                JOptionPane.INFORMATION_MESSAGE, null , new String[]{"up","down","left","right"},"right");
        return res;
    }

    public String formatBoard(int[][] board){
        String res = "";
        for(int[] row:board){
            for(int num:row){
                res += num + " ";
            }
            res = res.substring(0,res.length()-1);
            res += "\n";
        }
        res = res.substring(0,res.length()-1);
        System.out.println(res);
        return res;
    }

    public int[][] stringToGrid(String board,Dimension d){
        int[][] res = new int[(int)d.getHeight()][(int)d.getWidth()];

        for(int i = 0;i<d.getHeight();i++){
            for(int j = 0; j<d.getWidth();j++){
                res[i][j] = Integer.parseInt(board.split("\n")[i].split(" ")[j]);
            }
        }

        return res;
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

class PaintPanel extends JPanel implements MouseListener, MouseMotionListener{
    //TODO Bug lors de l'enregistrement de la map : soit les cases sont mal modifiées ici, soit la grid est mal convertie au dessus (inversion des coordonnées)
    private String startOrientation;
    private boolean repaintRect;
    private int[][] grid;
    private Dimension visibleSize;
    private Dimension realSize;
    private int caseSize;
    private double probBlack;
    private int currentTexture;
    private BufferedImage img;
    private BufferedImage[] textures;
    private File f;
    private int defaultNum;

    public PaintPanel(Dimension size, Dimension n, int caseSize, Hashtable<String, String> map,int[][] board){
        //img = new BufferedImage(n,n,BufferedImage.TYPE_BYTE_BINARY);
        this.visibleSize = size;
        this.caseSize = caseSize;
        this.realSize = new Dimension((int)(caseSize*n.getWidth()), (int)(caseSize*n.getHeight()));

        defaultNum = 1;

        textures = new BufferedImage[map.size()];

        for(String key: map.keySet()){
            try {
                textures[Integer.parseInt(key)] =  ImageIO.read(new File("./res/textures/"+map.get(key)));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        if(board != null){
            grid = board.clone();
        }
        else {
            grid = new int[(int) n.getHeight()][(int) n.getWidth()];
            for (int i = 0; i < n.getHeight(); i++) {
                for (int j = 0; j < n.getWidth(); j++) {
                    grid[i][j] = defaultNum;
                }
            }
        }

        setPreferredSize(visibleSize);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);

        setKeyBindings();

        probBlack = 0.2;
        repaintRect = false;
    }

    public void setDefaultNum(int i){
        defaultNum =i;
    }

    public int[][] getGrid(){return grid;}

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition);
        String vkR = "VK_R";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), vkR);

        actionMap.put(vkR, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomize();
                repaint();
            }
        });
    }

    public void setCaseHeight(int num){
        grid = new int[num][grid[0].length];
        for(int i = 0;i<num;i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = defaultNum;
            }
        }
        realSize.setSize(realSize.width, caseSize*num);
        repaint();
    }
    public void setCaseWidth(int num){
        grid = new int[grid.length][num];
        for(int i = 0;i<grid.length;i++){
            for(int j = 0; j < num; j++){
                grid[i][j] = defaultNum;
            }
        }
        realSize.setSize(caseSize*num, realSize.height);
        repaint();
    }

    public void setCaseSize(int num){
        realSize.setSize(realSize.width*num/caseSize, realSize.height*num/caseSize);
        caseSize = num;
        repaint();
    }

    public void clear(){
        for(int i = 0; i<grid.length; i++){
            for(int j = 0; j<grid[0].length;j++){
                grid[i][j] = defaultNum;
            }
        }
        repaint();
    }
    public void randomize(){
        for(int i = 0; i<grid.length; i++){
            for(int j = 0; j<grid[0].length;j++){
                grid[i][j] = Math.random() < probBlack ? defaultNum+1 : defaultNum;
            }
        }
    }

    public void save(){



        //TODO Save L'aperçu de la map
        /*
        for(int i = 0; i<grid.length; i++){
            for(int j = 0; j<grid[0].length;j++){
                img.setRGB(i,j,new Color(grid[i][j]-1).getRGB());
            }
        }

         */
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(Color.white);

        Rectangle r = new Rectangle();

        if(repaintRect){
            r = g.getClipBounds();
            g.drawImage(textures[currentTexture],(int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight(),null);
            repaintRect = false;
        }
        else{
            paintGrid(g);
        }
    }

    public void paintGrid(Graphics g){
        for(int i = 0;i<grid.length;i++){
            for(int j = 0; j < grid[0].length; j++){
                g.drawImage(textures[grid[i][j]],j*caseSize,i*caseSize, caseSize,caseSize,null);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return realSize;
    }


    public void setTexture(int nbr){
        currentTexture = nbr;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int a = e.getX()/caseSize;
        int b = e.getY()/caseSize;
        if(0 <= b && b < grid.length && 0 <= a && a < grid[0].length){
            grid[b][a] = currentTexture;
            repaintRect = true;
            repaint(a*caseSize,b*caseSize,caseSize,caseSize);
        }
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
        int a = e.getX() / caseSize;
        int b = e.getY() / caseSize;
        if (0 <= b && b < grid.length && 0 <= a && a < grid[0].length) {
            grid[b][a] = currentTexture;
            repaintRect = true;
            repaint(a * caseSize, b * caseSize, caseSize, caseSize);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
