import java.awt.*;
import java.io.File;
import java.util.Hashtable;

public class Map {
    private Texture[] textures;
    private int width;
    private int height;
    private int nbCaseX;
    private int nbCaseY;
    private Case[][] board;
    private String mapsName = "default";

    public Map(int w, int h, String id){
        width = w;
        height = h;
        loadData();
        generateBoard(id);
    }

    private void loadData(){
        String path = System.getProperty("user.dir");
        File f = new File(path + "/res/textures");
        String[] names = f.list();
        int countImages = 0;
        for(String pathname : names) {
            if (pathname.matches("0[a-zA-Z_0-9]*.png")) {
                countImages += 1;
            }
        }
        textures = new Texture[countImages];
        for (String name : names) {
            int iImg = -1;
            boolean forMap = true;
            switch (name) {
                case "0grass.png":
                    iImg = 0;
                    break;
                case "0wall.png":
                    iImg = 1;
                    break;
                default:
                    forMap = false;
            }
            if (forMap) {
                textures[iImg] = new Texture(path + "/res/textures/" + name, name);
            }
        }
    }

    public void generateBoard(String id){
        String mainPath = System.getProperty("user.dir");

        Hashtable<String, String> dico = IOFiles.getInformation("maps", mapsName);

        String line = dico.get("size");
        String[] boardData = dico.get("board").split("\n");

        nbCaseX = Integer.parseInt(line.split(" ")[0]);
        nbCaseY = Integer.parseInt(line.split(" ")[1]);

        board = new Case[nbCaseY][nbCaseX];

        Position P = new Position(0, 0, width/nbCaseX, height/nbCaseY, 0, 0);
        Position dx = new Position(width/nbCaseX, 0);
        Position dy = new Position(0, height/nbCaseY);

        for(int iLig = 0; iLig < nbCaseY; iLig++) {
            line = boardData[iLig];
            String[] stringVals = line.split(" ");
            for (int iCol = 0; iCol < nbCaseX; iCol++) {
                int val = Integer.parseInt(stringVals[iCol]);
                if (val >= textures.length) {
                    val = 0;
                }
                board[iLig][iCol] = new Case(textures[val], P);
                P.add(dx);
            }
            P.x = 0;
            P.add(dy);
        }
    }

    public void dessine(Graphics g){
        for(int iLig=0; iLig<nbCaseY; iLig++){
            for(int iCol=0; iCol<nbCaseX; iCol++){
                board[iLig][iCol].dessine(g);
            }
        }
    }

    public Case[][] getBoard() {
        return board;
    }

    public Case get(int noLig, int noCol){
        return board[noLig][noCol];
    }

    public int getNbCaseX() {
        return nbCaseX;
    }

    public int getNbCaseY() {
        return nbCaseY;
    }
}
