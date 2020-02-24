import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.HashMap;

public class Binds extends HashMap<String,Integer> {
    public Binds(){
        put("Forward", KeyEvent.VK_Z);
        put("Backward", KeyEvent.VK_S);
        put("Left", KeyEvent.VK_Q);
        put("Right", KeyEvent.VK_D);
    }
    public void setArrowBind(){
        put("Forward", KeyEvent.VK_UP);
        put("Backward", KeyEvent.VK_DOWN);
        put("Left", KeyEvent.VK_LEFT);
        put("Right", KeyEvent.VK_RIGHT);
    }
}
