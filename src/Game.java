import java.awt.*;
import java.util.Hashtable;
import java.util.LinkedList;

public class Game {
    private Joueur[] players;
    private int nPlayers;
    private int[] lapsPerPlayer;
    private int nLaps = 1;
    private Map map;
    private String title = "default";

    public Game(Joueur[] j, String id, int w, int h){
        players = j;
        Hashtable<String, String> dico = IOFiles.getInformation("games/", id);
        for(java.util.Map.Entry<String, String> param : dico.entrySet()){
            switch (param.getKey()){
                case "map":
                    map = new Map(w, h, param.getValue());
                    break;
                case "title":
                    title = param.getValue();
                    break;
                case "nLaps":
                    nLaps = Integer.parseInt(param.getValue());
                    break;
                default:
                    System.out.println("Un parametre enregistré dans le fichier : games/"+id+" n'est pas correct : " + param.getKey());
            }
        }
        if(map == null){
            System.out.println("Le paramètre 'map' n'a pas été trouvé dans le fichier games/"+id);
            map = new Map(w, h, "0");
        }

        // initialise le nombre de tours des joueurs
        nPlayers = j.length;
        lapsPerPlayer = new int[nPlayers];
        for(int i = 0; i< nPlayers; i++){
            lapsPerPlayer[i] = 0;
        }

        // initialise les véhicules de chaque joueurs
        for(int i = 0; i< nPlayers; i++){
            players[i].setVehicule(80, 80, map.getWidthCase());
        }
    }

    public void tick(LinkedList<Integer> pressedKeys){
        avancer(pressedKeys);
    }

    public void avancer(LinkedList<Integer> pressedKeys){
        for(int i = 0; i< nPlayers; i++){
            players[i].avancer(pressedKeys, map);
        }
    }

    public int isOver(){
        for(int i = 0; i< nPlayers; i++){
            if(lapsPerPlayer[i] == nLaps){
                return i;
            }
        }
        return -1;
    }

    public void dessineMap(Graphics g){
        map.dessine(g);
    }
    public void dessineJoueurs(Graphics g){
        for(Joueur j: players){
            j.dessine(g);
        }
    }
}
