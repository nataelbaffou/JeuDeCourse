import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;

public class Audio {

    private static final String SOUND_PATH = "sound/";

    private Clip clip;

    public Audio(){
        {
            try {
                clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public void playMusic(String theme)
    {
        try
        {
            String filename;

            switch (theme){
                case "menu":
                    filename = "ascenceur2.wav";
                    break;
                case "race":
                    filename = "trackmania.wav";
                    break;
                default:
                    filename = "gong.au";
            }

            clip.close();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(SOUND_PATH+filename)));
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopMusic(){
        try {
            clip.close();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}