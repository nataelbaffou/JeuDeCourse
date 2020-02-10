
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;

public class FenetrePrincipale extends JFrame implements KeyListener{

	private CardLayout panelSelection;
	private MainMenu mainMenu;
	private GameContent gameContent;
	private JPanel cardContent;

	public FenetrePrincipale(){
		this.setTitle("Jeu de voiture ULTRA stylé");

		mainMenu = new MainMenu(400);
		gameContent = new GameContent(400);
		panelSelection = new CardLayout();

		cardContent = new JPanel();
		cardContent.setLayout(panelSelection);
		cardContent.add(mainMenu, "menu");
		cardContent.add(gameContent, "game");
		setContentPane(cardContent);

		addKeyListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
		setFocusable(true);

		panelSelection.last(cardContent);

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		gameContent.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		gameContent.keyReleased(e);
	}
}
