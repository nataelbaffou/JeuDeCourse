import java.awt.*;

public class Roue {
    private double orientation = 0;

    public void tournerAGauche(){
        orientation = 45;
    }

    public void tournerADroite(){
        orientation = -45;
    }

    public void toutDroit(){
        orientation = 0;
    }

    public void dessine(Graphics g){

    }
}