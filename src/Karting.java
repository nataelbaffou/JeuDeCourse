
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

	public Karting(double x, double y){
		texture = new Texture(System.getProperty("user.dir")+"/res/textures/green.png", "kart");
		double r = texture.getImg().getHeight()/texture.getImg().getWidth();
		this.P = new Position(x, y, 20, (int)(20*r));
		this.vx = 0;
		this.vy = 0;

		roues = new Roue[4];
		roues[0] = new Roue(P.width/2, P.height/3);
		roues[1] = new Roue(P.width/2, -P.height/2);
		roues[2] = new Roue(-P.width/2, -P.height/2);
		roues[3] = new Roue(-P.width/2, P.height/3);
		
	}

	public void avancer(boolean[] keyPressed, Map map) {
		Position dP = new Position();

		ralentir();
		accelerer(keyPressed[3], keyPressed[1]);
		tourner(keyPressed[0], keyPressed[2]);

		double normeV = Math.sqrt(vx * vx + vy * vy);

		dP.x = vx * dt;
		dP.y = vy * dt;

		if (normeV != 0) {
			dP.setRad(sensDir*orientation*0.1);
		}

		P.add(dP);

		if(Collision.isColliding(this, map)){
			// On annule l'avance du véhicule et on stop sa vitesse
			P.substract(dP);
			vx = 0;
			vy = 0;
		}
	}

	protected void accelerer(boolean av, boolean ar){
		sensDir = 0;
		if(av && !ar){ sensDir = 1;}
		if(ar && !av){ sensDir = -1;}
		vx -= sensDir*Math.sin(P.getRad());
		vy += sensDir*Math.cos(P.getRad());
	}
	
	protected void ralentir(){
		vx = vx/F;
		vy = vy/F;
		if(Math.abs(vx) < 0.1){ vx = 0; }
		if(Math.abs(vy) < 0.1){ vy = 0; }
	}

	protected void tourner(boolean g,  boolean d){
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
/*
	protected void getReachableDirections(Map map){
		ArrayList<Case> casesEnCollision = Collision.isColliding(this, map);
		int[] directions = {1, 1, 1, 1};
		for(Case c : casesEnCollision){
			if(c.isBlocking()){
				int[] caseDirection = Collision.getDirectionOfCollision(this, c);
				for(int i=0; i<caseDirection.length; i++ ){
					directions[i] *= caseDirection[i];
				}
			}
		}
	}*/

	public void dessine(Graphics g){
		((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		roues[0].dessine(g, P);
		roues[1].dessine(g, P);
		roues[2].dessine(g, P);
		roues[3].dessine(g, P);
		texture.dessine(g, P);

	}
}
