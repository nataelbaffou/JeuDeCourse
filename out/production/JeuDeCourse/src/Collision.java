import java.awt.*;

public abstract class Collision {

    public static boolean isColliding(Vehicule V, Map map){
        for(int iLig=0; iLig<map.getNbCaseY(); iLig++){
            for(int iCol=0; iCol<map.getNbCaseX(); iCol++){
                if(map.get(iLig, iCol).isBlocking() && Collision.isColliding(V, map.get(iLig, iCol))){
                    return true;
                }
            }
        }
        return false;

    }

    public static boolean isColliding(Vehicule V, Case c){

        if(V instanceof Karting){
            return isColliding((Karting)V, c);
        } else {
            System.out.println("Ce vehicule n'est pas un kart et ce n'est pas normal");
            return false;
        }
    }

    public static boolean isColliding(Karting kart, Case c){
        Polygon Pkart = kart.getPolygon();
        Polygon Pcase = c.getPolygon();

        for(int ikart=0; ikart<Pkart.npoints; ikart++){
            if(Pcase.contains(Pkart.xpoints[ikart], Pkart.ypoints[ikart])){
                return true;
            }
        }
        for(int icase=0; icase<Pcase.npoints; icase++){
            if(Pkart.contains(Pcase.xpoints[icase], Pcase.ypoints[icase])){
                return true;
            }
        }
        return false;
    }
}
