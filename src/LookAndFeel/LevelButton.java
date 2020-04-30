package LookAndFeel;

import IOEngine.IOFiles;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

public class LevelButton extends JButton {
    BufferedImage preview = null;

    public LevelButton(String name){
        super();
        setBorder(null);
        setRolloverEnabled(true);
        setBackground(new Color(0, true));
        setForeground(Color.black);
        setLayout(new GridLayout(1, 2));
        setActionCommand(name);

        if(name.equals("Back to menu")){
            setText(name);
            setBorder(null);
        }else{
            Hashtable<String, String> h = IOFiles.getInformation("maps", name);

            try {
                preview = ImageIO.read(new File("res/textures/map/"+name+".png"));
            } catch (IOException e) {
                // Nothing that's normal if the image hasn't been created
                System.out.println(name);
            }

            if(preview==null){
                add(Box.createGlue());
            }else{
                JPanel imgPanel = new JPanel(){
                    @Override
                    public void paintComponent(Graphics g){
                        super.paintComponent(g);
                        float factor;
                        factor = Math.max((float)preview.getWidth()/this.getWidth(), (float)preview.getHeight()/this.getHeight());
                        int w = (int)(preview.getWidth()/factor);
                        int h = (int)(preview.getHeight()/factor);
                        int dx = (this.getWidth()-w)/2;
                        int dy = (this.getHeight()-h)/2;
                        g.drawImage(preview, dx, dy, w, h, null);
                    }
                };
                imgPanel.setBackground(new Color(0, true));
                add(imgPanel);
            }

            JPanel infoPanel = new JPanel();
            infoPanel.setBackground(new Color(0, true));
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            JLabel title = new JLabel(name){
                @Override
                public Font getFont(){
                    return DesignFont.getTitleFont().deriveFont(0.3f*infoPanel.getHeight());
                }
            };
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setBackground(new Color(0, true));

            JLabel description = new JLabel(h.getOrDefault("description", "Pas de description")){
                @Override
                public Font getFont(){
                    return DesignFont.getTitleFont().deriveFont(0.2f*infoPanel.getHeight());
                }
            };
            description.setAlignmentX(Component.CENTER_ALIGNMENT);
            description.setBackground(new Color(0, true));

            infoPanel.add(title);
            infoPanel.add(description);

            String[] txtSpecsRecords = new String[3];
            txtSpecsRecords[0] = "laps : " + h.getOrDefault("laps", "-");
            txtSpecsRecords[1] = "time record : " + h.getOrDefault("time-record", "--:--:--");
            txtSpecsRecords[2] = "lap record : " + h.getOrDefault("lap-record", "--:--:--");

            for(String s : txtSpecsRecords){
                JLabel spec = new JLabel(s){
                    @Override
                    public Font getFont(){
                        return DesignFont.getTitleFont().deriveFont(0.1f*infoPanel.getHeight());
                    }
                };
                spec.setAlignmentX(Component.CENTER_ALIGNMENT);
                spec.setBackground(new Color(0, true));
                infoPanel.add(spec);
            }

            add(infoPanel);
        }


        try {
            setFont(Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/fonts/memphis5.ttf")).deriveFont(25F));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            setFont(new Font("Arial",Font.PLAIN,20));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setPaintMode();
        if(getModel().isArmed()){
            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(140, 140, 140, 210));
            g.fillRoundRect(0, 0, width, height, height/2, height/2);
        }
        else if(!getModel().isRollover()){
            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(200, 200, 200, 130));
            g.fillRoundRect(0, 0, width, height, height/2, height/2);
        }
        else if(getModel().isRollover()){
            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(200, 200, 200, 220));
            g.fillRoundRect(0, 0, width, height, height/2, height/2);
        }
        super.paintComponent(g);
        betterRender(g);
    }

    public void betterRender(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

}
