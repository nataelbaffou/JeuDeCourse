
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class FenetrePrincipale extends JFrame implements KeyListener, ActionListener{

	private Joueur J1;
	private Timer timer;
	private static int DELTA_T = 50;
	// (down, left, up, right)
	private LinkedList<Integer> listeTouches = new LinkedList<>();

	public FenetrePrincipale(){
		this.setTitle("Jeu de voiture ULTRA stylé");
		this.setSize(400, 400);

		J1 = new Joueur("test");

		this.addKeyListener(this);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		
		timer = new Timer(DELTA_T, this);
		timer.start();
	}

	public void paint(Graphics g){
		g.setColor(Color.blue);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		J1.dessine(g);
	}

	public void keyPressed(KeyEvent e){
		if(!listeTouches.contains(e.getKeyCode())){
			listeTouches.add(e.getKeyCode());
		}
	}
	
	public void keyReleased(KeyEvent e){
		if(listeTouches.contains(e.getKeyCode())){
			listeTouches.remove((Object)e.getKeyCode());
		}
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == timer){
			J1.avancer(listeTouches);
		}
		repaint();
	}

	
}
