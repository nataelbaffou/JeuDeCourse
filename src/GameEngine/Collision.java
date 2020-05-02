package GameEngine;

import GameObjects.Case;
import GameObjects.Karting;
import GameObjects.Map;
import GameObjects.Vehicule;

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
        ArrayList<Case> casesEnCollision = new ArrayList<>();
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
        Polygon[] kartPolygons = kart.getAllPolygons();
        Polygon Pcase = c.getPolygon();

        for(Polygon Pkart : kartPolygons){
            if(isColliding(Pkart, Pcase)){
                return true;
            }
        }
        return false;
    }

    public static boolean isColliding(Polygon P1, Polygon P2){
        for(int i=0; i<P1.npoints; i++){
            if(P2.contains(P1.xpoints[i], P1.ypoints[i])){
                return true;
            }
        }
        for(int i=0; i<P2.npoints; i++){
            if(P1.contains(P2.xpoints[i], P2.ypoints[i])){
                return true;
            }
        }
        return false;
    }

    /*
    public static int[] getDirectionOfCollision(GameObjects.Karting kart, GameObjects.Case c){
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
     */
}
