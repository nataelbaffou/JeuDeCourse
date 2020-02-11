public class Position{
    public int x;
    public int y;
    public int width;
    public int height;
    private double alpha;

    public Position(){
        this(0, 0, 0, 0, 0);
    }

    public Position(int px, int py){
        this(px, py, 0, 0, 0);
    }

    public Position(int px, int py, double pa){
        this(px, py, 0, 0, pa);
    }

    public Position(int px, int py, int dx, int dy){
        this(px, py, dx, dy, 0);
    }

    public Position(int px, int py, int dx, int dy, double pa){
        x = px;
        y = py;
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

    public Position copy(){
        return new Position(x, y, width, height, alpha);
    }
}