import java.awt.*;
import java.util.ArrayList;

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

    public static ArrayList<Case> getCasesInCollision(Vehicule V, Map map){
        ArrayList<Case> casesEnCollision = new ArrayList<Case>();
        for(int iLig=0; iLig<map.getNbCaseY(); iLig++){
            for(int iCol=0; iCol<map.getNbCaseX(); iCol++){
                if(Collision.isColliding(V, map.get(iLig, iCol))){
                    casesEnCollision.add(map.get(iLig, iCol));
                }
            }
        }
        return casesEnCollision;

    }

    private static boolean isColliding(Vehicule V, Case c){

        if(V instanceof Karting){
            return isColliding((Karting)V, c);
        } else {
            System.out.println("Ce vehicule n'est pas un kart et ce n'est pas normal");
            return false;
        }
    }

    private static boolean isColliding(Karting kart, Case c){
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

    public static int[] getDirectionOfCollision(Karting kart, Case c){
        int[] dir = {1, 1, 1, 1};
        Polygon pKart = kart.getPolygon();
        Polygon pCase = c.getPolygon();

        for(int ikart=0; ikart<pKart.npoints; ikart++){
            if(pCase.contains(pKart.xpoints[ikart], pKart.ypoints[ikart])){
                return dir;
            }
        }
        for(int icase=0; icase<pCase.npoints; icase++){
            if(pKart.contains(pCase.xpoints[icase], pCase.ypoints[icase])){
                return dir;
            }
        }
        return dir;
    }
}
