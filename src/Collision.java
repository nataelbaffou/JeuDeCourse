

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
        boolean b = false;
        //if(Collision.isColliding(kart))
        return b;
    }
}
