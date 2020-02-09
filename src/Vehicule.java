
import javax.swing.*;
import java.awt.*;

public abstract class Vehicule{

	// position
	protected int x;
	protected int y;

	// vitesse
	protected double vx;
	protected double vy;

	// direction
	protected double alpha = 0;


	public void avancer(){}
	public void accelerer(){}
	public void freiner(){}
	public void ralentir(){}
	public void tournerADroite(){}
	public void tournerAGauche(){}
	public void dessine(Graphics g){}
	
}

