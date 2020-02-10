import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class CustomButton extends JButton {
    private String text;
    public CustomButton(String text){
        setBackground(Color.lightGray);
        this.text = text;
        setText(text);
        try {
            setFont(Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/fonts/memphis5.ttf")).deriveFont(25F));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            setFont(new Font("Arial",Font.PLAIN,20));
        }
    }
}
