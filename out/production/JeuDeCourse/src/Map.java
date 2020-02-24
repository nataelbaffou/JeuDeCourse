import java.awt.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Map {
    private Texture[] textures;
    private int width;
    private int height;
    private int nbCaseX;
    private int nbCaseY;
    private Case[][] board;
    private String mapsName = "default";

    public Map(int w, int h){
        width = w;
        height = h;
        loadData();
        generateBoard();
    }

    private void loadData(){
        String path = System.getProperty("user.dir");
        File f = new File(path + "/res/textures");
        String[] pathnames = f.list();
        int countImages = 0;
        for(String pathname : pathnames) {
            if (pathname.matches("[a-zA-Z_0-9]*.png")) {
                countImages += 1;
            }
        }
        textures = new Texture[countImages];
        int iImg = 0;
        for(int iPath = 0; iPath < countImages; iPath++){
            if (pathnames[iPath].matches("0[a-zA-Z_0-9]*.png")) {
                textures[iImg] = new Texture(path+"/res/textures/"+pathnames[iPath], pathnames[iPath]);
                iImg++;
            }
        }
    }

    public void generateBoard(){
        String mainPath = System.getProperty("user.dir");
        Path file = FileSystems.getDefault().getPath(mainPath + "/res/maps", mapsName);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = reader.readLine();
            if(line != null){
                nbCaseX = Integer.parseInt(line.split(" ")[0]);
                nbCaseY = Integer.parseInt(line.split(" ")[1]);
                board = new Case[nbCaseY][nbCaseX];
                int iLig = 0;
                Position P = new Position(0, 0, width/nbCaseX, height/nbCaseY, 0, 0);
                Position dx = new Position(width/nbCaseX, 0);
                Position dy = new Position(0, height/nbCaseY);
                while ((line = reader.readLine()) != null) {
                    String[] stringVals = line.split(" ");
                    for(int iCol = 0; iCol < nbCaseX; iCol++){
                        int val = Integer.parseInt(stringVals[iCol]);
                        board[iLig][iCol] = new Case(textures[val], P);
                        P.add(dx);
                    }
                    P.x = 0;
                    P.add(dy);
                    iLig++;
                }

            }

        } catch (IOException x) {
            System.err.println(x);
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