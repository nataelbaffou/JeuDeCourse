package GameObjects;

import IOEngine.IOFiles;

import java.awt.*;
import java.io.File;
import java.util.Hashtable;
import java.util.Set;

public class Map {
    private Texture[] textures;
    private int width;
    private int height;
    private int nbCaseX;
    private int nbCaseY;
    private int widthCase;
    private Case[][] board;
    private Hashtable<String, String> initData;
    private Position startPos;
    private String startLineType = "down";
    private Position startLinePos;

    public Map(){
        loadData();
    }

    private void loadData(){
        String path = System.getProperty("user.dir");
        File f = new File(path + "/res/textures");

        Hashtable<String, String> mapTexturesTable = IOFiles.getInformation("textures", "map_conversion_textures");
        Hashtable<String, String> texturesSettings = IOFiles.getInformation("textures", "textures_settings");
        mapTexturesTable.remove("filename");

        int nbKeys = mapTexturesTable.size();

        textures = new Texture[nbKeys];

        Set<String> keySet = mapTexturesTable.keySet();

        for(String key : keySet){
            String name = mapTexturesTable.get(key);
            textures[Integer.parseInt(key)] = new Texture(path + "/res/textures/" + name, name, texturesSettings);
        }
    }

    public void initMap(int w, int h, Hashtable<String, String> data){
        width = w;
        height = h;
        initData = data;

        // Load and Genarate Board
        String line = initData.get("size");
        String[] boardData = initData.get("board").split("\n");


        nbCaseX = Integer.parseInt(line.split(" ")[0]);
        nbCaseY = Integer.parseInt(line.split(" ")[1]);

        board = new Case[nbCaseY][nbCaseX];
        widthCase = Math.min(width/nbCaseX, height/nbCaseY);

        Position P = new Position(0, 0, widthCase, widthCase, 0, 0);
        Position dx = new Position(widthCase, 0);
        Position dy = new Position(0, widthCase);

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

        // Initialisation of the rest
        startPos = new Position();
        for(java.util.Map.Entry<String, String> param : initData.entrySet()){
            switch (param.getKey()){
                case "start-line":
                    String[] lines1 = param.getValue().split("\n");
                    startLineType = lines1[0];
                    Point p1 = new Point(Integer.parseInt(lines1[1].split(" ")[1])*widthCase, Integer.parseInt(lines1[1].split(" ")[0])*widthCase);
                    Point p2 = new Point((Integer.parseInt(lines1[2].split(" ")[1])+1)*widthCase, (Integer.parseInt(lines1[2].split(" ")[0])+1)*widthCase);
                    startLinePos = new Position(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p2.x-p1.x), Math.abs(p2.y-p1.y), 0, 0);
                    break;
                case "start-position":
                    String[] lines2 = param.getValue().split("\n");
                    startPos.x = Double.parseDouble(lines2[0].split(" ")[1])*widthCase;
                    startPos.y = Double.parseDouble(lines2[0].split(" ")[0])*widthCase;
                    switch (lines2[1]){
                        case "right":
                            startPos.setDeg(-90);
                            break;
                        case "left":
                            startPos.setDeg(90);
                            break;
                        case "up":
                            startPos.setDeg(180);
                            break;
                        case "down":
                            startPos.setDeg(0);
                            break;
                        default:
                            System.out.println("Start orientation isn't known : "+ lines2[1]);
                            startPos.setDeg(180);
                    }
                    break;
            }
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

    public int getWidthCase() {
        return widthCase;
    }

    public Position getStartPos() {
        return startPos;
    }

    public Position getStartLinePos() {
        return startLinePos;
    }

    public String getStartLineType() {
        return startLineType;
    }
}
