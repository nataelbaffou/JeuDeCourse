
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;

public class FenetrePrincipale extends JFrame implements KeyListener, ActionListener{

	private Vehicule V;
	private Timer timer;
	private static int DELTA_T = 50;
	// (down, left, up, right)
	private boolean[] listeTouches = new boolean[4];

	public FenetrePrincipale(){
		this.setTitle("Jeu de voiture ULTRA stylé");
		this.setSize(400, 400);

		V = new Karting(100, 100);

		this.addKeyListener(this);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		
		timer = new Timer(DELTA_T, this);
		timer.start();
	}

	public void paint(Graphics g){
		g.setColor(Color.blue);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		V.dessine(g);
	}

	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
			case KeyEvent.VK_DOWN: 		{ listeTouches[0] = true; break; }
			case KeyEvent.VK_LEFT: 		{ listeTouches[1] = true; break; }
			case KeyEvent.VK_UP: 		{ listeTouches[2] = true; break; }
			case KeyEvent.VK_RIGHT:		{ listeTouches[3] = true; break; }
		}
	}
	
	public void keyReleased(KeyEvent e){
		switch(e.getKeyCode()){
			case KeyEvent.VK_DOWN: 		{ listeTouches[0] = false; break; }
			case KeyEvent.VK_LEFT: 		{ listeTouches[1] = false; break; }
			case KeyEvent.VK_UP: 		{ listeTouches[2] = false; break; }
			case KeyEvent.VK_RIGHT:		{ listeTouches[3] = false; break; }
		}
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == timer){
			V.ralentir();
			if(listeTouches[0]) { V.freiner(); }
			if(listeTouches[1]) { V.tournerAGauche(); }
			if(listeTouches[2]) { V.accelerer(); }
			if(listeTouches[3]) { V.tournerADroite(); }
			V.avancer();
		}
		repaint();
	}

	
}
