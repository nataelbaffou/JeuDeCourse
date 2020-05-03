package GameObjects;

import GameEngine.Collision;

import javax.imageio.ImageIO;
		import java.awt.*;
		import java.awt.image.BufferedImage;
		import java.io.File;
		import java.io.IOException;
import java.util.ArrayList;

public class Karting extends Vehicule{

	// direction (-1 gauche, 0 droit, 1 droite)
	private int orientation=0;
	private int sensDir=0;

	private static double Rc; // rayon de courbure associé au virage
	// description
	private double widthCaseref = 28;
	private double coefprop;
	private double Fc; // force centrifuge
	private double Ff; // force de frottement opoosé à force centrifuge
	private double vc = 0; //vitesse du dérapage
	private boolean aderape = false;
	private int countderap =0; // compteur de 'combien de fois' il a dérapé
	private Position Pderape = new Position(); // repère des vitesse quand on dérape
	private double normeVavant =0; // C'est la vitesse avant que la voiture ne dérape
	private int gauche = 0;
	private int droite = 0;
	private double kderape;
	private double k; // coefficient de frottement des roues sur la route (perpendiculaire)
	private double F; // force de frottement des roues (parallèle)
	private ArrayList<GameObjects.Case>  casesCollision = new ArrayList<>();
	private int nbcases = 0;
	private Texture texturecase;




	// caractéristiques du karting
	private double dt = 0.15;
	private double masse = 1430;
	private Roue[] roues;


	public Karting(){
		this(new Position(), 20, Color.RED);
	}

	public Karting(Position initPos, int widthCase, Color c){
		//texture = new GameObjects.Texture(System.getProperty("user.dir")+"/res/textures/green.png", "kart");

		setColor(c);

		double widthFactor = 0.7;
		double heightFactor = 1.4;
		this.P = new Position(initPos.x, initPos.y, widthCase*widthFactor, widthCase*heightFactor, initPos.getDeg());
		this.vx = 0;
		this.vy = 0;

		//pour avoir la même vitesse relative sur toutes les map (même faille de l'invocateur)
		coefprop = 6.22 * widthCase / widthCaseref;

		Rc = 4.68 * coefprop;

		roues = new Roue[4];
		roues[0] = new Roue(P.width*0.46, P.height/3, this.P);
		roues[1] = new Roue(P.width*0.46, -P.height/3, this.P);
		roues[2] = new Roue(-P.width*0.46, -P.height/3, this.P);
		roues[3] = new Roue(-P.width*0.46, P.height/3, this.P);

	}

	@Override
	public void avancer(boolean[] keyPressed, Map map) {

		casesCollision = Collision.getCasesInCollision(this, map);
		for(GameObjects.Case n : casesCollision){
			nbcases ++;
			texturecase = n.texture;
			if(k == 0 || kderape == 0 || F == 0) {
				k = texturecase.getk();
				kderape = texturecase.getkderape();
				F = texturecase.getF();
			}else{
				if(texturecase.getk() < k){
					k = texturecase.getk();
				}
				if(texturecase.getkderape() > kderape){
					kderape = texturecase.getkderape();
				}
				if(texturecase.getF() < F){
					F = texturecase.getF();
				}
			}

		}


		Position dP = new Position();

		tourner(keyPressed[0], keyPressed[2]);
		accelerer(keyPressed[3], keyPressed[1]);

		Fc = (masse * normeV * normeV) / (Rc*coefprop);
		Ff = masse * 9.81 * k;

		//System.out.println("Fc : "+ Fc);
		//System.out.println("Ff : "+ Ff);

		if(Fc > 4 * Ff && (keyPressed[0] || keyPressed[2]) && countderap == 0 && (gauche> k ||droite >k )){
			Pderape.x = P.x;
			Pderape.y = P.y;
			Pderape.setDeg(P.getDeg());
			Pderape.substract(dP);

			derapage(keyPressed[0], keyPressed[2]);
			normeVavant = normeV;
			countderap = 1;
			aderape = true;

		}

		if(aderape){

			vx += -1 * vc * Math.sin(Pderape.getRad());
			vy += vc * Math.cos(Pderape.getRad());


			vx = vx /((0.5/kderape)+1);
			vy = vy /((0.5/kderape)+1);
		}




		if(!aderape) {
			vx = -1 * normeV * Math.sin(P.getRad());
			vy = normeV * Math.cos(P.getRad());
		}

		dP.x = vx * dt;
		dP.y = vy * dt;

		if(Math.abs((int)(Pderape.getDeg()-P.getRad())) < 30){
			aderape =false;
			countderap = 0;
		}

		if(Math.abs((int)(normeV))<1) {normeV = 0;}
		if (normeV > 0) {
			dP.setRad(orientation*0.1);
		}
		if (normeV < 0) {
			dP.setRad(-1*orientation*0.1);
		}

		P.add(dP);

		if(Collision.isColliding(this, map) && !aderape){
			// On annule l'avance du véhicule et on stop sa vitesse
			dP.x = 1.2 * dP.x;
			dP.y = 1.2 * dP.y;
			P.substract(dP);
			normeV = 0;

		}

		if(Collision.isColliding(this, map) && aderape){
			// On réduit l'avance du véhicule et on stop sa vitesse
			dP.x = 1.2 * dP.x;
			dP.y = 1.2 * dP.y;
			P.substract(dP);

			aderape = false;
			vc = 0;
			//P.setDeg(Pderape.getDeg());
			int deg =0;
			//System.out.println("début");
			if(keyPressed[0]) {
				while( !Collision.isColliding(this, map) && deg < 360){
					deg ++;
					P.setDeg(P.getDeg() - 1);
					//System.out.println("A");
					normeV = normeV / 1.1;
				}
				P.setDeg((P.getDeg()  + 1));
			}

			if(keyPressed[2]) {
				while( !Collision.isColliding(this, map) && deg < 360){
					deg ++;
					P.setDeg(P.getDeg() + 1);
					//System.out.println("B");
					normeV = normeV / 1.1;
				}
				P.setDeg((P.getDeg() - 1));
			}
		}


		vc = vc/ ((0.5/kderape)+1) ;
		if(vc<0.1){
			vc =0;
			countderap =0;
			aderape = false;
		}
		if(vc !=0){
			normeV = normeV /1.9;
		}
		//System.out.println( "vc :" + vc);

		if (keyPressed[0]) {
			gauche++;
		}else{
			gauche = 0;
		}

		if (keyPressed[2]) {
			droite++;
		}else{
			droite = 0;
		}

		k = 0;
		kderape = 0;
		F = 0;


		//System.out.println("droite :" + droite);
		//System.out.println("gauche :" + gauche);
		//System.out.println("kderape = " + kderape);
		//System.out.println("k = " + k);
		//System.out.println("F = " + F);

	}

	@Override
	protected void accelerer(boolean av, boolean ar){
		sensDir = 0;
		double a =0;
		if(Math.abs((int)(normeV)) <= 13.9 * coefprop){
			a = 4.37;
		}else if(Math.abs((int)(normeV)) > 13.9 * coefprop && Math.abs((int)(normeV)) < 27.8 * coefprop){
			a = 1.87;
		}else if(Math.abs((int)(normeV)) > 27.8 * coefprop && Math.abs((int)(normeV)) < 41.7 * coefprop){
			a = 0.46;
		}

		if(av && !ar && normeV >= 0){
			sensDir =1;
			normeV += coefprop * a * dt * k / 2.5;
		}

		if(ar && !av && normeV <= 0 && Math.abs((int)(normeV)) < 25){
			sensDir =-1;
			normeV -= coefprop * a  * dt *  k  / 2.5;
		}
		//quand on freine
		if(av && !ar && normeV < 0){
			sensDir = -1;
			normeV = normeV / (F * 1.1);  // F*1.1 = nouveau coeff de frottement
			if (Math.abs((int)(normeV)) < 10){
				normeV =0;
			}
		}
		if(ar && !av && normeV > 0){
			sensDir = 1;
			normeV = normeV / (F * 1.1);  // F*1.1 = nouveau coeff de frottement
			if (Math.abs((int)(normeV)) < 10){
				normeV =0;
			}
		}
		// quand on déscelère
		if(sensDir == 0){
			normeV = normeV / F;
		}

		//normeV += sensDir * 6.22 * a * dt;
	}


	@Override
	protected void tourner(boolean g, boolean d){

		if(g && !d && Math.abs((int)(normeV)) > 8){
			orientation = -1;
			roues[0].tournerAGauche();
			roues[3].tournerAGauche();
		} else if(d && !g && Math.abs((int)(normeV)) > 8){
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

	protected void derapage(boolean g,  boolean d) {


		Position derap = new Position();
		vc = (Fc - Ff) * kderape / (100 *masse);
		//System.out.println("vc = " + vc);


		if (d) {
			derap.setDeg(76 * vc - 46);
			P.add(derap);
		}
		if (g) {
			derap.setDeg(76 * vc - 46);
			P.substract(derap);
		}
		//System.out.println("degré = " + derap.getDeg());
	}






/*
	protected void getReachableDirections(GameObjects.Map map){
		ArrayList<GameObjects.Case> casesEnCollision = GameEngine.Collision.isColliding(this, map);
		int[] directions = {1, 1, 1, 1};
		for(GameObjects.Case c : casesEnCollision){
			if(c.isBlocking()){
				int[] caseDirection = GameEngine.Collision.getDirectionOfCollision(this, c);
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
