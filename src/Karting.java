
import javax.swing.*;
import java.awt.*;

public class Karting extends Vehicule{

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

	public Karting(int x, int y){
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
		
		normes = new double[tx.length];
		angles = new double[tx.length];
		
		for(int i=0; i < tx.length; i++){
			normes[i] = Math.sqrt(tx[i]*tx[i]+ty[i]*ty[i]);
			angles[i] = Math.atan2(ty[i], tx[i]);
		}
		
	}

	public void avancer(){
		x += vx*dt;
		y += vy*dt;
	}
	
	
	private void accelerer(int sens){
		vx -= sens*Math.sin(alpha);
		vy += sens*Math.cos(alpha);
	}

	public void accelerer(){
		this.accelerer(1);
	}

	public void freiner(){
		this.accelerer(-1);
	}
	
	public void ralentir(){
		vx = vx/F;
		vy = vy/F;
		if(Math.abs(vx) < 0.01){ vx = 0; }
		if(Math.abs(vy) < 0.01){ vy = 0; }
	}

	public void tournerADroite(){
		this.alpha += 0.1;
	}

	public void tournerAGauche(){
		this.alpha -= 0.1;
	}

	public void dessine(Graphics g){
		g.setColor(Color.black);
		int[] posX = new int[5];
		int[] posY = new int[5];
		for(int i=0; i < 5; i++){
			posX[i] = x + (int)(normes[i] * Math.cos(alpha + angles[i]));
			posY[i] = y + (int)(normes[i] * Math.sin(alpha + angles[i]));
		}
		g.fillPolygon(posX, posY, posX.length);
	}
	


}
