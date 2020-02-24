import java.awt.*;

public abstract class Objet {
    protected Texture texture;
    protected Position P;

    public void dessine(Graphics g){
        texture.dessine(g, P);
    }

    public Polygon getPolygon(){
        Polygon Pol = new Polygon();
        Pol.addPoint(P.x + (int)(-P.centerX*Math.sin(P.getRad())), P.y + (int)(-P.centerY*Math.cos(P.getRad())));
        Pol.addPoint(P.x + (int)((-P.centerX + P.width)*Math.sin(P.getRad())), P.y + (int)(-P.centerY*Math.cos(P.getRad())));
        Pol.addPoint(P.x + (int)((-P.centerX + P.width)*Math.sin(P.getRad())), P.y + (int)((-P.centerY+P.height)*Math.cos(P.getRad())));
        Pol.addPoint(P.x + (int)(-P.centerX*Math.sin(P.getRad())), P.y + (int)((-P.centerY+P.height)*Math.cos(P.getRad())));
        return Pol;
    }
}
