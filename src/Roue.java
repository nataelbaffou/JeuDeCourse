import java.awt.*;

public class Roue extends Objet{

    public Roue(double x, double y){
        P = new Position(x, y, 6, 9); // position relative au kart
        texture = new Texture(System.getProperty("user.dir")+"/res/textures/1roue.png", "roue");
    }

    public void tournerAGauche(){
        P.setDeg(-35);
    }

    public void tournerADroite(){
        P.setDeg(35);
    }

    public void toutDroit(){
        P.setDeg(0);
    }

    public void dessine(Graphics g, Position kartP){
        Position P2 = Position.sumRelative(kartP, P);
        texture.dessine(g, P2);
    }
}