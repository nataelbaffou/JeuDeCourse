
import javax.swing.*;
import java.awt.*;

public abstract class Vehicule extends Objet{

	// vitesse
	protected double vx;
	protected double vy;


	public abstract void avancer(boolean[] keyPressed, Map map);
	public abstract void accelerer(boolean av, boolean ar);
	public abstract void ralentir();
	public abstract void tourner(boolean g, boolean d);
	public abstract void dessine(Graphics g);

	public Position getP() {
		return P;
	}
}
