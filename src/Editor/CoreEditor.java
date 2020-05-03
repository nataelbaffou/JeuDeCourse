package Editor;

import LookAndFeel.ImageButton;
import IOEngine.IOFiles;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Hashtable;

public class CoreEditor extends JPanel implements ActionListener, MouseListener{

    PaintPanel paintPanel;
    JScrollPane scrollPaintPanel;
    JToolBar toolBar;

    String mapName;
    String desc;
    Point startPos;
    Point[] mapBounds;
    int[][] boardResized;
    double rSettings;
    int caseSize;
    String lap;
    Color toolColor;

    JSlider caseSizeSlider;
    JComboBox<String> theme;

    FlowLayout fl;

    ImageButton clear;
    ImageButton createOv;
    JButton startButton;
    ImageButton boundsButton;
    ImageButton save;
    ImageButton back;

    JPanel bPanelstart;
    JPanel bPanelground;
    JPanel bPanelwall;
    JPanel zoomPanel;
    JPanel themePanel;
    JPanel optionPanel;

    GridLayout bLayout;
    ImageButton[] textures;

    LevelEditor levelEditor;

    boolean isSaving;

    public CoreEditor(Dimension s, String name, LevelEditor l){
        levelEditor = l;
        Dimension d = new Dimension(20,20);
        mapName = name;
        isSaving = false;
        int[][] grid = null;
        if(mapName!=null && !mapName.equals("")){
            // Loading map
            Hashtable<String,String> dico = IOFiles.getInformation("maps",name);
            d = new Dimension(Integer.parseInt(dico.get("size").split(" ")[0]),Integer.parseInt(dico.get("size").split(" ")[1]));
            grid = stringToGrid(dico.get("board"),d);
            desc = dico.get("description");
        }

        rSettings = 0.2;
        toolColor = new Color(255, 255, 255);
        String path = System.getProperty("user.dir")+"/res/textures/";

        Hashtable<String, String> mapTexturesTable = IOFiles.getInformation("textures", "map_conversion_textures");
        mapTexturesTable.remove("filename");

        setBackground(Color.lightGray);
        setLayout(new BorderLayout());


        toolBar = new JToolBar("Menu");
        toolBar.setBackground(toolColor);

        fl = new FlowLayout(FlowLayout.CENTER);
        fl.setHgap(10);
        fl.setVgap(10);


        textures = new ImageButton[mapTexturesTable.size()];

        zoomPanel = new JPanel();
        zoomPanel.setBackground(toolColor);
        zoomPanel.setBorder(BorderFactory.createTitledBorder("Zoom"));
        caseSizeSlider = new JSlider(14, 100);
        caseSizeSlider.setBackground(toolColor);
        caseSizeSlider.addChangeListener((ChangeEvent e) -> {
            try{
                int v = caseSizeSlider.getValue();
                Rectangle r = scrollPaintPanel.getViewport().getViewRect();

                double rx = r.getCenterX()/(100*caseSize);
                double ry = r.getCenterY()/(100*caseSize);

                paintPanel.setCaseSize(v);
                scrollPaintPanel.revalidate();
                scrollPaintPanel.repaint();

                r = scrollPaintPanel.getViewport().getViewRect();

                rx = rx*100*v-r.getWidth()/2d;
                if(rx < 0)
                    rx = 0;
                ry = ry*100*v-r.getHeight()/2d;
                if(ry < 0)
                    ry = 0;
                scrollPaintPanel.getViewport().setViewPosition(new Point((int)(rx),(int)(ry)));

                caseSize = caseSizeSlider.getValue();
            } catch (Exception except){
                // Do nothing this is just for
            }

        });
        Hashtable<Integer, JLabel> zoomLabels = new Hashtable<>();
        zoomLabels.put(14, new JLabel("Out"));
        zoomLabels.put(100, new JLabel("In"));
        caseSizeSlider.setLabelTable(zoomLabels);
        caseSizeSlider.setPaintLabels(true);
        caseSizeSlider.setValue(40);
        caseSize = caseSizeSlider.getValue();
        zoomPanel.add(caseSizeSlider);
        toolBar.add(zoomPanel);

        createButtonPanels(mapTexturesTable);

        themePanel = new JPanel();
        themePanel.setLayout(fl);
        themePanel.setBackground(toolColor);
        themePanel.setBorder(BorderFactory.createTitledBorder("Theme"));
        theme = new JComboBox<String>();
        theme.setBackground(toolColor);
        theme.addItem("classic");
        theme.addItem("neon");
        theme.setSelectedIndex(0);
        theme.addActionListener(this);
        themePanel.add(theme);
        toolBar.add(themePanel);

        optionPanel = new JPanel();
        optionPanel.setBackground(toolColor);
        optionPanel.setLayout(fl);
        optionPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        clear = new ImageButton("clear","Clear Map",path+"clearIcon.png",new Dimension(30,30));
        clear.addActionListener(this);
        optionPanel.add(clear);

        boundsButton = new ImageButton("bounds","Select map bounds",path+"boundsIcon.png",new Dimension(30,30));
        boundsButton.addActionListener(this);
        optionPanel.add(boundsButton);

        startButton = new ImageButton("start","Select start",path+"startIcon.png",new Dimension(30,30));
        startButton.addActionListener(this);
        optionPanel.add(startButton);

        createOv = new ImageButton("overView","Create Overview",path+"screenIcon.png",new Dimension(30,30));
        createOv.addActionListener(this);
        optionPanel.add(createOv);

        save = new ImageButton("save","Save map",path+"saveIcon.png",new Dimension(30,30));
        save.addActionListener(this);
        optionPanel.add(save);

        back = new ImageButton("back","Back",path+"backIcon.png",new Dimension(30,30));
        back.addActionListener(this);
        optionPanel.add(back);
        toolBar.add(optionPanel);
        toolBar.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String propName = evt.getPropertyName();
                if("orientation".equals(propName)){
                    caseSizeSlider.setOrientation(toolBar.getOrientation());
                }
                if(toolBar.getOrientation()==JToolBar.VERTICAL){
                    toolBar.setPreferredSize(new Dimension((int)(0.1*s.width),s.height));
                }
                else{
                    toolBar.setPreferredSize(new Dimension(s.width,(int)(0.1*s.height)));
                }
            }
        });
        add(toolBar,BorderLayout.PAGE_START);

        paintPanel = new PaintPanel(new Dimension(s.width, s.height),caseSizeSlider.getValue(),mapTexturesTable,grid,this);
        scrollPaintPanel = new JScrollPane(paintPanel);
        scrollPaintPanel.setFocusable(true);
        scrollPaintPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaintPanel.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPaintPanel.getHorizontalScrollBar().setValue(scrollPaintPanel.getHorizontalScrollBar().getMaximum()/2);
        scrollPaintPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaintPanel.getVerticalScrollBar().setUnitIncrement(16);
        scrollPaintPanel.getVerticalScrollBar().setValue(scrollPaintPanel.getVerticalScrollBar().getMaximum()/2);

        add(scrollPaintPanel,BorderLayout.CENTER);
    }

    public void createButtonPanels(Hashtable<String, String> map){
        Dimension bSize = new Dimension(20,20);
        Hashtable<String, String> mapTexturesTable = (Hashtable<String, String>) map.clone();
        Hashtable<String, String> mapTexturesSettings = IOFiles.getInformation("textures", "textures_settings");
        mapTexturesSettings.remove("filename");

        bPanelstart = new JPanel();
        bPanelstart.setLayout(fl);
        bPanelstart.setBackground(toolColor);
        bPanelstart.setBorder(BorderFactory.createTitledBorder("Start Tiles"));
        textures[0] = new ImageButton("0",mapTexturesTable.get("0").split("\\.png")[0],"./res/textures/"+mapTexturesTable.get("0"), bSize);
        textures[0].addActionListener(this);
        bPanelstart.add(textures[0]);
        mapTexturesTable.remove("0");

        bPanelground = new JPanel();
        bPanelground.setLayout(fl);
        bPanelground.setBackground(toolColor);
        bPanelground.setBorder(BorderFactory.createTitledBorder("Ground Tiles"));
        for(String key: mapTexturesTable.keySet()){
            if(mapTexturesSettings.get(mapTexturesTable.get(key)).contains("block : false")){
                textures[Integer.parseInt(key)] = new ImageButton(key,mapTexturesTable.get(key).split("\\.png")[0],"./res/textures/"+mapTexturesTable.get(key), bSize);
                textures[Integer.parseInt(key)].addActionListener(this);
                bPanelground.add(textures[Integer.parseInt(key)]);
            }
        }
        bPanelwall = new JPanel();
        bPanelwall.setLayout(fl);
        bPanelwall.setBackground(toolColor);
        bPanelwall.setBorder(BorderFactory.createTitledBorder("Wall Tiles"));

        bLayout = new GridLayout((int)Math.sqrt(mapTexturesTable.size())+1,(int)Math.sqrt(mapTexturesTable.size()),5,5);

        for(String key: mapTexturesTable.keySet()){
            if(mapTexturesSettings.get(mapTexturesTable.get(key)).contains("block : true")) {
                textures[Integer.parseInt(key)] = new ImageButton(key,mapTexturesTable.get(key).split("\\.png")[0], "./res/textures/" + mapTexturesTable.get(key), bSize);
                textures[Integer.parseInt(key)].addActionListener(this);
                bPanelwall.add(textures[Integer.parseInt(key)]);
            }
        }
        toolBar.add(bPanelstart);
        toolBar.add(bPanelground);
        toolBar.add(bPanelwall);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == save){
            save();
        }
        else if(e.getSource() == createOv){
            if(mapName==null || mapName.equals(""))
                mapName = JOptionPane.showInputDialog("Nom de la map");
            paintPanel.save(mapName);
        }
        else if(e.getSource() == theme) {
            paintPanel.setDefaultNum(theme.getSelectedIndex() == 0 ? 1 : 3);
            paintPanel.clear();
        }
        else if(e.getSource() == clear)
            paintPanel.clear();
        else if(e.getSource() == boundsButton){
            paintPanel.selectBounds();
        }
        else if(e.getSource() == startButton){
            if(mapBounds != null){
                paintPanel.selectStart();
            }
            else{
                JOptionPane.showMessageDialog(this,"Veuillez d'abord sélectionner les bords de la map avant la position de départ");
            }
        }
        else if(e.getSource()== back){
            if(toolBar.isFloatable()) {
                toolBar.isVisible();
            }
            levelEditor.getC().show(levelEditor, "menu");
        }

        for(ImageButton b:textures){
            if(e.getSource() == b){
                paintPanel.setTexture(Integer.parseInt(b.getText()));
            }
        }
    }

    public void save(){
        try {
            isSaving=true;
            Hashtable<String, String> data = new Hashtable<>();
            if(mapName==null || mapName.equals("")) {
                mapName = JOptionPane.showInputDialog("Nom de la map").trim();
                while(IOFiles.listFilesUsingJavaIO("./res/maps").contains(mapName) || mapName.equals("")) {
                    if(IOFiles.listFilesUsingJavaIO("./res/maps").contains(mapName))
                        JOptionPane.showMessageDialog(this,"Map " + mapName + " already exist, please choose another name");
                    else
                        JOptionPane.showMessageDialog(this,"Veuillez rentrer un nom valide");
                    mapName = JOptionPane.showInputDialog("Nom de la map");
                }
            }
            if(desc == null)
                desc = JOptionPane.showInputDialog("Description de la map");
            if(mapBounds == null) {
                paintPanel.selectBounds();
                throw new Exception("selectBounds");
            }
            if(startPos == null) {
                paintPanel.selectStart();
                throw new Exception("selectStart");
            }
            if(lap == null){
                lap = JOptionPane.showInputDialog("Nombre de tours à faire").trim();
                try {
                    int l = Integer.parseInt(lap);
                    if(l < 1)
                        throw new Exception("Le nombre de tours doit être au moins égal à 1");
                }
                catch(NumberFormatException nfe){
                    throw new Exception("Le nombre de tours doit être un entier");
                }
                catch(Exception e){
                    throw e;
                }
            }
            String path = "maps"; //TODO Possibilité de choisir le sous-dossier (mais impossibilité de modifier les maps enregistrées dans maps/campaign)
            //TODO Vérifier que la map n'existe pas déjà + Vérification à chaque niveau que les données rentrées sont conformes
            //int[][] boardResized = resizeBoard(paintPanel.getGrid(),mapBounds);
            boardResized = paintPanel.getResizedGrid();
            System.out.println("1");
            data.put("description", desc);
            data.put("size", boardResized[0].length + " " + boardResized.length);
            data.put("board", formatBoard(boardResized));
            data.put("start-line", getStartLine(boardResized));
            data.put("start-position", formatStart(startPos));
            data.put("laps", lap);

            IOFiles.setInformation(data, path, mapName);

            paintPanel.save(mapName);

            JOptionPane.showMessageDialog(this,"Successfully created map and overview of  : "+mapName);

            levelEditor.f.getLevelSelector().charge(null);

            levelEditor.getC().show(levelEditor, "menu");
        }catch (Exception e){
            if(!(e.getMessage().equals("selectBounds") ||e.getMessage().equals("selectStart"))) {
                JOptionPane.showMessageDialog(this, "Erreur :" + e);
            }
            System.out.println(e);
            isSaving = false;
        }
    }

    public void setMapBounds(Point[] bounds){
        mapBounds = bounds;
        JOptionPane.showMessageDialog(this,"Sélection réussie");
        if(isSaving)
            save();
    }

    public void setStart(Point start){
        startPos = start;
        JOptionPane.showMessageDialog(this,"Sélection réussie");
        if(isSaving)
            save();
    }

    public String getStartLine(int[][] board) throws Exception {
        String res = "";
        res += JOptionPane.showInputDialog(null,"Franchissement de la ligne par quel côté ?","Side",
                JOptionPane.INFORMATION_MESSAGE, null , new String[]{"up","down","left","right"},"left") + "\n";

        System.out.println("Test");
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

        for(int i = 0;i<board.length;i++){
            for(int j=0; j<board[0].length;j++){
                System.out.print(board[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println(cmin+" "+cmax+" "+lmax+" "+lmin);
        if(cmin == -1 && cmax == -1 && lmin == -1 && lmax == -1)
            throw new Exception("Il n'y a pas de ligne de départ");

        // Vérification qu'il n'y a que des cases départ sur cette zone
        for(int i = lmin; i <= lmax;i++){
            for(int j = cmin; j <= cmax; j++){
                if(board[i][j]!=0){
                    throw new Exception("La ligne de départ créée n'est pas rectangulaire");
                }
            }
        }

        res+= lmin + " " + cmin + "\n";
        res+= lmax + " " + cmax;
        return res;
    }

    public String formatStart(Point startPos){
        String res = "";
        res += startPos.y+" ";
        res+= startPos.x+"\n";
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
    private int[][] resizedGrid;
    private int[][] tempGrid;
    private Dimension visibleSize;
    private Dimension realSize;
    private int caseSize;
    private double probBlack;
    private int currentTexture;
    private BufferedImage[] textures;
    private File f;
    private int defaultNum;
    private Point[] bounds;
    private boolean selectBounds;
    private boolean selectStart;
    private CoreEditor c;

    public PaintPanel(Dimension size, int caseSize, Hashtable<String, String> map,int[][] board,CoreEditor coreEditor){
        c = coreEditor;
        this.visibleSize = size;
        this.caseSize = caseSize;
        Dimension n = new Dimension(100,100);
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

        grid = new int[(int) n.getHeight()][(int) n.getWidth()];
        for (int i = 0; i < n.getHeight(); i++) {
            for (int j = 0; j < n.getWidth(); j++) {
                grid[i][j] = defaultNum;
            }
        }

        if(board!=null){
            int nX = (grid[0].length-board[0].length)/2;
            int nY = (grid.length-board.length)/2;
            for(int i = nY; i < nY+board.length;i++){
                for(int j = nX;j < nX+board[0].length;j++){
                    grid[i][j] = board[i-nY][j-nX];
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
        selectStart = false;
        selectBounds = false;
        bounds = new Point[2];
    }

    public int[][] resizeBoard(int[][] board,Point[] mapBounds){
        int x = Math.min(mapBounds[1].x,mapBounds[0].x);
        int y = Math.min(mapBounds[1].y,mapBounds[0].y);
        int dx = Math.abs(mapBounds[1].x-mapBounds[0].x);
        int dy = Math.abs(mapBounds[1].y-mapBounds[0].y);
        int[][] res = new int[dy+1][dx+1];
        for(int i = y; i<=y+dy; i++){
            for(int j = x; j<=x+dx; j++){
                res[i-y][j-x] = board[i][j];
            }
        }
        return res;
    }

    public void setDefaultNum(int i){
        defaultNum =i;
    }

    public int[][] getGrid(){return grid;}

    public int[][] getResizedGrid(){
        if(bounds != null) {
            resizedGrid = resizeBoard(grid, bounds);
            return resizedGrid;
        }
        return null;
    }

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

    public void selectBounds(){
        selectBounds = true;
        JOptionPane.showMessageDialog(this,"Sélectionnez (en cliquant) un des coins de votre map");
    }

    public void selectStart(){
        selectStart = true;
        JOptionPane.showMessageDialog(this,"Sélectionnez (en cliquant) la case de départ de la course");
    }

    public void setCaseSize(int num){
        realSize.setSize(grid[0].length*num,grid.length*num);
        caseSize = num;
        setSize(realSize);
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

    public void save(String mapName){
        tempGrid = grid.clone();
        grid = resizedGrid.clone();
        //setCaseSize(Math.max(visibleSize.height/resizedGrid.length,visibleSize.width/resizedGrid[0].length));
        setCaseSize(40);
        Dimension d = this.getSize();
        BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        print(g2d);
        g2d.dispose();
        try {
            ImageIO.write(image, "PNG", new File(System.getProperty("user.dir")+"/res/textures/map/" + mapName+".png"));
            System.out.println("Successfully created overview");
        }catch(Exception e){
            System.out.println(e);
        }
        grid = tempGrid.clone();
        repaint();
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
        if(e.getSource()== this) {
            int a = e.getX() / caseSize;
            int b = e.getY() / caseSize;
            if (0 <= b && b < grid.length && 0 <= a && a < grid[0].length) {
                if (selectBounds) {
                    if (bounds[0] == null) {
                        bounds[0] = new Point(a, b);
                        JOptionPane.showMessageDialog(this, "Sélectionnez maintenant le coin diagonalement opposé");
                    } else {
                        if (!(Math.abs(a - bounds[0].x) == 0 || Math.abs(b - bounds[0].y) == 0)) {
                            bounds[1] = new Point(a, b);
                            c.setMapBounds(bounds);
                            selectBounds = false;
                            resizedGrid = resizeBoard(grid, bounds);
                        } else {
                            JOptionPane.showMessageDialog(this, "Le coin opposé ne doit ni être sur la même ligne, ni sur la même colonne");
                        }

                    }
                } else if (selectStart) {
                    int x = Math.min(bounds[1].x, bounds[0].x);
                    int y = Math.min(bounds[1].y, bounds[0].y);
                    int dx = Math.abs(bounds[1].x - bounds[0].x);
                    int dy = Math.abs(bounds[1].y - bounds[0].y);
                    if (x < a && a < x + dx && y < b && b < y + dy) {
                        c.setStart(new Point(a-x, b-y));
                        selectStart = false;
                    } else {
                        JOptionPane.showMessageDialog(this, "La case de départ doit se situe strictement à l'intérieur des limites de la map");
                    }
                } else {
                    grid[b][a] = currentTexture;
                    repaintRect = true;
                    repaint(a * caseSize, b * caseSize, caseSize, caseSize);
                }
            }
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
