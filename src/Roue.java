import java.awt.*;

public class Roue {
    // position
    private Position P;

    // coordonnées de la roue centrée en (0, 0)
    // image de la roue
    private int tx[] = {3, 3, -3, -3};
    private int ty[] = {-4, 4, 4, -4};
    private double normes[];
    private double angles[];

    public Roue(int x, int y){
        P = new Position(x, y);

        normes = new double[tx.length];
        angles = new double[tx.length];

        for(int i=0; i < tx.length; i++){
            normes[i] = Math.sqrt(tx[i]*tx[i]+ty[i]*ty[i]);
            angles[i] = Math.atan2(ty[i], tx[i]);
        }
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

    public void dessine(Graphics g, Position kartP, double kartNorme, double kartAngle){
        g.setColor(Color.red);
        int[] posX = new int[tx.length];
        int[] posY = new int[tx.length];
        for(int i=0; i < tx.length; i++){
            posX[i] = kartP.x + (int)( kartNorme*Math.cos(kartP.getRad()+kartAngle) + normes[i]*Math.cos(kartP.getRad()+angles[i]+P.getRad()) );
            posY[i] = kartP.y + (int)( kartNorme*Math.sin(kartP.getRad()+kartAngle) + normes[i]*Math.sin(kartP.getRad()+angles[i]+P.getRad()) );
        }
        g.fillPolygon(posX, posY, posX.length);
    }
}