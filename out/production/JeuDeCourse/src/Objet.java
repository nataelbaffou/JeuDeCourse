import java.awt.*;

public abstract class Objet {
    protected Texture texture;
    protected Position P;
    protected boolean showCollisionBox = true;

    public void dessine(Graphics g){
        texture.dessine(g, P);
        if(showCollisionBox){
            g.setColor(Color.BLACK);
            g.drawPolygon(this.getPolygon());
        }
    }

    public Polygon getPolygon(){
        return getPolygon(P);
    }

    public static Polygon getPolygon(Position P) {
        Polygon Pol = new Polygon();
        for(int[] c : new int[][] {{0, 0}, {0, 1}, {1, 1}, {1, 0}}){
            int x = (int)(-P.centerX + P.width*c[0]);
            int y = (int)(-P.centerY + P.height*c[1]);
            double dist = Math.sqrt(x*x+y*y);
            double angle = Math.atan2(x,y);
            Pol.addPoint((int)(P.x + dist*Math.sin(angle-P.getRad())), (int)(P.y +  dist * Math.cos(angle - P.getRad())));
        }
        return Pol;
    }

    public Texture getTexture() {
        return texture;
    }
}