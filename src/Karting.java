
import javax.swing.*;
import java.awt.*;

public class Karting extends Vehicule{

	// direction (-1 gauche, 0 droit, 1 droite)
	private int orientation=0;
	private int sensDir=0;

	// description
	
	// coordonnées de la voiture centrée en (0, 0)
	// image du karting
	private int tx[] = {0, 10, 10, -10, -10};
	private int ty[] = {20, 10, -10, -10, 10};
	private double normes[];
	private double angles[];
	
	
	// caractéristiques du karting
	private double dt = 1;
	private double masse = 10;
	private double F = 1.25; // force de frottement des roues
	private Roue[] roues;

	public Karting(int x, int y){
		this.P = new Position(x, y);
		this.vx = 0;
		this.vy = 0;
		
		normes = new double[tx.length];
		angles = new double[tx.length];
		
		for(int i=0; i < tx.length; i++){
			normes[i] = Math.sqrt(tx[i]*tx[i]+ty[i]*ty[i]);
			angles[i] = Math.atan2(ty[i], tx[i]);
		}

		roues = new Roue[4];
		roues[0] = new Roue(10, 10);
		roues[1] = new Roue(10,-10);
		roues[2] = new Roue(-10, -10);
		roues[3] = new Roue(-10,10);
		
	}

	public void avancer(){
		P.x += vx*dt;
		P.y += vy*dt;
		double normeV = Math.sqrt(vx*vx+vy*vy);
		if(normeV!=0){
			P.setRad(P.getRad()+sensDir*orientation*0.1);
		}
	}
	
	
	public void accelerer(boolean av, boolean ar){
		sensDir = 0;
		if(av && !ar){ sensDir = 1;}
		if(ar && !av){ sensDir = -1;}
		vx -= sensDir*Math.sin(P.getRad());
		vy += sensDir*Math.cos(P.getRad());
	}
	
	public void ralentir(){
		vx = vx/F;
		vy = vy/F;
		if(Math.abs(vx) < 0.01){ vx = 0; }
		if(Math.abs(vy) < 0.01){ vy = 0; }
	}

	public void tourner(boolean g, boolean d){
		if(g && !d){
			orientation = -1;
			roues[0].tournerAGauche();
			roues[3].tournerAGauche();
		} else if(d && !g){
			orientation = 1;
			roues[0].tournerADroite();
			roues[3].tournerADroite();
		} else {
			orientation = 0;
			roues[0].toutDroit();
			roues[3].toutDroit();
		}

	}

	public void dessine(Graphics g){
		roues[0].dessine(g, P, normes[1], angles[1]);
		roues[1].dessine(g, P, normes[2], angles[2]);
		roues[2].dessine(g, P, normes[3], angles[3]);
		roues[3].dessine(g, P, normes[4], angles[4]);

		g.setColor(Color.black);
		int[] posX = new int[tx.length];
		int[] posY = new int[tx.length];
		for(int i=0; i < tx.length; i++){
			posX[i] = P.x + (int)(normes[i] * Math.cos(P.getRad() + angles[i]));
			posY[i] = P.y + (int)(normes[i] * Math.sin(P.getRad() + angles[i]));
		}
		g.fillPolygon(posX, posY, posX.length);

	}

	public double[] getNormes(){
		return normes;
	}

	public double[] getAngles() {
		return angles;
	}
}
