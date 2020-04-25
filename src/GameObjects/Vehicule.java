package GameObjects;

import java.awt.*;

public abstract class Vehicule extends Objet{

	// vitesse
	protected double vx;
	protected double vy;
	protected double normeV;

	protected Color color;

	public abstract void avancer(boolean[] keyPressed, Map map);
	protected abstract void accelerer(boolean av, boolean ar);
	protected abstract void tourner(boolean g, boolean d);

	public void setColor(Color c){
		color = c;
	}
	public Position getP() {
		return P;
	}
}

