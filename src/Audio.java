import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/*
Audio class

Fonctionnement :
    - Initialisation :
        . On récupère FenetrePrincipale pour pouvoir récupérer toutes les données nécessaires
        . On créé les List de musiques utiles
    - Lancement d'une musique :
        . Appel a  playMusic ou playTheme
        . playMusic : son joué qu'une seule fois puis la musique de fond sera rappelée
        . playTheme : le theme sera joué jusqu'à lancement d'une autre musique
        . -> init sound et lance le son
    - Gestion des sons :
        . Si un autre son est appelé on coupe le précédent
        . Si le son actuel est fini on lance le son suivant en fonction des paramètres actuels
 */


public class Audio{

    private static final String SOUND_PATH = "sound/";

    private Clip clip;
    private Sound sound;
    private FenetrePrincipale f;
    private ArrayList<String> musicMenu = new ArrayList<>();
    private ArrayList<String> musicRace = new ArrayList<>();
    private ArrayList<String> musicTroll = new ArrayList<>();

    private boolean isTrolling = false;

    public Audio(FenetrePrincipale fenetrePrincipale){
        f = fenetrePrincipale;
        {
            try {
                clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }

        ArrayList<String> musics = getMusics();

        for(String s : musics){
            if(s.startsWith("menu/")){
                musicMenu.add(s);
            }else if(s.startsWith("race/")){
                musicRace.add(s);
            }else if(s.startsWith("troll/")){
                musicTroll.add(s);
            }
        }
    }

    public static boolean isMusic(String s){
        try{
            return s.substring(s.indexOf(".")).equals(".wav");
        }catch (Exception e){
            return false;
        }

    }

    public static ArrayList<String> getMusics(){
        ArrayList<String> M = new ArrayList<>();

        File f = new File(Audio.class.getResource("sound/").getFile());
        for(String path : f.list()){
            if(!path.contains(".")){
                File g = new File(Audio.class.getResource("sound/"+path+"/").getFile());
                for(String s : g.list()){
                    if(isMusic(s)){
                        M.add(path+"/"+s);
                    }
                }
            }
        }
        Collections.sort(M);
        return M;
    }

    private void playSound(){
        stopMusic();
        try {
            clip = AudioSystem.getClip();
            clip.addLineListener(new ClipHandler(sound, clip));
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(SOUND_PATH+sound.filename)));
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        clip.start();
    }

    public void stopMusic(){
        try {
            clip.close();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void playTheme(String theme)
    {
        if(theme == "troll"){
            isTrolling = true;
            sound = new Sound(theme, true);
            playSound();
        }
        if(!isTrolling){
            sound = new Sound(theme, f.getSettings().isMusicRandom());
            playSound();
        }
    }

    public void playMusic(String filename)
    {
        if(!isTrolling){
            sound = new Sound(filename);
            playSound();
        }
    }

    public void stopTroll(){
        isTrolling = false;
    }

    private class ClipHandler implements LineListener{

        private Sound s;
        private Clip c;

        public ClipHandler(Sound s, Clip c){
            this.s = s;
            this.c = c;
        }

        @Override
        public void update(LineEvent e) {
            if(e.getType() == LineEvent.Type.STOP){
                if(!(c.getFrameLength()>e.getFramePosition())){
                    sound = s.getNextSound();
                    playSound();
                }
            }
            if(e.getType() == LineEvent.Type.CLOSE){
                s = null;
                c = null;
            }
        }
    }

    private class Sound {
        public String filename;
        private String theme;
        private boolean randomize;

        public Sound(String theme, boolean randomize){
            this.theme = theme;
            this.randomize = randomize;

            if(randomize){
                filename = getRandomlyFromTheme(theme);
            } else{
                switch (theme){
                    case "menu":
                        filename = f.getSettings().getMusicMenuBox().getSelectedItem().toString();
                        break;
                    case "race":
                        filename = f.getSettings().getMusicRaceBox().getSelectedItem().toString();
                        break;
                    case "troll":
                        filename = getRandomlyFromTheme("troll");
                    default:
                        filename = getRandomlyFromTheme("none");
                }
            }
        }

        public Sound(String filename){
            this.filename = filename;
            this.theme = "none";
            this.randomize = false;
        }

        public Sound(String filename, String theme, boolean randomize){
            this.filename = filename;
            this.theme = theme;
            this.randomize = randomize;
        }

        public Sound getNextSound(){
            if(isTrolling){
                return new Sound(theme, true);
            }
            randomize = f.getSettings().isMusicRandom();
            if(!randomize){
                return new Sound(filename, theme, randomize);
            } else{
                return new Sound(theme, randomize);
            }
        }

        public String getRandomlyFromTheme(String theme){
            Random random = new Random();
            String newFilename;
            switch (theme){
                case "menu":
                    newFilename = musicMenu.get(random.nextInt(musicMenu.size()));
                    break;
                case "race":
                    newFilename = musicRace.get(random.nextInt(musicRace.size()));
                    break;
                case "troll":
                    newFilename = musicTroll.get(random.nextInt(musicTroll.size()));
                    break;
                default:
                    return getRandomlyFromTheme("menu");
            }
            return newFilename;
        }
    }
}