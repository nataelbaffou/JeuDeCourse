import java.util.Hashtable;
import java.util.LinkedList;

public class Game {
    private Joueur[] players;
    private int nPlayers;
    private int[] lapsPerPlayer;
    private int nLaps = 1;
    private Map map;
    private String title;

    public Game(Joueur[] j, int id, int w, int h){
        players = j;
        Hashtable<String, String> dico = IOFiles.getInformation("games/"+id);
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
                    System.out.println("Un parametre enregistré dans le fichier : games/"+id+"n'est pas correct");
            }
        }
        nPlayers = j.length;
        lapsPerPlayer = new int[nPlayers];
        for(int i = 0; i< nPlayers; i++){
            lapsPerPlayer[i] = 0;
        }
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
}
