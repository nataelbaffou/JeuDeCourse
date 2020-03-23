
import javax.swing.*;
import java.awt.*;

public class FenetrePrincipale extends JFrame{

	private CardLayout panelSelection;
	private MainMenu mainMenu;
	private GameContent gameContent;
	private JPanel cardContent;
	private LevelSelector levelSelector;

	public FenetrePrincipale(int width, int height, boolean isFullscreen){
		this.setTitle("Jeu de voiture ULTRA stylé");
		if(isFullscreen){
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setUndecorated(true);
			Dimension n = Toolkit.getDefaultToolkit().getScreenSize();
			width = (int)n.getWidth();
			height = (int)n.getHeight();
		}
		mainMenu = new MainMenu(width, height,this);
		gameContent = new GameContent(width, height,this);
		panelSelection = new CardLayout();
		levelSelector = new LevelSelector(new Dimension(width,height),this);

		cardContent = new JPanel();
		cardContent.setLayout(panelSelection);
		cardContent.add(mainMenu, "menu");
		cardContent.add(gameContent, "game");
		cardContent.add(levelSelector,"levelSelector");
		setContentPane(cardContent);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
		setFocusable(true);

		panelSelection.show(cardContent, "menu");

	}

	public GameContent getGameContent() {
		return gameContent;
	}

	public CardLayout getPanelSelection() {
		return panelSelection;
	}

	public JPanel getCardContent() {
		return cardContent;
	}
}
