package GameObjects;/*
    Définit les paramètres de position de tous les objets du jeu
    Pour les différentes coordonnées se référer au dessin réalisé par mes soins (Nataël)
    ATTENTION l'attribut alpha est défini en degré pour l'initialisation : pour le reste il faut utiliser les méthodes
 */

public class Position{
    public double x;
    public double y;
    public double centerX;
    public double centerY;
    public double width;
    public double height;
    private double alpha;

    public Position(){
        this(0, 0, 0, 0, 0, 0, 0);
    }

    public Position(double px, double py){
        this(px, py, 0, 0, 0, 0, 0);
    }

    public Position(double px, double py, double pa){
        this(px, py, 0, 0, 0, 0, pa);
    }

    public Position(double px, double py, double dx, double dy){
        this(px, py, dx, dy, dx/2, dy/2, 0);
    }

    public Position(double px, double py, double dx, double dy, double pa){
        this(px, py, dx, dy, dx/2, dy/2, pa);
    }

    public Position(double px, double py, double dx, double dy, double cx, double cy){
        this(px, py, dx, dy, cx, cy, 0);
    }

    public Position(double px, double py, double dx, double dy, double cx, double cy, double pa){
        x = px;
        y = py;
        centerX = cx;
        centerY = cy;
        width = dx;
        height = dy;
        alpha = pa;
    }

    // add coordinates but not size
    public void add(Position P){
        x += P.x;
        y += P.y;
        alpha += P.getDeg();
    }

    public void substract(Position P){
        x -= P.x;
        y -= P.y;
        alpha -= P.getDeg();
    }

    public double getDeg(){
        return alpha;
    }

    public double getRad(){
        return alpha*Math.PI/180;
    }

    public void setDeg(double a){
        alpha = a;
    }

    public void setRad(double a){
        alpha = a*180/Math.PI;
    }

    public static Position sum(Position P1, Position P2){
        return new Position(P1.x+P2.x, P2.y+P2.y, P1.alpha+P2.alpha);
    }

    public static Position sumRelative(Position P1, Position P2){
        double dist = Math.sqrt(P2.x*P2.x+P2.y*P2.y);
        double angle = Math.atan2(P2.x, P2.y);
        return new Position(P1.x + dist*Math.sin(angle-P1.getRad()),
                            P1.y + dist*Math.cos(angle-P1.getRad()),
                            P2.width, P2.height, P2.centerX, P2.centerY, P1.getDeg()+P2.getDeg());
    }

    public Position copy(){
        return new Position(x, y, width, height, centerX, centerY, alpha);
    }

    public String toString(){
        String res = "";
        res += "x : " + x + " y : " + y + " w : " + width + " h : " + height;// + "\n";
        //res += "cx : " + centerX + " cy : " + centerY + " alpha : " + alpha;
        return res;
    }
}