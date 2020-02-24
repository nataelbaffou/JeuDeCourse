import java.awt.*;

public class Case extends Objet{

    public Case(){
        this(new Texture(), new Position());
    }

    public Case(Texture texture, Position P){
        this.texture = texture; //On ne copie pas exprès pour conserver uniquement les images chargée au départ
        this.P = P.copy();
    }

    public void dessine(Graphics g){
        texture.dessine(g, P);
    }

    public Case copy(){
        return new Case(texture, P.copy());
    }

    public boolean isBlocking(){
        return texture.isBlocking();
    }


}
