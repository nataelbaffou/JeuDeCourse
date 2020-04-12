import java.awt.*;

public class Roue extends Objet{
    Position PosRelative;

    public Roue(double x, double y, Position PRel){
        P = new Position(x, y, Math.abs(x)/4, 2*Math.abs(x)/5); // position relative au kart
        this.PosRelative = PRel;
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

    public void dessine(Graphics g){
        Position P2 = Position.sumRelative(PosRelative, P);
        texture.dessine(g, P2);
        if(showCollisionBox){
            g.setColor(Color.BLACK);
            g.drawPolygon(this.getPolygon());
        }
    }

    @Override
    public Polygon getPolygon(){
        return Objet.getPolygon(Position.sumRelative(PosRelative, P));
    }
}