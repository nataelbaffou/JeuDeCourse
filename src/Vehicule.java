
import javax.swing.*;
import java.awt.*;

public abstract class Vehicule{

	// position
	protected Position P;

	// vitesse
	protected double vx;
	protected double vy;

	protected Color color;


	public void avancer(boolean[] keyPressed){}
	public void accelerer(boolean av, boolean ar){}
	public void ralentir(){}
	public void tourner(boolean g, boolean d){}
	public void dessine(Graphics g){}

	public void setColor(Color c){ color = c;}
	public Position getP() {
		return P;
	}
}

