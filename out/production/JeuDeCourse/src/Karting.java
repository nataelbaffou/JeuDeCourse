
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Karting extends Vehicule{

	// direction (-1 gauche, 0 droit, 1 droite)
	private int orientation=0;
	private int sensDir=0;

	// description
	
	// caractéristiques du karting
	private double dt = 0.1;
	private double masse = 10;
	private double F = 1.1; // force de frottement des roues
	private Roue[] roues;

	public Karting(){
		this(new Position(), 20, Color.RED);
	}

	public Karting(Position initPos, int widthCase, Color c){
		//texture = new Texture(System.getProperty("user.dir")+"/res/textures/green.png", "kart");

		setColor(c);

		double widthFactor = 0.7;
		double heightFactor = 1.4;
		this.P = new Position(initPos.x, initPos.y, widthCase*widthFactor, widthCase*heightFactor, initPos.getDeg());
		this.vx = 0;
		this.vy = 0;

		roues = new Roue[4];
		roues[0] = new Roue(P.width*0.46, P.height/3, this.P);
		roues[1] = new Roue(P.width*0.46, -P.height/3, this.P);
		roues[2] = new Roue(-P.width*0.46, -P.height/3, this.P);
		roues[3] = new Roue(-P.width*0.46, P.height/3, this.P);
		
	}

	@Override
	public void avancer(boolean[] keyPressed, Map map) {
		Position dP = new Position();


		tourner(keyPressed[0], keyPressed[2]);
		accelerer(keyPressed[3], keyPressed[1]);
		/*if(keyPressed[3] == false && keyPressed[1] == false) {
			ralentir();
		}*/


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

	@Override
	protected void accelerer(boolean av, boolean ar){
		sensDir = 0;
		if(av && !ar){ sensDir = 1;}
		if(ar && !av){ sensDir = -1;}


		//ralentir
		if(sensDir == 0){
			vx = vx /F;
			vy = vy /F;
		}



		double ax = 4.37;
		double ay = 4.37;

		vx -= (sensDir*Math.sin(P.getRad())) * ax * dt * 6.22;
		vy += (sensDir*Math.cos(P.getRad())) * ay * dt * 6.22;



	}










	
	@Override
	protected void ralentir( ){


		vx = vx * -Math.sin(P.getRad()) / F;
		vy = vy * Math.cos(P.getRad()) /  F;

		// if(Math.abs(vx) < 0.1){ vx = 0; }
		// if(Math.abs(vy) < 0.1){ vy = 0; }

	}

	@Override
	protected void tourner(boolean g, boolean d){
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

	public Polygon[] getAllPolygons(){
		Polygon[] poly = new Polygon[roues.length+1];
		for(int i=0; i<roues.length; i++){
			poly[i]=roues[i].getPolygon();
		}
		poly[roues.length] = this.getPolygon();
		return poly;
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

	@Override
	public void dessine(Graphics g){
		((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		roues[0].dessine(g);
		roues[1].dessine(g);
		roues[2].dessine(g);
		roues[3].dessine(g);
		texture.dessine(g, P);
		if(showCollisionBox){
			g.setColor(Color.BLACK);
			g.drawPolygon(this.getPolygon());
			g.setColor(Color.BLUE);
			g.drawOval((int)P.x, (int)P.y, 2, 2);
		}

	}

	public void setColor(Color c){
		color = c;
		String path = System.getProperty("user.dir")+"/res/textures/";
		try {
			BufferedImage vide = ImageIO.read(new File(path + "kart_vide.png"));

			BufferedImage kart = new BufferedImage(vide.getWidth(), vide.getHeight(), vide.getType());

			for(int col=0; col<kart.getWidth(); col++){
				for(int lig=0; lig<kart.getHeight(); lig++){
					int v = vide.getRGB(col, lig);

					int a = (v>>24) & 0xff;
					int r = (v>>16) & 0xff;
					int g = (v>>8) & 0xff;
					int b = v & 0xff;
					int pix = ((a*color.getAlpha()/255)<<24) | ((r*color.getRed()/255)<<16) | ((g*color.getGreen()/255)<<8) | (b*color.getBlue()/255);

					kart.setRGB(col, lig, pix);
				}
			}

			texture = new Texture(kart);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}












/*
import javax.imageio.ImageIO;
		import java.awt.*;
		import java.awt.image.BufferedImage;
		import java.io.File;
		import java.io.IOException;

public class Karting extends Vehicule{

	// direction (-1 gauche, 0 droit, 1 droite)
	private int orientation=0;
	private int sensDir=0;

	// description

	// caractéristiques du karting
	private double dt = 0.1;
	private double masse = 10;
	private double F = 1.1; // force de frottement des roues
	private Roue[] roues;

	public Karting(){
		this(new Position(), 20, Color.RED);
	}

	public Karting(Position initPos, int widthCase, Color c){
		//texture = new Texture(System.getProperty("user.dir")+"/res/textures/green.png", "kart");

		setColor(c);

		double widthFactor = 0.7;
		double heightFactor = 1.4;
		this.P = new Position(initPos.x, initPos.y, widthCase*widthFactor, widthCase*heightFactor, initPos.getDeg());
		this.vx = 0;
		this.vy = 0;

		roues = new Roue[4];
		roues[0] = new Roue(P.width*0.46, P.height/3, this.P);
		roues[1] = new Roue(P.width*0.46, -P.height/3, this.P);
		roues[2] = new Roue(-P.width*0.46, -P.height/3, this.P);
		roues[3] = new Roue(-P.width*0.46, P.height/3, this.P);

	}

	@Override
	public void avancer(boolean[] keyPressed, Map map) {
		Position dP = new Position();


		tourner(keyPressed[0], keyPressed[2]);
		accelerer(keyPressed[3], keyPressed[1]);
		/*if(keyPressed[3] == false && keyPressed[1] == false) {
			ralentir();
		}


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

	@Override
	protected void accelerer(boolean av, boolean ar){
		sensDir = 0;
		if(av && !ar){ sensDir = 1;}
		if(ar && !av){ sensDir = -1;}


		//ralentir
		if(sensDir == 0){
			vx = vx /F;
			vy = vy /F;
		}



		double ax = 4.37;
		double ay = 4.37;

		vx -= (sensDir*Math.sin(P.getRad())) * ax * dt * 6.22;
		vy += (sensDir*Math.cos(P.getRad())) * ay * dt * 6.22;



	}











	@Override
	protected void ralentir( ){


		vx = vx * -Math.sin(P.getRad()) / F;
		vy = vy * Math.cos(P.getRad()) /  F;

		// if(Math.abs(vx) < 0.1){ vx = 0; }
		// if(Math.abs(vy) < 0.1){ vy = 0; }

	}

	@Override
	protected void tourner(boolean g, boolean d){
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

	public Polygon[] getAllPolygons(){
		Polygon[] poly = new Polygon[roues.length+1];
		for(int i=0; i<roues.length; i++){
			poly[i]=roues[i].getPolygon();
		}
		poly[roues.length] = this.getPolygon();
		return poly;
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
	}

	@Override
	public void dessine(Graphics g){
		((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		roues[0].dessine(g);
		roues[1].dessine(g);
		roues[2].dessine(g);
		roues[3].dessine(g);
		texture.dessine(g, P);
		if(showCollisionBox){
			g.setColor(Color.BLACK);
			g.drawPolygon(this.getPolygon());
			g.setColor(Color.BLUE);
			g.drawOval((int)P.x, (int)P.y, 2, 2);
		}

	}

	public void setColor(Color c){
		color = c;
		String path = System.getProperty("user.dir")+"/res/textures/";
		try {
			BufferedImage vide = ImageIO.read(new File(path + "kart_vide.png"));

			BufferedImage kart = new BufferedImage(vide.getWidth(), vide.getHeight(), vide.getType());

			for(int col=0; col<kart.getWidth(); col++){
				for(int lig=0; lig<kart.getHeight(); lig++){
					int v = vide.getRGB(col, lig);

					int a = (v>>24) & 0xff;
					int r = (v>>16) & 0xff;
					int g = (v>>8) & 0xff;
					int b = v & 0xff;
					int pix = ((a*color.getAlpha()/255)<<24) | ((r*color.getRed()/255)<<16) | ((g*color.getGreen()/255)<<8) | (b*color.getBlue()/255);

					kart.setRGB(col, lig, pix);
				}
			}

			texture = new Texture(kart);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}*/
