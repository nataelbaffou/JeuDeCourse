import java.awt.*;

public class Case {
    private Texture texture;
    private Position P;

    public Case(Texture texture, Position P){
        this.texture = texture; //On ne copie pas exprès pour conserver uniquement les images chargée au départ
        this.P = P.copy();
    }

    public void dessine(Graphics g){
        texture.dessine(g, P);
    }


}
