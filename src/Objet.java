import java.awt.*;

public abstract class Objet {
    protected Texture texture;
    protected Position P;

    public void dessine(Graphics g){
        texture.dessine(g, P);
    }

}
