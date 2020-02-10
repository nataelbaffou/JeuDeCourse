public class Position{
    public int x;
    public int y;
    private double alpha;

    public Position(){
        x = 0;
        y = 0;
        alpha = 0;
    }

    public Position(int px, int py){
        x = px;
        y = py;
        alpha = 0;
    }

    public Position(int px, int py, double pa){
        x = px;
        y = py;
        alpha = pa;
    }

    public Position add(Position P){
        return new Position(x+P.x, y+P.y, alpha+P.alpha);
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
}