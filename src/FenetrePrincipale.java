
import javax.swing.*;
import java.awt.*;

public class FenetrePrincipale extends JFrame{

	private CardLayout panelSelection;
	private MainMenu mainMenu;
	private GameContent gameContent;
	private JPanel cardContent;

	public FenetrePrincipale(int width, int height){
		this.setTitle("Jeu de voiture ULTRA stylé");

		mainMenu = new MainMenu(width, height);
		gameContent = new GameContent(width, height);
		panelSelection = new CardLayout();

		cardContent = new JPanel();
		cardContent.setLayout(panelSelection);
		cardContent.add(mainMenu, "menu");
		cardContent.add(gameContent, "game");
		setContentPane(cardContent);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
		setFocusable(true);

		panelSelection.show(cardContent, "game");

	}
}
