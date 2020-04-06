import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Binds extends HashMap<String,Integer> {
    public Binds(){
        put("Forward", KeyEvent.VK_UP);
        put("Backward", KeyEvent.VK_DOWN);
        put("Left", KeyEvent.VK_LEFT);
        put("Right", KeyEvent.VK_RIGHT);
    }
    public void setBind(int[] codes){
        put("Forward", codes[0]);
        put("Backward", codes[1]);
        put("Left", codes[2]);
        put("Right", codes[3]);
    }
}
