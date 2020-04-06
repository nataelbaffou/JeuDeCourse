import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.IOException;
import java.io.InputStream;

public class Audio {

    private static final String SOUND_PATH = "sound/";

    private AudioStream audioStream;

    public Audio(){}

    public void playMusic(String theme)
    {
        stopMusic();
        try
        {
            // TODO Faire en sorte que la musique se répète ;)
            // how to get an image file as a resource out of a jar file
            //imgBoldText = new ImageIcon(com.devdaily.opensource.jelly.MainFrame.class.getResource("text_bold.png"));

            // (1) get resource as a file on the filesystem
            //InputStream inputStream = new FileInputStream("/Users/al/DevDaily/Projects/MeditationApp/resources/gong.au");

            // (2) get the sound file as a resource out of our jar file;
            //     the sound file must be in the same directory as this class file.
            //     this input stream recipe comes from a javaworld.com article.
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
            InputStream inputStream = Audio.class.getResourceAsStream(SOUND_PATH+filename);
            audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);

            // other methods (not needed here; just kept here for reference)
            //audioStream.close();
            //AudioPlayer.player.stop(as);
        }
        catch (Exception e)
        {

        }
    }

    private void stopMusic(){
        try {
            audioStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){

        }
    }
}