public class Position{
    public int x;
    public int y;
    public int centerX;
    public int centerY;
    public int width;
    public int height;
    private double alpha;

    public Position(){
        this(0, 0, 0, 0, 0, 0, 0);
    }

    public Position(int px, int py){
        this(px, py, 0, 0, 0, 0, 0);
    }

    public Position(int px, int py, double pa){
        this(px, py, 0, 0, 0, 0, pa);
    }

    public Position(int px, int py, int dx, int dy){
        this(px, py, dx, dy, dx/2, dy/2, 0);
    }

    public Position(int px, int py, int dx, int dy, int cx, int cy){
        this(px, py, dx, dy, cx, cy, 0);
    }

    public Position(int px, int py, int dx, int dy, int cx, int cy, double pa){
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
        return new Position((int)(P1.x + dist*Math.sin(angle-P1.getRad())),
                            (int)(P1.y + dist*Math.cos(angle-P1.getRad())),
                            P2.width, P2.height, P2.centerX, P2.centerY, P1.getDeg()+P2.getDeg());
    }

    public Position copy(){
        return new Position(x, y, width, height, centerX, centerY, alpha);
    }
}