
import javax.swing.*;
import java.awt.*;

public class Karting extends Vehicule{

	// direction (-1 gauche, 0 droit, 1 droite)
	private int orientation=0;
	private int sensDir=0;

	// description
	
	// caractéristiques du karting
	private double dt = 1;
	private double masse = 10;
	private double F = 1.25; // force de frottement des roues
	private Roue[] roues;

	public Karting(){
		this(0, 0);
	}

	public Karting(int x, int y){
		this.P = new Position(x, y, 20, 30);
		this.vx = 0;
		this.vy = 0;

		texture = new Texture(System.getProperty("user.dir")+"/res/textures/1karting.png", "kart");

		roues = new Roue[4];
		roues[0] = new Roue(P.width/2, P.height/3);
		roues[1] = new Roue(P.width/2, -P.height/2);
		roues[2] = new Roue(-P.width/2, -P.height/2);
		roues[3] = new Roue(-P.width/2, P.height/3);
		
	}

	public void avancer(boolean[] keyPressed, Map map) {
		ralentir();
		accelerer(keyPressed[2], keyPressed[0]);
		tourner(keyPressed[1], keyPressed[3]);

		P.x += vx * dt;
		P.y += vy * dt;
		double normeV = Math.sqrt(vx * vx + vy * vy);
		if (normeV != 0) {
			P.setRad(P.getRad() + sensDir * orientation * 0.1);
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
		((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		roues[0].dessine(g, P);
		roues[1].dessine(g, P);
		roues[2].dessine(g, P);
		roues[3].dessine(g, P);
		texture.dessine(g, P);

	}
}
